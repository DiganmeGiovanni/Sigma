package org.assistant.sigma.model.dao;

import org.assistant.sigma.model.entities.User;

/**
 * Created by giovanni on 24/01/18.
 *
 */
public class UserDao extends AbstractDao {

    public User findActive() {
        return realm.where(User.class)
                .equalTo("active", true)
                .findFirst();
    }
}
