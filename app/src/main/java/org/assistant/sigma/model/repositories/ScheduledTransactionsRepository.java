package org.assistant.sigma.model.repositories;

import org.assistant.sigma.model.entities.ScheduledTransactionMonthly;
import org.assistant.sigma.model.entities.ScheduledTransactionWeekly;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by giovanni on 15/10/17.
 *
 */
public class ScheduledTransactionsRepository {
    private Realm realm;

    public ScheduledTransactionsRepository() {
        realm = Realm.getDefaultInstance();
    }

    public void destroy() {
        realm.close();
    }

    public RealmResults<ScheduledTransactionWeekly> allWeeklyST() {
        return realm
                .where(ScheduledTransactionWeekly.class)
                .findAllSorted("scheduledTransaction.createdAt", Sort.DESCENDING);
    }

    public RealmResults<ScheduledTransactionMonthly> allMonthlyST() {
        return realm
                .where(ScheduledTransactionMonthly.class)
                .findAllSorted("scheduledTransaction.createdAt", Sort.DESCENDING);
    }

    public void upsert(ScheduledTransactionWeekly stWeekly) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(stWeekly);
        realm.commitTransaction();
    }

    public ScheduledTransactionWeekly find(String stWeeklyId) {
        return realm.where(ScheduledTransactionWeekly.class)
                .equalTo("id", stWeeklyId)
                .findFirst();
    }
}
