package org.assistant.sigma.model.dao;

import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.utils.callbacks.CBGeneric;

import java.util.Date;

import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by giovanni on 22/01/18.
 * Data access object for transactions
 */
public class TransactionsDao extends AbstractDao {

    public TransactionsDao() {}

    public RealmResults<Transaction> findByAccount(String accountId) {
        return realm.where(Transaction.class)
                .equalTo("account.id", accountId)
                .findAllSorted("createdAt", Sort.DESCENDING);
    }

    public RealmResults<Transaction> findSorted(String accountId, boolean ascending) {
        RealmQuery<Transaction> query = realm.where(Transaction.class)
                .equalTo("account.id", accountId);

        if (ascending) {
            return query.findAllSorted("createdAt", Sort.ASCENDING);
        } else {
            return query.findAllSorted("createdAt", Sort.DESCENDING);
        }
    }

    public Transaction find(String transactionId) {
        return realm.where(Transaction.class)
                .equalTo("id", transactionId).
                        findFirst();
    }

    public Transaction last() {
        RealmQuery<Transaction> query = realm.where(Transaction.class);
        if (query.count() > 0) {
            return query
                    .findAllSorted("createdAt", Sort.DESCENDING)
                    .first();
        } else {
            return null;
        }
    }

    public Transaction last(String accountId) {
        RealmQuery<Transaction> query = realm.where(Transaction.class)
                .equalTo("account.id", accountId);
        if (query.count() > 0) {
            return query
                    .findAllSorted("createdAt", Sort.DESCENDING)
                    .first();
        } else {
            return null;
        }
    }

    /**
     * Retrieves the last transaction in given account registered
     * before given date
     * @param accountId Id of account
     * @param limit Top limit for query
     * @return Last transaction before given limit
     */
    public Transaction lastUntil(String accountId, Date limit) {
        RealmQuery<Transaction> query = realm.where(Transaction.class)
                .equalTo("account.id", accountId)
                .lessThan("createdAt", limit);
        if (query.count() > 0) {
            return query
                    .findAllSorted("createdAt", Sort.DESCENDING)
                    .first();
        } else {
            return null;
        }
    }

    public void upsert(final Transaction transaction, final CBGeneric<Boolean> cb) {
        realm.beginTransaction();
        Transaction oldTrans = realm.where(Transaction.class)
                .equalTo("id", transaction.getId())
                .findFirst();

        //
        // Calculate balance and insert transaction as new
        if (oldTrans == null) {
            RealmQuery<Transaction> query = realm
                    .where(Transaction.class)
                    .equalTo("account.id", transaction.getAccount().getId());
            if (query.count() > 0) {
                Transaction lastTrans = query
                        .lessThanOrEqualTo("createdAt", transaction.getCreatedAt())
                        .findAllSorted("createdAt", Sort.DESCENDING)
                        .first();
                double balance = lastTrans.getCurrentAccountBalance() + transaction.getQuantity();
                transaction.setCurrentAccountBalance(balance);
                realm.copyToRealmOrUpdate(transaction);

                // If transaction was inserted before another existing transactions, update
                // the balance for those
                RealmResults<Transaction> postTransactions = realm
                        .where(Transaction.class)
                        .equalTo("account.id", transaction.getAccount().getId())
                        .greaterThan("createdAt", transaction.getCreatedAt())
                        .findAllSorted("createdAt", Sort.ASCENDING);
                for (Transaction postTransaction : postTransactions) {
                    balance = balance + postTransaction.getQuantity();
                    postTransaction.setCurrentAccountBalance(balance);
                }
            } else {
                double balance = transaction.getQuantity();
                transaction.setCurrentAccountBalance(balance);
                realm.copyToRealmOrUpdate(transaction);
            }
        }

        //
        // Update trans and recalculate accounts balances
        else {
            String oldAccountId = oldTrans.getAccount().getId();
            String newAccountId = transaction.getAccount().getId();
            realm.copyToRealmOrUpdate(transaction);

            //
            // Recalculate balance for old account
            RealmResults<Transaction> transactions = realm.where(Transaction.class)
                    .equalTo("account.id", oldAccountId)
                    .findAllSorted("createdAt", Sort.ASCENDING);
            double balance = 0;
            for (Transaction transactionX : transactions) {
                balance += transactionX.getQuantity();
                transactionX.setCurrentAccountBalance(balance);
            }

            //
            // Recalculate balance for new account if is different than old
            if (!oldAccountId.equals(newAccountId)) {
                RealmResults<Transaction> transactionsNewAccount = realm
                        .where(Transaction.class)
                        .equalTo("account.id", newAccountId)
                        .findAllSorted("createdAt", Sort.ASCENDING);
                double balanceNewAccount = 0;
                for (Transaction transactionX : transactionsNewAccount) {
                    balanceNewAccount += transactionX.getQuantity();
                    transactionX.setCurrentAccountBalance(balanceNewAccount);
                }
            }
        }

        realm.commitTransaction();
        cb.onResponse(true);
    }

    public void delete(String transactionId) {
        Transaction transaction = find(transactionId);
        if (transaction != null) {
            realm.beginTransaction();
            transaction.deleteFromRealm();
            realm.commitTransaction();
        }
    }
}
