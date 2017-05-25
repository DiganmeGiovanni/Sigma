package org.assistant.sigma.utils;

import android.text.format.DateUtils;

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
