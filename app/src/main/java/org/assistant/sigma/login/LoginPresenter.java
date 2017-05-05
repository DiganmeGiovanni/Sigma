package org.assistant.sigma.login;

import android.util.Log;

import org.assistant.sigma.model.entities.User;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 4/05/17.
 */
class LoginPresenter implements LoginContract.Presenter {

    private final LoginContract.View mLoginView;
    private Realm realm;

    LoginPresenter(LoginContract.View mLoginView) {
        this.mLoginView = mLoginView;
        mLoginView.setPresenter(this);

        this.realm = Realm.getDefaultInstance();
    }

    /**
     * Marks a given user as the only active user in application
     * @param user user to activate
     */
    @Override
    public void activateUser(User user) {
        RealmResults<User> users = realm.where(User.class).findAll();

        realm.beginTransaction();
        for (User dbUser : users) {
            dbUser.setActive(false);
        }
        user.setActive(true);
        realm.copyToRealmOrUpdate(users);
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();
    }

    @Override
    public void attemptAutoLogin() {
        mLoginView.setLoadingIndicator(true);
        User user = realm.where(User.class).equalTo("active", true).findFirst();

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

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(user);
                realm.commitTransaction();

                // Activate user and continue to dashboard
                activateUser(user);
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

    @Override
    public void start() { }
}
