package org.assistant.sigma.utils;

import android.content.Context;

import org.assistant.sigma.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by giovanni on 13/12/17.
 *
 */
public class DateFormatter {

    public String format(int hour, int min) {
        StringBuilder sBuilder = new StringBuilder();

        if (hour < 10) {
            sBuilder.append("0");
        }
        sBuilder.append(hour);
        sBuilder.append(":");

        if (min < 10) {
            sBuilder.append("0");
        }
        sBuilder.append(min);

        return sBuilder.toString();
    }

    public String relativePast(Date date) {
        Calendar calDate = Calendar.getInstance(Locale.getDefault());
        Calendar calNow = Calendar.getInstance(Locale.getDefault());
        calDate.setTime(date);

        return "";
    }

    public String agoMinutes(Context mContext, Calendar date, Calendar now) {
        long diff = now.getTimeInMillis() - date.getTimeInMillis();
        int min = Math.round(diff / 1000 / 60);

        String ago = mContext.getString(R.string.ago);
        StringBuilder sBuilder = new StringBuilder(ago)
                .append(" ")
                .append(min)
                .append(" ");

        if (min == 1) {
            sBuilder.append(mContext.getString(R.string.minute));
        }

        return ago + " " + min + " " + mContext.getString(R.string.minutes);
    }

    public boolean lessThanAWeek(Calendar date, Calendar now) {
//        Calendar.

        return true;
    }
}
