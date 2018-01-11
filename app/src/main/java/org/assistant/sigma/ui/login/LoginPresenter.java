package org.assistant.sigma.ui.login;

import android.util.Log;

import org.assistant.sigma.model.entities.User;
import org.assistant.sigma.model.repositories.SettingsRepository;
import org.assistant.sigma.model.repositories.UsersRepository;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * Created by giovanni on 4/05/17.
 */
class LoginPresenter implements LoginContract.Presenter {

    private final LoginContract.View mLoginView;
    private final SettingsRepository settingsRepository;
    private final UsersRepository usersRepository;

    LoginPresenter(LoginContract.View mLoginView) {
        this.mLoginView = mLoginView;
        this.mLoginView.setPresenter(this);

        settingsRepository = new SettingsRepository();
        usersRepository = new UsersRepository();
    }

    @Override
    public void onDestroy() {
        usersRepository.onDestroy();
        settingsRepository.onDestroy();
    }

    /**
     * Marks a given user as the only active user in application
     * @param user user to activate
     */
    @Override
    public void activateUser(User user) {
        usersRepository.activateUser(user);
    }

    @Override
    public void attemptAutoLogin() {
        mLoginView.setLoadingIndicator(true);
        User user = usersRepository.activeUser();

        if (user != null) {
            mLoginView.goToDashboard();
        } else {
            mLoginView.setLoadingIndicator(false);
        }
    }

    @Override
    public void attemptFBLogin() {
        mLoginView.setLoadingIndicator(true);
        mLoginView.startFBLogin();
    }

    @Override
    public void onFBLoginSuccess(JSONObject jObject) {

        // Retrieve user data from json response
        if (jObject.has("id") && jObject.has("first_name") && jObject.has("email")) {
            try {
                User user = new User();
                user.setFbId(jObject.getLong("id"));
                user.setEmail(jObject.getString("email"));
                user.setFirstName(jObject.getString("first_name"));
                user.setLastName(jObject.has("last_name") ? jObject.getString("last_name") : "");

                // Retrieve picture url
                if (jObject.has("picture") && jObject.getJSONObject("picture").has("data")) {
                    JSONObject data = jObject.getJSONObject("picture").getJSONObject("data");
                    if (data.has("url")) {
                        user.setUrlPicture(data.getString("url"));
                    }
                }

                // Save and activate user
                usersRepository.saveUser(user);
                usersRepository.activateUser(user);
                settingsRepository.createDefaultSettingsForActiveUser();

                // Continue to dashboard
                mLoginView.goToDashboard();

            } catch (JSONException e) {
                Log.e(getClass().getName(), "Message: " + e.getMessage());
                this.onFBLoginError();
            }

        } else {
            this.onFBLoginError();
        }
    }

    @Override
    public void onFBLoginError() {
        mLoginView.setLoadingIndicator(false);
    }
}
