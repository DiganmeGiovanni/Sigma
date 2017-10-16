package org.assistant.sigma.ui.scheduled_transactions.weekly.list;

import org.assistant.sigma.model.entities.ScheduledTransactionWeekly;
import org.assistant.sigma.model.repositories.ScheduledTransactionsRepository;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by giovanni on 15/10/17.
 *
 */
public class STWeeklyListPresenter implements STWeeklyListContract.Presenter {
    private STWeeklyListContract.View mSTWeeklyListView;
    private ScheduledTransactionsRepository mSTRepository;

    public STWeeklyListPresenter(STWeeklyListContract.View mSTWeeklyListView) {
        this.mSTWeeklyListView = mSTWeeklyListView;
        this.mSTWeeklyListView.setPresenter(this);

        mSTRepository = new ScheduledTransactionsRepository();
    }

    @Override
    public void onDestroy() {
        mSTRepository.destroy();
    }

    @Override
    public void loadTransactions() {
        RealmResults<ScheduledTransactionWeekly> transactions = mSTRepository.allWeeklyST();
        mSTWeeklyListView.renderTransactions(transactions);

        transactions.addChangeListener(new RealmChangeListener<RealmResults<ScheduledTransactionWeekly>>() {
            @Override
            public void onChange(RealmResults<ScheduledTransactionWeekly> element) {
                mSTWeeklyListView.notifyTransactionsChanged();
            }
        });
    }
}
