package org.assistant.sigma.utils;

import android.content.Context;

import org.assistant.sigma.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by giovanni on 13/12/17.
 *
 */
public class DateFormatter {
    private static SimpleDateFormat monthFormatter = new SimpleDateFormat(
            "dd MMM",
            Locale.getDefault()
    );
    private static SimpleDateFormat timeFormatter = new SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
    );

    /**
     * Formats given date as a moment in past examples:
     * - Today at 14:02
     * - Yesterday at 23:50
     * - Before yesterday at 13:15
     * - 15 Nov at 15:18
     *
     * @param mContext Current context to access strings
     * @param date Moment to format
     * @return Human friendly formatted date
     */
    public static String asPastTime(Context mContext, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return getDayPrefix(mContext, calendar) +
                " " +
                formatTime(mContext, calendar);
    }

    /**
     * Formats given date as: hh:mm, eg:
     * - 01:23 AM
     * - 03:23 PM
     * @param calendar Moment to format
     * @return Human friendly time
     */
    public static String asHourMinute(Calendar calendar) {
        return timeFormatter.format(calendar.getTime());
    }

    /**
     * Formats given date as: dd MMM, eg:
     * - 15 Nov
     * - 01 Ene
     *
     * @param date Moment to format
     * @return Human friendly date month string
     */
    public static String asSimpleDateMonth(Date date) {
        return monthFormatter.format(date);
    }

    /**
     * Creates a human friendly string for given day
     * @param date Date which to get prefix
     * @return Today dd MMM, Yesterday dd MMM, Before yesterday dd MMM or simply dd MMM
     */
    private static String getDayPrefix(Context mContext, Calendar date) {
        Calendar calNow = Calendar.getInstance(Locale.getDefault());
        StringBuilder prefixBuilder = new StringBuilder();

        if (calNow.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)) {
             prefixBuilder.append(mContext.getString(R.string.today));
        }

        Calendar yesterday = (Calendar) calNow.clone();
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        if (yesterday.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)) {
             prefixBuilder.append(mContext.getString(R.string.yesterday));
        }

        Calendar beforeYesterday = (Calendar) yesterday.clone();
        beforeYesterday.add(Calendar.DAY_OF_MONTH, -1);
        if (beforeYesterday.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)) {
             prefixBuilder.append(mContext.getString(R.string.before_yesterday));
        }

        // Append space before date if string is not empty
        if (prefixBuilder.length() > 0) {
            prefixBuilder.append(" ");
        }

        prefixBuilder.append(monthFormatter.format(date.getTime()).toUpperCase());
        return prefixBuilder.toString();
    }

    public static String formatTime(Context mContext, Calendar date) {
        return mContext.getString(R.string.to_the) +
                " " +
                timeFormatter.format(date.getTime());
    }
}
