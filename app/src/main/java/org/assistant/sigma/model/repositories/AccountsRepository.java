package org.assistant.sigma.model.repositories;

import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.User;

import io.realm.Realm;
import io.realm.RealmList;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class AccountsRepository {

    private Realm realm = Realm.getDefaultInstance();

    public RealmList<Account> userAccounts(User user) {
        return user.getAccounts();
    }

    public void save(Account account, User user) {
        realm.beginTransaction();
        user.getAccounts().add(account);
        realm.commitTransaction();
    }
}
