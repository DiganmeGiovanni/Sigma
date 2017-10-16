package org.assistant.sigma.ui.scheduled_transactions.monthly.list;

import org.assistant.sigma.model.entities.ScheduledTransactionMonthly;
import org.assistant.sigma.model.repositories.ScheduledTransactionsRepository;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by giovanni on 15/10/17.
 *
 */
public class STMonthlyListPresenter implements STMonthlyListContract.Presenter {
    private STMonthlyListContract.View mSTMonthlyListView;
    private ScheduledTransactionsRepository mSTRepository;

    public STMonthlyListPresenter(STMonthlyListContract.View mSTMonthlyListView) {
        this.mSTMonthlyListView = mSTMonthlyListView;
        this.mSTMonthlyListView.setPresenter(this);

        mSTRepository = new ScheduledTransactionsRepository();
    }

    @Override
    public void onDestroy() {
        mSTRepository.destroy();
    }

    @Override
    public void loadTransactions() {
        RealmResults<ScheduledTransactionMonthly> transactions = mSTRepository.allMonthlyST();
        mSTMonthlyListView.renderTransactions(transactions);

        transactions.addChangeListener(new RealmChangeListener<RealmResults<ScheduledTransactionMonthly>>() {
            @Override
            public void onChange(RealmResults<ScheduledTransactionMonthly> element) {
                mSTMonthlyListView.notifyTransactionsChanged();
            }
        });
    }
}
