package org.assistant.sigma.model.migrations;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by giovanni on 2/10/17.
 * Adds 'exclude_from_spent_resume' to transaction
 * entity
 */
public class MigrationV2 implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        if (newVersion == 2) {
            schema.get("Transaction").addField("excludeFromSpentResume", boolean.class);
        }
    }
}
