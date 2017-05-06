package org.assistant.sigma.model.entities;

import java.util.Date;

import io.realm.RealmObject;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class Transaction extends RealmObject {

    private Date createdAt;
    private double quantity;
    private String description;

    private TransactionCategory transactionCategory;
    private Account account;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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
