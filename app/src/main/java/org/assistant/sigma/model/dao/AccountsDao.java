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
     * Sort all account transactions by creation date and recalculates
     * the {@link Transaction#currentAccountBalance} for each transaction
     *
     * @param accountId Id of account to recalculate
     * @param callback  The task will be executed in async mode and callback invoked
     *                  on recalculation completed
     */
    public void recalculateBalance(final String accountId, final CBGeneric<Boolean> callback) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Account account = realm.where(Account.class).equalTo("id", accountId).findFirst();
                RealmResults<Transaction> transactions = account.getTransactions()
                        .sort("createdAt", Sort.ASCENDING);

                double balance = 0;
                for (Transaction transaction : transactions) {
                    balance += transaction.getQuantity();
                    transaction.setCurrentAccountBalance(balance);
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                callback.onResponse(true);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e(
                        AccountsDao.this.getClass().getName(),
                        "Error while recalculating balance: " + error.getMessage()
                );

                callback.onResponse(false);
            }
        });
    }
}
