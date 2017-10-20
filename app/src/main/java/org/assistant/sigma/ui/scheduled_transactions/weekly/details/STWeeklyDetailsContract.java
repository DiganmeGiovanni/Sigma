package org.assistant.sigma.ui.scheduled_transactions.weekly.details;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.BaseView;
import org.assistant.sigma.model.entities.ScheduledTransactionWeekly;

/**
 * Created by giovanni on 19/10/17.
 *
 */
class STWeeklyDetailsContract {

    interface Presenter extends BasePresenter {
        void deleteSTWeekly(String stWeeklyId);

        void loadSTWeekly(String stWeeklyId);
    }

    interface View extends BaseView<Presenter> {
        void renderSTWeekly(ScheduledTransactionWeekly stWeekly);

        void goToEditSTWeekly(String stWeeklyId);

        void onDeleteBtnClicked();
    }
}
