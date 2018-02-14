package org.assistant.sigma.ui.accounts.detail;

import org.assistant.sigma.AbstractPresenter;
import org.assistant.sigma.model.dao.AccountsDao;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.utils.callbacks.CBGeneric;

import io.realm.RealmResults;

/**
 * Created by giovanni on 13/02/18.
 *
 */
public class AccountsPresenter implements AbstractPresenter {
    private AccountsContract.View view;
    private AccountsDao accountsDao;

    AccountsPresenter(AccountsContract.View view) {
        this.view = view;
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

    void loadAccounts() {
        RealmResults<Account> accounts = accountsDao.allActive();
        view.renderAccounts(accounts);
    }

    void recalculateBalance() {
        view.toggleLoading(true);
        accountsDao.recalculateBalance(new CBGeneric<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                view.toggleLoading(false);
                loadAccounts();
            }
        });
    }
}
