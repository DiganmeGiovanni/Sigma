package org.assistant.sigma.dashboard;

import org.assistant.sigma.dashboard.DashboardContract.Presenter;
import org.assistant.sigma.model.catalogs.Periods;
import org.assistant.sigma.model.entities.Settings;
import org.assistant.sigma.model.repositories.AccountsRepository;
import org.assistant.sigma.model.repositories.TransactionsRepository;
import org.assistant.sigma.model.repositories.UsersRepository;
import org.assistant.sigma.utils.Warning;
import org.assistant.sigma.utils.callbacks.CBGeneric;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * Created by giovanni on 5/05/17.
 */
class DashboardPresenter implements Presenter {

    private final DashboardContract.View mDashboardView;
    private AccountsRepository accountsRepository;
    private TransactionsRepository transactionsRepository;
    private UsersRepository usersRepository;

    DashboardPresenter(DashboardContract.View mDashboardView) {
        this.mDashboardView = mDashboardView;
        mDashboardView.setPresenter(this);

        accountsRepository = new AccountsRepository();
        transactionsRepository = new TransactionsRepository();
        usersRepository = new UsersRepository();
    }

    @Override
    public void start() { }

    @Override
    public Settings activeUserSettings() {
        return usersRepository.activeUser().getSettings();
    }

    @Override
    public boolean allowAddTransaction() {
        return accountsRepository.allActive().size() > 0;
    }

    @Override
    public void loadLastTransactions() {
        mDashboardView.updateLastTransactions(transactionsRepository.lastTransactions());
    }

    @Override
    public void loadPeriodLgAmount(String... excludedCategoriesNames) {
        final double spentLimit = usersRepository.activeUser().getSettings().getSpentLimitLarge();
        transactionsRepository.spentSince(
                largePeriodStartDate(),
                excludedCategoriesNames,
                new CBGeneric<Double>() {
                    @Override
                    public void onResponse(Double spent) {
                        int warningLevel = Warning.LEVEL_NORMAL;
                        if (spent >= spentLimit) {
                            warningLevel = Warning.LEVEL_DANGER;
                        } else if (spent >= spentLimit/3) {
                            warningLevel = Warning.LEVEL_WARNING;
                        }

                        mDashboardView.showPeriodLgAmount(spent, spentLimit - spent, warningLevel);
                    }
                }
        );
    }

    @Override
    public void loadPeriodLgTransactionsCount() {
        transactionsRepository.transactionsSince(largePeriodStartDate(), new CBGeneric<Long>() {
            @Override
            public void onResponse(Long count) {
                mDashboardView.showPeriodLgTransactionsCount(count);
            }
        });
    }

    @Override
    public void loadPeriodSmAmount() {
        transactionsRepository.spentSince(shortPeriodStartDate(), null, new CBGeneric<Double>() {
            @Override
            public void onResponse(Double spent) {
                mDashboardView.showPeriodSmAmount(spent, Warning.LEVEL_NORMAL);
            }
        });
    }

    @Override
    public void loadPeriodSmTransactionsCount() {
        transactionsRepository.transactionsSince(shortPeriodStartDate(), new CBGeneric<Long>() {
            @Override
            public void onResponse(Long count) {
                mDashboardView.showPeriodSmTransactionsCount(count);
            }
        });
    }

    /**
     * Determinate the large period start date
     * based on active users preferences
     *
     * @return Start date for large period
     */
    @Override
    public Date largePeriodStartDate() {
        Settings settings = usersRepository.activeUser().getSettings();

        Calendar startDate = Calendar.getInstance();
        int TODAY = startDate.get(Calendar.DAY_OF_WEEK);
        int NOW_HOUR = startDate.get(Calendar.HOUR_OF_DAY);
        int START_HOUR = settings.getStartDayHour();

        switch (settings.getLargePeriod()) {
            case Periods.LG_WEEKLY:
                if (TODAY == Calendar.MONDAY && NOW_HOUR < START_HOUR) {
                    startDate.set(Calendar.WEEK_OF_YEAR, startDate.get(Calendar.WEEK_OF_YEAR) - 1);
                } else {
                    startDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                }
                break;

            case Periods.LG_FORTNIGHT:
                if (startDate.get(Calendar.DAY_OF_MONTH) == 15 && NOW_HOUR < START_HOUR) {
                    startDate.set(Calendar.DAY_OF_MONTH, 1);
                }
                else if (startDate.get(Calendar.DAY_OF_MONTH) >= 15) {
                    startDate.set(Calendar.DAY_OF_MONTH, 15);
                } else if (startDate.get(Calendar.DAY_OF_MONTH) == 1 & NOW_HOUR < START_HOUR) {
                    startDate.set(Calendar.DAY_OF_MONTH, 15);
                } else {
                    startDate.set(Calendar.DAY_OF_MONTH, 1);
                }
                break;

            case Periods.LG_MONTHLY:
                if (startDate.get(Calendar.DAY_OF_MONTH) == 1 && NOW_HOUR < START_HOUR) {
                    startDate.set(Calendar.MONTH, startDate.get(Calendar.MONTH) - 1);
                }

                startDate.set(Calendar.DAY_OF_MONTH, 1);
                break;
        }

        startDate.set(Calendar.HOUR_OF_DAY, START_HOUR);
        return startDate.getTime();
    }

    /**
     * Determinate the short period start date
     * based on active users settings
     *
     * @return Start date for short period
     */
    @Override
    public Date shortPeriodStartDate() {
        Settings settings = usersRepository.activeUser().getSettings();

        Calendar startDate = Calendar.getInstance();
        int TODAY = startDate.get(Calendar.DAY_OF_WEEK);
        int NOW_HOUR = startDate.get(Calendar.HOUR_OF_DAY);
        int START_HOUR = settings.getStartDayHour();

        switch (settings.getSmallPeriod()) {
            case Periods.SM_DAILY:
                if (NOW_HOUR < START_HOUR) {
                    startDate.set(Calendar.DAY_OF_MONTH, startDate.get(Calendar.DAY_OF_MONTH) - 1);
                }
                break;

            case Periods.SM_DAILY_GROUPED_WEEKEND:
                if (TODAY == Calendar.SATURDAY || TODAY == Calendar.SUNDAY) {
                    startDate.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                } else if (TODAY == Calendar.MONDAY && NOW_HOUR < START_HOUR) {
                    startDate.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                } else if (NOW_HOUR < START_HOUR) {
                    startDate.set(Calendar.DAY_OF_MONTH, startDate.get(Calendar.DAY_OF_MONTH) - 1);
                }
                break;
        }

        startDate.set(Calendar.HOUR_OF_DAY, START_HOUR);
        return startDate.getTime();
    }
}
