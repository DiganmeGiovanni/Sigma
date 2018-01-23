package org.assistant.sigma.model.dao;

import io.realm.Realm;

/**
 * Created by giovanni on 22/01/18.
 *
 */
public abstract class AbstractDao {
    Realm realm;

    public void onCreate() {
        this.realm = Realm.getDefaultInstance();
    }

    public void onDestroy() {
        realm.close();
    }
}
