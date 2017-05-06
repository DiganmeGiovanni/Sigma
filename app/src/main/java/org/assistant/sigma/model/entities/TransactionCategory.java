package org.assistant.sigma.model.entities;

import io.realm.RealmObject;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class TransactionCategory extends RealmObject {

    private String name;
    private String description;

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
}
