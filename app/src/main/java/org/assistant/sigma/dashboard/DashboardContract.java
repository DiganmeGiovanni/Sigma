package org.assistant.sigma.dashboard;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.BaseView;
import org.assistant.sigma.model.entities.Settings;

import java.util.Date;

/**
 *
 * Created by giovanni on 5/05/17.
 */
interface DashboardContract {

    interface Presenter extends BasePresenter {

        Settings activeUserSettings();

        boolean allowAddTransaction();

        void loadLastTransactionTime();

        void loadSpentPeriodLg(String... excludedCategoriesNames);

        void loadSpentPeriodSm();

        Date largePeriodStartDate();

        Date shortPeriodStartDate();
    }

    interface View extends BaseView<Presenter> {

        void showLastTransactionTime(Date date);

        void showShortPeriodLabel();

        void showSpentPeriodSm(double amount, int WARNING_LEVEL);

        void showSpentPeriodLg(double amount, double beforeLimit, int WARNING_LEVEL);
    }
}
