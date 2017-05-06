package org.assistant.sigma.dashboard;

import org.assistant.sigma.dashboard.DashboardContract.Presenter;

import io.realm.Realm;

/**
 *
 * Created by giovanni on 5/05/17.
 */
class DashboardPresenter implements Presenter {

    private final DashboardContract.View mDashboardView;
    private Realm realm;

    DashboardPresenter(DashboardContract.View mDashboardView) {
        this.mDashboardView = mDashboardView;
        mDashboardView.setPresenter(this);

        this.realm = Realm.getDefaultInstance();
    }

    @Override
    public void start() {

    }
}
