package org.assistant.sigma.ui.accounts.detail;

import android.util.Log;

import org.assistant.sigma.AbstractPresenter;
import org.assistant.sigma.model.dao.AccountsDao;
import org.assistant.sigma.model.dao.TransactionsDao;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.utils.callbacks.CBGeneric;

/**
 * Created by giovanni on 24/01/18.
 *
 */
public class AccountDetailPresenter implements AbstractPresenter {
    private AccountDetailContract.View view;
    private AccountsDao accountsDao;
    private TransactionsDao transactionsDao;

    AccountDetailPresenter(AccountDetailContract.View view) {
        this.view = view;
        accountsDao = new AccountsDao();
        transactionsDao = new TransactionsDao();

        onCreate();
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

    void loadAccount(String accountId) {
        Account account = accountsDao.find(accountId);
        if (account != null) {
            view.renderAccount(account);
        } else {
            Log.w(
                    getClass().getName(),
                    "Account with id: " + accountId + ". Not found"
            );
        }
    }

    void loadCurrentBalance(String accountId) {
        Transaction lastTrans = transactionsDao.last(accountId);
        if (lastTrans == null) {
            view.renderCurrentBalance(0d);
        } else {
            view.renderCurrentBalance(lastTrans.getCurrentAccountBalance());
        }
    }

    void recalculateBalance(final String accountId) {
        view.toggleLoading(true);
        accountsDao.recalculateBalance(accountId, new CBGeneric<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                loadCurrentBalance(accountId);
                view.toggleLoading(false);
            }
        });
    }
}
