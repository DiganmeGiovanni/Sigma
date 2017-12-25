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

    public RealmResults<Transaction> findAccountTransactions(String accountId) {
        return realm.where(Transaction.class)
                .equalTo("account.id", accountId)
                .findAllSorted("createdAt", Sort.DESCENDING);
    }

    /**
     * Calculates the spent amount since a given date
     * @param startDate Date which start calculation
     * @param includeExcludedTransactions If true, even transaction marked for exclude in spent
     *                                    resumes will be included in calculation
     * @param callback A funcion to exec after compute has finished
     */
    public void spentAmountSince(final Date startDate, final boolean includeExcludedTransactions,
                                 final CBGeneric<Double> callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Realm realm = Realm.getDefaultInstance();
                RealmQuery<Transaction> query = realm.where(Transaction.class)
                        .greaterThan("createdAt", startDate)
                        .lessThan("quantity", (double) 0);

                if (!includeExcludedTransactions) {
                    query.equalTo("excludeFromSpentResume", false);
                }

                double spent = query.sum("quantity").doubleValue();
                callback.onResponse(Math.abs(spent));
                realm.close();
            }
        });
    }

    /**
     * Calculates the spent amount since given date
     * @param startDate Only transactions after this date will be considered
     * @param includeExcludedTrans If true, even transactions marked as 'exclude from spent resume'
     *                             will be included in calculation
     * @return The spent amount
     */
    public double spentSince(Date startDate, boolean includeExcludedTrans) {
        RealmQuery<Transaction> query = realm.where(Transaction.class)
                .greaterThan("createdAt", startDate)
                .lessThan("quantity", 0d);

        if (!includeExcludedTrans) {
            query.equalTo("excludeFromSpentResume", false);
        }

        double spent = query.sum("quantity").doubleValue();
        return Math.abs(spent);
    }

    /**
     * Calculates the spent amount since given date for a specific category
     * @param startDate Calculations since
     * @param includeExcludedTrans If true even transactions marked as 'exclude from spent resume'
     *                             will be included in calculation
     * @param categoryId Category to calculate
     * @return The spent amount
     */
    public double spentSince(Date startDate, boolean includeExcludedTrans, int categoryId) {
        RealmQuery<Transaction> query = realm.where(Transaction.class)
                .equalTo("transactionCategory.id", categoryId)
                .greaterThan("createdAt", startDate)
                .lessThan("quantity", 0d);

        if (!includeExcludedTrans) {
            query.equalTo("excludeFromSpentResume", false);
        }

        double spent = query.sum("quantity").doubleValue();
        return Math.abs(spent);
    }
}
