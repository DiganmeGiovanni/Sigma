package org.assistant.sigma.ui.scheduled_transactions.weekly.list;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.BaseView;
import org.assistant.sigma.model.entities.ScheduledTransactionWeekly;

import io.realm.RealmResults;

/**
 * Created by giovanni on 15/10/17.
 *
 */
interface STWeeklyListContract {

    interface Presenter extends BasePresenter {

        void loadTransactions();
    }

    interface View extends BaseView<Presenter> {

        void renderTransactions(RealmResults<ScheduledTransactionWeekly> transactions);
    }
}
