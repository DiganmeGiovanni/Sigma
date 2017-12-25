package org.assistant.sigma.utils;

import org.assistant.sigma.model.catalogs.Periods;
import org.assistant.sigma.model.entities.Settings;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by giovanni on 25/12/17.
 *
 */
public class TimeUtils {

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
}
