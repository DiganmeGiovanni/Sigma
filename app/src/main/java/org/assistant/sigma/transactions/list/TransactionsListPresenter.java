package org.assistant.sigma.transactions.list;

import org.assistant.sigma.model.repositories.TransactionsRepository;

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
        mTransactionsListView.renderTransactions(transactionsRepository.lastTransactions());
    }
}
