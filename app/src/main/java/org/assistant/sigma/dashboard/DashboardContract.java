package org.assistant.sigma.dashboard;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.BaseView;
import org.assistant.sigma.model.entities.Transaction;

import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 5/05/17.
 */
interface DashboardContract {

    interface Presenter extends BasePresenter {

        boolean allowAddTransaction();

        void loadLastTransactions();

        void loadPeriodSmAmount();

        void loadPeriodSmTransactionsCount();
    }

    interface View extends BaseView<Presenter> {

        void goToAddTransaction();

        void goToAccounts();

        void showPeriodSmAmount(double periodSmSpent, int WARNING_LEVEL);

        void showPeriodSmTransactionsCount(long transactionsCount);

        void updateLastTransactions(RealmResults<Transaction> transactions);
    }
}
