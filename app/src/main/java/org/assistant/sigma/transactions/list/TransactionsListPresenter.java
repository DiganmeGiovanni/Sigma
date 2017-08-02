package org.assistant.sigma.transactions.list;

import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.model.repositories.TransactionsRepository;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 21/05/17.
 */
public class TransactionsListPresenter implements TransactionsListContract.Presenter {

    private TransactionsListContract.View mTransactionsListView;
    private TransactionsRepository transactionsRepository;

    public TransactionsListPresenter(TransactionsListContract.View mTransactionsListView) {
        this.mTransactionsListView = mTransactionsListView;
        mTransactionsListView.setPresenter(this);

        transactionsRepository = new TransactionsRepository();
    }

    @Override
    public void loadLastTransactions() {
        RealmResults<Transaction> transactions = transactionsRepository.lastTransactions();
        mTransactionsListView.renderTransactions(transactions);

        transactions.addChangeListener(new RealmChangeListener<RealmResults<Transaction>>() {
            @Override
            public void onChange(RealmResults<Transaction> element) {
                mTransactionsListView.notifyTransactionsChanged();
            }
        });
    }

    @Override
    public void onDestroy() {
        transactionsRepository.destroy();
    }
}
