package org.assistant.sigma.utils;

import android.content.Context;
import android.text.format.DateUtils;

import org.assistant.sigma.R;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Utility methods to format text
 *
 * Created by giovanni on 6/05/17.
 */
public class TextUtils {

    private enum TIME_UNIT {
        SECS,
        MINUTES,
        HOURS,
        DAYS
    }

    private static final NumberFormat currencyFormatter = NumberFormat
            .getCurrencyInstance(Locale.getDefault());

    private static final SimpleDateFormat humanDateFormatter = new SimpleDateFormat(
            "dd MMMM yyyy hh:mm a",
            Locale.getDefault()
    );

    private static final SimpleDateFormat humanHourFormatter = new SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
    );



    public static String asMoney(double value) {
        return currencyFormatter.format(value);
    }

    public static String forHumans(Calendar calendar, boolean hoursOnly) {
        if (hoursOnly) {
            return humanHourFormatter.format(calendar.getTime());
        } else {
            return forHumans(calendar);
        }
    }

    public static String forHumans(Date date) {
        return humanDateFormatter.format(date);
    }

    public static String forHumans(Calendar calendar) {
        return forHumans(calendar.getTime());
    }

    public static String relativeTime(Context mContext, long time) {
        long now = System.currentTimeMillis();
        long diff = Math.abs(now - time);
        long secs = diff/1000;

        if (secs < 50) {
            return relativeMessage(mContext, (int) secs, TIME_UNIT.SECS);
        } else {
            long minutes = secs / 60;
            if (minutes < 55) {
                if (minutes < 0) minutes = 1;
                return relativeMessage(mContext, (int) minutes, TIME_UNIT.MINUTES);
            } else {
                long hours = minutes / 60;
                if (hours < 22) {
                    if (hours < 1) hours = 1;
                    return relativeMessage(mContext, (int) hours, TIME_UNIT.HOURS);
                } else {
                    long days = hours / 24;
                    if (days < 1) days = 1;
                    return relativeMessage(mContext, (int) days, TIME_UNIT.DAYS);
                }
            }
        }
    }

    private static String relativeMessage(Context mContext, int quantity, TIME_UNIT timeUnit) {
        switch (timeUnit) {
            case SECS:
                return mContext.getString(R.string.just_now);
            case MINUTES:
                if (quantity == 1) {
                    return mContext.getString(R.string.relative_time_past_prefix) +
                            " " + quantity + " " + mContext.getString(R.string.time_abbr_min);
                } else {
                    return mContext.getString(R.string.relative_time_past_prefix) +
                            " " + quantity + " " + mContext.getString(R.string.time_abbr_mins);
                }
            case HOURS:
                if (quantity == 1) {
                    return mContext.getString(R.string.relative_time_past_prefix) +
                            " " + quantity + " " + mContext.getString(R.string.time_abbr_hour);
                } else {
                    return mContext.getString(R.string.relative_time_past_prefix) +
                            " " + quantity + " " + mContext.getString(R.string.time_abbr_hours);
                }
            case DAYS:
                if (quantity == 1) {
                    return mContext.getString(R.string.relative_time_past_prefix) +
                            " " + quantity + " " + mContext.getString(R.string.time_abbr_day);
                } else {
                    return mContext.getString(R.string.relative_time_past_prefix) +
                            " " + quantity + " " + mContext.getString(R.string.time_abbr_days);

                }
            default:
                return "";
        }
    }

    public static String relative(Date date, boolean abbrevAll) {
        return DateUtils.getRelativeTimeSpanString(
                date.getTime(),
                new Date().getTime(),
                1000,
                DateUtils.FORMAT_ABBREV_ALL
        ).toString();
    }

    public static String relative(Calendar calendar, boolean abbrevAll) {
        return relative(calendar.getTime(), abbrevAll);
    }
}
