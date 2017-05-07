package org.assistant.sigma.model.repositories;

import org.assistant.sigma.model.entities.User;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class UsersRepository {

    private Realm realm = Realm.getDefaultInstance();

    public User activeUser() {
        return realm.where(User.class).equalTo("active", true).findFirst();
    }

    public void activateUser(User user) {
        RealmResults<User> users = realm.where(User.class).findAll();

        realm.beginTransaction();
        for (User dbUser : users) {
            dbUser.setActive(false);
        }
        user.setActive(true);
        realm.copyToRealmOrUpdate(users);
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();
    }

    public void saveUser(User user) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();
    }
}
