package org.assistant.sigma.model.dao;

import org.assistant.sigma.model.entities.Account;

import io.realm.RealmResults;

/**
 * Created by giovanni on 22/01/18.
 *
 */
public class AccountsDao extends AbstractDao {

    public AccountsDao() { }

    public RealmResults<Account> allActive() {
        return realm.where(Account.class).equalTo("active", true).findAll();
    }
}
