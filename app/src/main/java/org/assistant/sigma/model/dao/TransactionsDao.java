package org.assistant.sigma.model.dao;

import org.assistant.sigma.model.entities.Transaction;

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
}
