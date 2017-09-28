package org.assistant.sigma.transactions.form;

import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.model.repositories.AccountsRepository;
import org.assistant.sigma.model.repositories.TransactionsRepository;
import org.assistant.sigma.utils.callbacks.CBGeneric;

/**
 *
 * Created by giovanni on 6/05/17.
 */
public class TransactionsFormPresenter implements TransactionsFormContract.Presenter {

    private TransactionsFormContract.View mTransactionsFormView;

    private AccountsRepository accountsRepository;
    private TransactionsRepository transactionsRepository;

    public TransactionsFormPresenter(TransactionsFormContract.View mTransactionsFormView) {
        this.mTransactionsFormView = mTransactionsFormView;
        mTransactionsFormView.setPresenter(this);

        accountsRepository = new AccountsRepository();
        transactionsRepository = new TransactionsRepository();
    }

    @Override
    public void onDestroy() {
        accountsRepository.destroy();
        transactionsRepository.destroy();
    }

    @Override
    public void loadAccounts() {
        mTransactionsFormView.updateAccountsSpinner(accountsRepository.allActive());
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        double balance = accountsRepository.currentBalance(transaction.getAccount());
        transaction.setCurrentAccountBalance(balance + transaction.getQuantity());

        transactionsRepository.insert(transaction);
    }

    @Override
    public void updateTransaction(Transaction transaction, CBGeneric<Boolean> callback) {
        transactionsRepository.update(transaction);
        accountsRepository.recalculateBalance(transaction.getAccount().getId(), callback);
    }

    @Override
    public void loadTransaction(String transactionId) {
        Transaction transaction = transactionsRepository.find(transactionId);
        if (transaction != null) {
            mTransactionsFormView.preloadTransaction(transaction);
        }
    }
}
