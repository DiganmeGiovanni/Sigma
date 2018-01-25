package org.assistant.sigma.ui.accounts.list;

import org.assistant.sigma.AbstractPresenter;
import org.assistant.sigma.model.dao.TransactionsDao;
import org.assistant.sigma.model.dao.UserDao;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.model.entities.User;

import io.realm.RealmChangeListener;
import io.realm.RealmList;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class AccountsListPresenter implements AbstractPresenter {
    private final AccountsListContract.View mAccountsListView;
    private TransactionsDao transactionsDao;
    private UserDao userDao;

    AccountsListPresenter(AccountsListContract.View mAccountsListView) {
        this.mAccountsListView = mAccountsListView;
        transactionsDao = new TransactionsDao();
        userDao = new UserDao();
    }

    @Override
    public void onCreate() {
        transactionsDao.onCreate();
        userDao.onCreate();
    }

    @Override
    public void onDestroy() {
        transactionsDao.onDestroy();
        userDao.onDestroy();
    }

    double currentBalance(String accountId) {
        Transaction last = transactionsDao.last(accountId);
        if (last != null) {
            return last.getCurrentAccountBalance();
        }

        return 0;
    }

    void loadAccounts() {
        User user = userDao.findActive();
        RealmList<Account> accounts = user.getAccounts();
        accounts.addChangeListener(new RealmChangeListener<RealmList<Account>>() {
            @Override
            public void onChange(RealmList<Account> sameAccounts) {
                mAccountsListView.notifyAccountsListChanged();
            }
        });

        mAccountsListView.updateAccountsList(accounts);
    }
}
