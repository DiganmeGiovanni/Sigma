package org.assistant.sigma.ui.transactions.listing;

import org.assistant.sigma.AbstractPresenter;
import org.assistant.sigma.model.dao.AccountsDao;
import org.assistant.sigma.model.dao.TransactionsDao;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.Transaction;

import io.realm.RealmResults;

/**
 * Created by giovanni on 24/12/17.
 *
 */
public class TransactionsPresenter implements AbstractPresenter {
    private TransactionsDao transactionsDao;
    private AccountsDao accountsDao;

    TransactionsPresenter() {
        transactionsDao = new TransactionsDao();
        accountsDao = new AccountsDao();
    }

    @Override
    public void onCreate() {
        transactionsDao.onCreate();
        accountsDao.onCreate();
    }

    @Override
    public void onDestroy() {
        transactionsDao.onDestroy();
        accountsDao.onDestroy();
    }

    public RealmResults<Account> getAccounts() {
        return accountsDao.allActive();
    }

    RealmResults<Transaction> getTransactions(String accountId) {
        return transactionsDao.findByAccount(accountId);
    }
}
