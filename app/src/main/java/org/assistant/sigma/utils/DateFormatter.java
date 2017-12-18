package org.assistant.sigma.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by giovanni on 13/12/17.
 *
 */
public class DateFormatter {

    public String relativePast(Date date) {
        Calendar calDate = Calendar.getInstance(Locale.getDefault());
        Calendar calNow = Calendar.getInstance(Locale.getDefault());
        calDate.setTime(date);

        return "";
    }

    public boolean lessThanAWeek(Calendar date, Calendar now) {
//        Calendar.

        return true;
    }
}
