package org.assistant.sigma.model.entities;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by giovanni on 11/10/17.
 *
 */
public class ScheduledTransactionMonthly extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private int dayOfMonth;
    private ScheduledTransaction scheduledTransaction;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public ScheduledTransaction getScheduledTransaction() {
        return scheduledTransaction;
    }

    public void setScheduledTransaction(ScheduledTransaction scheduledTransaction) {
        this.scheduledTransaction = scheduledTransaction;
    }
}
