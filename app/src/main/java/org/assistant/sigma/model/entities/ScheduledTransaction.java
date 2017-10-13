package org.assistant.sigma.model.entities;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by giovanni on 10/10/17.
 *
 */
public class ScheduledTransaction extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private int hourOfDay;
    private int minute;
    private double quantity;
    private String description;
    private boolean excludeFromSpentResume;

    private TransactionCategory transactionCategory;
    private Account account;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isExcludeFromSpentResume() {
        return excludeFromSpentResume;
    }

    public void setExcludeFromSpentResume(boolean excludeFromSpentResume) {
        this.excludeFromSpentResume = excludeFromSpentResume;
    }

    public TransactionCategory getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(TransactionCategory transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
