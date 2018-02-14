package org.assistant.sigma.model.dao;

import android.util.Log;

import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.utils.callbacks.CBGeneric;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by giovanni on 22/01/18.
 *
 */
public class AccountsDao extends AbstractDao {

    public AccountsDao() { }

    public RealmResults<Account> allActive() {
        return realm.where(Account.class).equalTo("active", true).findAll();
    }

    public Account find(String accountId) {
        return realm.where(Account.class)
                .equalTo("id", accountId)
                .findFirst();
    }

    /**
     * Recalculates the balance for each transaction on each account
     * since begin of times
     * @param cb Callback to exec on recalculation completes
     */
    public void recalculateBalance(final CBGeneric<Boolean> cb) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Account> accounts = realm.where(Account.class).findAll();
                for (Account account : accounts) {
                    RealmResults<Transaction> transactions = realm.where(Transaction.class)
                            .equalTo("account.id", account.getId())
                            .findAllSorted("createdAt", Sort.ASCENDING);
                    double balance = 0;
                    for (Transaction transaction : transactions) {
                        balance = balance + transaction.getQuantity();
                        transaction.setCurrentAccountBalance(balance);
                    }
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                cb.onResponse(true);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e(
                        AccountsDao.this.getClass().getName(),
                        "Error while recalculating balance: " + error.getMessage()
                );

                cb.onResponse(false);
            }
        });
    }

    public void delete(String accountId) {
        Account account = find(accountId);
        if (account != null) {
            realm.beginTransaction();
            account.setActive(false);
            realm.copyToRealmOrUpdate(account);
            realm.commitTransaction();
        }
    }
}
