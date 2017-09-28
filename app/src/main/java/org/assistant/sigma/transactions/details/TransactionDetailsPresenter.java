package org.assistant.sigma.transactions.details;

import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.model.repositories.AccountsRepository;
import org.assistant.sigma.model.repositories.TransactionsRepository;
import org.assistant.sigma.utils.callbacks.CBGeneric;

/**
 * Created by giovanni on 27/09/17.
 *
 */
class TransactionDetailsPresenter implements TransactionDetailsContract.Presenter {

    private TransactionDetailsContract.View mDetailsView;
    private TransactionsRepository transactionsRepository;
    private AccountsRepository accountsRepository;

    TransactionDetailsPresenter(TransactionDetailsContract.View mDetailsView) {
        this.mDetailsView = mDetailsView;
        this.mDetailsView.setPresenter(this);

        transactionsRepository = new TransactionsRepository();
        accountsRepository = new AccountsRepository();
    }

    @Override
    public void onDestroy() {
        transactionsRepository.destroy();
        accountsRepository.destroy();
    }

    @Override
    public void loadTransaction(String transactionId) {
        Transaction transaction = transactionsRepository.find(transactionId);
        if (transaction != null) {
            mDetailsView.loadTransaction(transaction);
        }
    }

    @Override
    public void deleteTransaction(String transactionId, CBGeneric<Boolean> callback) {
        String accountId = transactionsRepository.find(transactionId)
                .getAccount()
                .getId();

        transactionsRepository.delete(transactionId);
        accountsRepository.recalculateBalance(accountId, callback);
    }
}
