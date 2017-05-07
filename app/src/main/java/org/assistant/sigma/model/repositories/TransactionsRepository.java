package org.assistant.sigma.model.repositories;

import org.assistant.sigma.model.entities.Transaction;

import java.util.Calendar;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 *
 * Created by giovanni on 6/05/17.
 */
public class TransactionsRepository {

    private Realm realm = Realm.getDefaultInstance();

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

    public RealmResults<Transaction> lastTransactions() {
        return realm.where(Transaction.class).findAllSorted("createdAt", Sort.DESCENDING);
    }

    public double todaySpent() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        double todaySpent = realm.where(Transaction.class)
                .greaterThan("createdAt", calendar.getTime())
                .lessThan("quantity", (double) 0)
                .sum("quantity")
                .doubleValue();
        return Math.abs(todaySpent);
    }

    public long todayTransactionsCount() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return realm.where(Transaction.class)
                .greaterThan("createdAt", calendar.getTime())
                .count();
    }
}
