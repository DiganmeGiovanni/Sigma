package org.assistant.sigma.dashboard;

import org.assistant.sigma.dashboard.DashboardContract.Presenter;
import org.assistant.sigma.model.repositories.AccountsRepository;
import org.assistant.sigma.model.repositories.TransactionsRepository;
import org.assistant.sigma.utils.Warning;

/**
 *
 * Created by giovanni on 5/05/17.
 */
class DashboardPresenter implements Presenter {

    private final DashboardContract.View mDashboardView;
    private AccountsRepository accountsRepository;
    private TransactionsRepository transactionsRepository;

    DashboardPresenter(DashboardContract.View mDashboardView) {
        this.mDashboardView = mDashboardView;
        mDashboardView.setPresenter(this);

        accountsRepository = new AccountsRepository();
        transactionsRepository = new TransactionsRepository();
    }

    @Override
    public void start() { }

    @Override
    public boolean allowAddTransaction() {
        return accountsRepository.allActive().size() > 0;
    }

    @Override
    public void loadLastTransactions() {
        mDashboardView.updateLastTransactions(transactionsRepository.lastTransactions());
    }

    @Override
    public void loadPeriodSmAmount() {
        // TODO Check for period sm chosen

        double todaySpent = transactionsRepository.todaySpent();
        // TODO Check for required warning

        mDashboardView.showPeriodSmAmount(todaySpent, Warning.LEVEL_NORMAL);
    }

    @Override
    public void loadPeriodSmTransactionsCount() {
        // TODO Check for period sm chosen
        long transactionsCount = transactionsRepository.todayTransactionsCount();

        mDashboardView.showPeriodSmTransactionsCount(transactionsCount);
    }
}
