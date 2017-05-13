package org.assistant.sigma.model.entities;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Application's users model
 *
 * Created by giovanni on 4/05/17.
 */
public class User extends RealmObject {

    @PrimaryKey
    private long fbId;
    private String urlPicture;
    private String email;
    private String firstName;
    private String lastName;
    private boolean active;

    private Settings settings;
    private RealmList<Account> accounts;

    public String fullName() {
        StringBuilder stringBuilder = new StringBuilder(this.firstName);
        if (this.lastName != null && this.lastName.length() > 0) {
            stringBuilder.append(" ").append(this.lastName);
        }

        return stringBuilder.toString();
    }


    public long getFbId() {
        return fbId;
    }

    public void setFbId(long fbId) {
        this.fbId = fbId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public RealmList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(RealmList<Account> accounts) {
        this.accounts = accounts;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }
}
