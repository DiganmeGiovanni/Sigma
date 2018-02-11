package org.assistant.sigma.ui.transactions.form;

import android.support.annotation.Nullable;

import org.assistant.sigma.AbstractPresenter;
import org.assistant.sigma.model.dao.AccountsDao;
import org.assistant.sigma.model.dao.TransactionsDao;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.utils.callbacks.CBGeneric;

/**
 * Created by giovanni on 10/02/18.
 *
 */
public class TransactionsFormPresenter implements AbstractPresenter {
    private TransactionsFormContract.View view;
    private AccountsDao accountsDao;
    private TransactionsDao transactionsDao;

    TransactionsFormPresenter(TransactionsFormContract.View view) {
        this.view = view;
        accountsDao = new AccountsDao();
        transactionsDao = new TransactionsDao();
    }

    @Override
    public void onCreate() {
        accountsDao.onCreate();
        transactionsDao.onCreate();
    }

    @Override
    public void onDestroy() {
        accountsDao.onDestroy();
        transactionsDao.onDestroy();
    }

    /**
     * Checks if a given account has sufficient funds to create a transaction
     * for given quantity
     * @param account Account which to check funds
     * @param quantity Amount of transaction to be created
     * @param transactionId Optional, if provided, the calculation will be computed
     *                       taking in account that current transaction amount will
     *                      be reseted
     * @return indicates if account has sufficient funds to create transaction
     */
    boolean hasEnoughFunds(Account account, double quantity, @Nullable String transactionId) {
        Transaction lastTrans = transactionsDao.last(account.getId());
        if (lastTrans != null) {
            double balance = lastTrans.getCurrentAccountBalance();

            if (transactionId != null) {
                Transaction prevTrans = transactionsDao.find(transactionId);
                balance += Math.abs(prevTrans.getQuantity());
            }

            return balance - quantity >= 0;
        }

        return false;
    }

    Transaction lastTransaction() {
        return transactionsDao.last();
    }

    void loadTransaction(String idTransaction) {
        Transaction transaction = transactionsDao.find(idTransaction);
        if (transaction != null) {
            view.preloadTransaction(transaction);
        }
    }

    void loadAccounts() {
        view.renderAccounts(accountsDao.allActive());
    }

    /**
     * Saves or update a given transaction into database
     * NOTE: If an update is required, the balance of implicated accounts
     * is recalculated
     * @param transaction Data to upsert
     */
    void upsert(Transaction transaction, CBGeneric<Boolean> cb) {
        transactionsDao.upsert(transaction, cb);
    }
}
