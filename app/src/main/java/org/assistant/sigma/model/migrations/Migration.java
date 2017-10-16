package org.assistant.sigma.model.migrations;

import android.util.Log;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by giovanni on 12/10/17.
 * Adds tables for scheduled transactions
 */
public class Migration implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        Log.e(getClass().getName(), "OLD Version is: " + oldVersion);

        // if coming from version 0
        if (oldVersion == 0) {
            schema.get("Transaction").addField("excludeFromSpentResume", boolean.class);
            oldVersion++;
        }

        // If coming from version 2
        if (oldVersion < 4) {
            schema.create("ScheduledTransaction")
                    .addField("id", String.class, FieldAttribute.PRIMARY_KEY)
                    .addField("createdAt", Date.class)
                    .addField("hourOfDay", int.class)
                    .addField("minute", int.class)
                    .addField("quantity", double.class)
                    .addField("description", String.class)
                    .addField("excludeFromSpentResume", boolean.class)
                    .addRealmObjectField("transactionCategory", schema.get("TransactionCategory"))
                    .addRealmObjectField("account", schema.get("Account"));

            schema.create("ScheduledTransactionWeekly")
                    .addField("id", String.class, FieldAttribute.PRIMARY_KEY)
                    .addField("onSunday", boolean.class)
                    .addField("onMonday", boolean.class)
                    .addField("onWednesday", boolean.class)
                    .addField("onThursday", boolean.class)
                    .addField("onFriday", boolean.class)
                    .addField("onSaturday", boolean.class)
                    .addRealmObjectField("scheduledTransaction", schema.get("ScheduledTransaction"));

            schema.create("ScheduledTransactionMonthly")
                    .addField("id", String.class, FieldAttribute.PRIMARY_KEY)
                    .addField("dayOfMonth", int.class)
                    .addRealmObjectField("scheduledTransaction", schema.get("ScheduledTransaction"));

            oldVersion = 4;
        }

        // By error tuesday was ignored on migration to v4 (Add this field)
        if (oldVersion == 4) {
            schema.get("ScheduledTransactionWeekly")
                    .addField("onTuesday", boolean.class);

            oldVersion++;
        }
    }
}
