package org.assistant.sigma.ui.accounts.detail;

import android.util.Log;

import org.assistant.sigma.AbstractPresenter;
import org.assistant.sigma.model.dao.AccountsDao;
import org.assistant.sigma.model.dao.TransactionsDao;
import org.assistant.sigma.model.dao.UserDao;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.Settings;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.utils.DateFormatter;
import org.assistant.sigma.utils.PeriodUtils;

import java.util.Date;

import io.realm.RealmResults;

/**
 * Created by giovanni on 24/01/18.
 *
 */
public class AccountDetailPresenter implements AbstractPresenter {
    private AccountDetailContract.View view;
    private AccountsDao accountsDao;
    private TransactionsDao transactionsDao;
    private UserDao userDao;

    AccountDetailPresenter(AccountDetailContract.View view) {
        this.view = view;
        accountsDao = new AccountsDao();
        transactionsDao = new TransactionsDao();
        userDao = new UserDao();

        onCreate();
    }

    @Override
    public void onCreate() {
        accountsDao.onCreate();
        transactionsDao.onCreate();
        userDao.onCreate();
    }

    @Override
    public void onDestroy() {
        accountsDao.onDestroy();
        transactionsDao.onDestroy();
        userDao.onDestroy();
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
        RealmResults<Transaction> transactions = transactionsDao.findSorted(
                accountId,
                true
        );
        double balance = 0;
        for (Transaction transaction : transactions) {
            balance = balance + transaction.getQuantity();
        }

        view.renderCurrentBalance(balance);

//        Transaction lastTrans = transactionsDao.last(accountId);
//        if (lastTrans == null) {
//            view.renderCurrentBalance(0d);
//        } else {
//            view.renderCurrentBalance(lastTrans.getCurrentAccountBalance());
//        }
    }

    void loadBalanceAtCurrShortPeriod(String accountId) {
        Settings settings = userDao.findActive().getSettings();
        Date periodStart = PeriodUtils.getCurrentShortPeriodStart(settings);
        String periodStartLb = DateFormatter.asSimpleDateMonth(periodStart);
        view.renderCurrShortPeriodLabel(periodStartLb);

        Transaction lastTrans = transactionsDao.lastUntil(accountId, periodStart);
        if (lastTrans == null) {
            view.renderCurrShortPeriodBalance(0d);
        } else {
            view.renderCurrShortPeriodBalance(lastTrans.getCurrentAccountBalance());
        }
    }

    void loadBalanceAtCurrLargePeriod(String accountId) {
        Settings settings = userDao.findActive().getSettings();
        Date periodStart = PeriodUtils.getCurrentLargePeriodStart(settings);
        String periodStartLb = DateFormatter.asSimpleDateMonth(periodStart);
        view.renderCurrLargePeriodLabel(periodStartLb);

        Transaction lastTrans = transactionsDao.lastUntil(accountId, periodStart);
        if (lastTrans == null) {
            view.renderCurrLargePeriodBalance(0d);
        } else {
            view.renderCurrLargePeriodBalance(lastTrans.getCurrentAccountBalance());
        }
    }


}
