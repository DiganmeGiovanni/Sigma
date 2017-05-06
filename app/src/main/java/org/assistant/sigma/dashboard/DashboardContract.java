package org.assistant.sigma.dashboard;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.BaseView;

/**
 *
 * Created by giovanni on 5/05/17.
 */
interface DashboardContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {

        void goToAccounts();
    }
}
