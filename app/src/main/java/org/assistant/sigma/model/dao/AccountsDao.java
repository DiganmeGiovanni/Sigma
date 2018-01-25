package org.assistant.sigma.model.dao;

import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.Transaction;

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

    public RealmResults<Transaction> byLastUsage() {
        return realm.where(Transaction.class)
                .findAllSorted("createdAt", Sort.DESCENDING);
    }
}
