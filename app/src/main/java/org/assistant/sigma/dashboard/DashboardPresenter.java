package org.assistant.sigma.dashboard;

import org.assistant.sigma.dashboard.DashboardContract.Presenter;
import org.assistant.sigma.model.repositories.AccountsRepository;

/**
 *
 * Created by giovanni on 5/05/17.
 */
class DashboardPresenter implements Presenter {

    private final DashboardContract.View mDashboardView;
    private AccountsRepository accountsRepository;

    DashboardPresenter(DashboardContract.View mDashboardView) {
        this.mDashboardView = mDashboardView;
        mDashboardView.setPresenter(this);

        accountsRepository = new AccountsRepository();
    }

    @Override
    public void start() { }

    @Override
    public boolean allowAddTransaction() {
        return accountsRepository.allActive().size() > 0;
    }
}
