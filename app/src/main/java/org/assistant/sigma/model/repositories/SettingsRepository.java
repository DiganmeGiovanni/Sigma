package org.assistant.sigma.model.repositories;

import org.assistant.sigma.model.catalogs.Periods;
import org.assistant.sigma.model.entities.Settings;
import org.assistant.sigma.model.entities.User;

import io.realm.Realm;

/**
 *
 * Created by giovanni on 7/05/17.
 */
public class SettingsRepository {

    private Realm realm = Realm.getDefaultInstance();

    public Settings activeUserSettings() {
        User user = activeUser();
        return user.getSettings();
    }

    /**
     * Set {@code settings} to active user
     * @param settings Updated settings for active user
     */
    public void saveSettings(Settings settings) {
        User user = activeUser();
        if (user != null) {
            settings.setId(1);
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(settings);
            realm.commitTransaction();
        }
    }

    /**
     * Ensure settings existence for active user
     */
    public void createDefaultSettingsForActiveUser() {
        User user = activeUser();
        if (user != null && user.getSettings() == null) {
            realm.beginTransaction();
            Settings settings = realm.createObject(Settings.class, 1);
            settings.setSmallPeriod(Periods.SM_DAILY_GROUPED_WEEKEND);
            settings.setLargePeriod(Periods.LG_FORTNIGHT);
            settings.setStartDayHour(6);
            settings.setIncludeHomeSpentForLimit(false);
            settings.setSpentLimitLarge(800);
            settings.setReminder1(15);
            settings.setReminder2(22);
            user.setSettings(settings);
            realm.commitTransaction();
        }
    }

    private User activeUser() {
        return realm.where(User.class).equalTo("active", true).findFirst();
    }
}
