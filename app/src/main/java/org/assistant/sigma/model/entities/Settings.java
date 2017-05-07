package org.assistant.sigma.model.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 *
 * Created by giovanni on 7/05/17.
 */
public class Settings extends RealmObject {

    @PrimaryKey
    private int id;
    private int smallPeriod;
    private int largePeriod;
    private int startDayHour;
    private double spentLimitLarge;
    private boolean includeHomeSpentForLimit;

    private int reminder1;
    private int reminder2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSmallPeriod() {
        return smallPeriod;
    }

    public void setSmallPeriod(int smallPeriod) {
        this.smallPeriod = smallPeriod;
    }

    public int getLargePeriod() {
        return largePeriod;
    }

    public void setLargePeriod(int largePeriod) {
        this.largePeriod = largePeriod;
    }

    public int getStartDayHour() {
        return startDayHour;
    }

    public void setStartDayHour(int startDayHour) {
        this.startDayHour = startDayHour;
    }

    public double getSpentLimitLarge() {
        return spentLimitLarge;
    }

    public void setSpentLimitLarge(double spentLimitLarge) {
        this.spentLimitLarge = spentLimitLarge;
    }

    public int getReminder1() {
        return reminder1;
    }

    public void setReminder1(int reminder1) {
        this.reminder1 = reminder1;
    }

    public int getReminder2() {
        return reminder2;
    }

    public void setReminder2(int reminder2) {
        this.reminder2 = reminder2;
    }

    public boolean isIncludeHomeSpentForLimit() {
        return includeHomeSpentForLimit;
    }

    public void setIncludeHomeSpentForLimit(boolean includeHomeSpentForLimit) {
        this.includeHomeSpentForLimit = includeHomeSpentForLimit;
    }
}
