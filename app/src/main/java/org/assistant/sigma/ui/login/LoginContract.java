package org.assistant.sigma.ui.login;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.BaseView;
import org.assistant.sigma.model.entities.User;
import org.json.JSONObject;

/**
 *
 * Created by giovanni on 4/05/17.
 */
interface LoginContract {

    interface Presenter extends BasePresenter {

        void activateUser(User user);

        void attemptAutoLogin();

        void attemptFBLogin();

        void onFBLoginSuccess(JSONObject jObject);

        void onFBLoginError();
    }

    interface View extends BaseView<Presenter> {

        void goToDashboard();

        void setLoadingIndicator(boolean isLoading);

        void startFBLogin();
    }
}
