package org.assistant.sigma.utils;

import android.support.annotation.StringRes;

import org.assistant.sigma.R;
import org.assistant.sigma.model.catalogs.Periods;
import org.assistant.sigma.model.entities.Settings;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by giovanni on 25/12/17.
 *
 */
public class PeriodUtils {

    public static Date getCurrentLargePeriodStart(Settings settings) {
        Calendar startDate = Calendar.getInstance();
        int TODAY = startDate.get(Calendar.DAY_OF_WEEK);
        int NOW_HOUR = startDate.get(Calendar.HOUR_OF_DAY);
        int START_HOUR = settings.getStartDayHour();

        switch (settings.getLargePeriod()) {
            case Periods.LG_WEEKLY:
                if (TODAY == Calendar.MONDAY && NOW_HOUR < START_HOUR) {
                    startDate.set(Calendar.WEEK_OF_YEAR, startDate.get(Calendar.WEEK_OF_YEAR) - 1);
                } else {
                    startDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                }
                break;

            case Periods.LG_FORTNIGHT:
                if (startDate.get(Calendar.DAY_OF_MONTH) == 15 && NOW_HOUR < START_HOUR) {
                    startDate.set(Calendar.DAY_OF_MONTH, 1);
                }
                else if (startDate.get(Calendar.DAY_OF_MONTH) >= 15) {
                    startDate.set(Calendar.DAY_OF_MONTH, 15);
                } else if (startDate.get(Calendar.DAY_OF_MONTH) == 1 & NOW_HOUR < START_HOUR) {
                    startDate.set(Calendar.DAY_OF_MONTH, 15);
                } else {
                    startDate.set(Calendar.DAY_OF_MONTH, 1);
                }
                break;

            case Periods.LG_MONTHLY:
                if (startDate.get(Calendar.DAY_OF_MONTH) == 1 && NOW_HOUR < START_HOUR) {
                    startDate.set(Calendar.MONTH, startDate.get(Calendar.MONTH) - 1);
                }

                startDate.set(Calendar.DAY_OF_MONTH, 1);
                break;
        }

        startDate.set(Calendar.HOUR_OF_DAY, START_HOUR);
        return startDate.getTime();
    }

    @StringRes
    public static int getCurrentLargePeriodLabel(Settings settings) {
        switch (settings.getLargePeriod()) {
            case Periods.LG_MONTHLY:
                return R.string.this_month;
            case Periods.LG_FORTNIGHT:
                return R.string.this_fortnight;
            case Periods.LG_WEEKLY:
            default:
                return R.string.this_week;
        }
    }

    public static Date getCurrentShortPeriodStart(Settings settings) {
        Calendar startDate = Calendar.getInstance();
        int TODAY = startDate.get(Calendar.DAY_OF_WEEK);
        int NOW_HOUR = startDate.get(Calendar.HOUR_OF_DAY);
        int START_HOUR = settings.getStartDayHour();

        switch (settings.getSmallPeriod()) {
            case Periods.SM_DAILY:
                if (NOW_HOUR < START_HOUR) {
                    startDate.set(Calendar.DAY_OF_MONTH, startDate.get(Calendar.DAY_OF_MONTH) - 1);
                }
                break;

            case Periods.SM_DAILY_GROUPED_WEEKEND:
                if (TODAY == Calendar.SATURDAY || TODAY == Calendar.SUNDAY) {
                    startDate.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                } else if (TODAY == Calendar.MONDAY && NOW_HOUR < START_HOUR) {
                    startDate.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                } else if (NOW_HOUR < START_HOUR) {
                    startDate.set(Calendar.DAY_OF_MONTH, startDate.get(Calendar.DAY_OF_MONTH) - 1);
                }
                break;
        }

        startDate.set(Calendar.HOUR_OF_DAY, START_HOUR);
        return startDate.getTime();
    }

    @StringRes
    public static int getCurrentShortPeriodLabel(Settings settings) {
        switch (settings.getSmallPeriod()) {
            case Periods.SM_DAILY_GROUPED_WEEKEND:
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(getCurrentShortPeriodStart(settings));
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                    return R.string.spent_this_weekend;
                } else {
                    return R.string.spent_today;
                }
            case Periods.SM_DAILY:
            default:
                return R.string.spent_today;
        }
    }
}
