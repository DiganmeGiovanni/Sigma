package org.assistant.sigma.model.repositories;

import android.os.AsyncTask;

import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.utils.callbacks.CBGeneric;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 *
 * Created by giovanni on 6/05/17.
 */
public class TransactionsRepository {

    private Realm realm;

    public TransactionsRepository() {
        realm = Realm.getDefaultInstance();
    }

    public void destroy() {
        realm.close();
    }

    public void insert(Transaction transaction) {
        realm.beginTransaction();
        Transaction transactionDB = realm.createObject(
                Transaction.class,
                UUID.randomUUID().toString()
        );
        transactionDB.setCreatedAt(transaction.getCreatedAt());
        transactionDB.setQuantity(transaction.getQuantity());
        transactionDB.setDescription(transaction.getDescription());
        transactionDB.setCurrentAccountBalance(transaction.getCurrentAccountBalance());

        transactionDB.setTransactionCategory(transaction.getTransactionCategory());
        transactionDB.setAccount(transaction.getAccount());
        transactionDB.getAccount().getTransactions().add(transactionDB);
        realm.commitTransaction();
    }

    public void update(Transaction transaction) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(transaction);
        realm.commitTransaction();
    }

    public Transaction find(String transactionId) {
        return realm.where(Transaction.class)
                .equalTo("id", transactionId).
                        findFirst();
    }

    public Transaction findLatest() {
        RealmResults<Transaction> all = realm.where(Transaction.class)
                .findAllSorted("createdAt", Sort.DESCENDING);
        if (all.size() > 0) {
            return all.get(0);
        }

        return null;
    }

    public void delete(String transactionId) {
        Transaction transaction = find(transactionId);
        if (transaction != null) {
            realm.beginTransaction();
            transaction.deleteFromRealm();
            realm.commitTransaction();
        }
    }

    public RealmResults<Transaction> lastTransactions() {
        return realm.where(Transaction.class).findAllSorted("createdAt", Sort.DESCENDING);
    }

    public Transaction lastTransaction() {
        if (lastTransactions().size() > 0) {
            return lastTransactions().first();
        }

        return null;
    }

    public void spentSince(final Date startDate, final String[] excludedCategoriesNames,
                           final CBGeneric<Double> callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Realm realm = Realm.getDefaultInstance();
                RealmQuery<Transaction> query = realm.where(Transaction.class)
                        .greaterThan("createdAt", startDate)
                        .lessThan("quantity", (double) 0);

                if (excludedCategoriesNames != null && excludedCategoriesNames.length > 0) {
                    query.not().in("transactionCategory.name", excludedCategoriesNames);
                }

                double spent = query.sum("quantity").doubleValue();
                callback.onResponse(Math.abs(spent));
                realm.close();
            }
        });
    }

    public void transactionsSince(final Date startDate, final CBGeneric<Long> callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Realm realm = Realm.getDefaultInstance();
                long count = realm.where(Transaction.class)
                        .greaterThan("createdAt", startDate)
                        .count();
                callback.onResponse(count);
                realm.close();
            }
        });
    }
}
