package org.assistant.sigma.dashboard;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.BaseView;
import org.assistant.sigma.model.entities.Settings;
import org.assistant.sigma.model.entities.Transaction;

import java.util.Date;

import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 5/05/17.
 */
interface DashboardContract {

    interface Presenter extends BasePresenter {

        Settings activeUserSettings();

        boolean allowAddTransaction();

        void loadLastTransactions();

        void loadPeriodLgAmount(String... excludedCategoriesNames);

        void loadPeriodLgTransactionsCount();

        void loadPeriodSmAmount();

        void loadPeriodSmTransactionsCount();

        Date largePeriodStartDate();

        Date shortPeriodStartDate();
    }

    interface View extends BaseView<Presenter> {

        void goToAddTransaction();

        void goToAccounts();

        void showPeriodLgAmount(double amount, double beforeLimit, int WARNING_LEVEL);

        void showPeriodLgTransactionsCount(long count);

        void showPeriodsNames();

        void showPeriodSmAmount(double amount, int WARNING_LEVEL);

        void showPeriodSmTransactionsCount(long count);

        void updateLastTransactions(RealmResults<Transaction> transactions);
    }
}
