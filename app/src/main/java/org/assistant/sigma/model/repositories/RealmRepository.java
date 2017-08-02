package org.assistant.sigma.model.repositories;

import io.realm.Realm;

/**
 * The base repository for all realm data sources,
 * Use method {@link #onDestroy()} to close realms
 *
 * Created by giovanni on 23/07/17.
 */
class RealmRepository {

    protected Realm realm = Realm.getDefaultInstance();

    public void onDestroy() {
        realm.close();
    }
}
