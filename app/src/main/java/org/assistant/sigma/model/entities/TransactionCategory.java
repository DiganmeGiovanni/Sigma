package org.assistant.sigma.model.entities;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class TransactionCategory extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private boolean incomeCategory;
    private String name;
    private String description;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIncomeCategory() {
        return incomeCategory;
    }

    public void setIncomeCategory(boolean incomeCategory) {
        this.incomeCategory = incomeCategory;
    }
}
