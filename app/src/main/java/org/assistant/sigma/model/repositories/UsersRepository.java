package org.assistant.sigma.model.repositories;

import org.assistant.sigma.model.entities.User;

import io.realm.Realm;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class UsersRepository {

    private Realm realm = Realm.getDefaultInstance();

    public User activeUser() {
        return realm.where(User.class).equalTo("active", true).findFirst();
    }
}
