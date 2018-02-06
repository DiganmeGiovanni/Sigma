package org.assistant.sigma.ui.accounts.list;

import org.assistant.sigma.AbstractPresenter;
import org.assistant.sigma.model.dao.AccountsDao;
import org.assistant.sigma.model.entities.Account;

import io.realm.RealmResults;

/**
 * Created by giovanni on 29/01/18.
 *
 */
public class AccountsPresenter implements AbstractPresenter {
    private AccountsDao accountsDao;

    public AccountsPresenter() {
        accountsDao = new AccountsDao();
    }

    @Override
    public void onCreate() {
        accountsDao.onCreate();
    }

    @Override
    public void onDestroy() {
        accountsDao.onDestroy();
    }

    public RealmResults<Account> getAccounts() {
        return accountsDao.allActive();
    }
}
