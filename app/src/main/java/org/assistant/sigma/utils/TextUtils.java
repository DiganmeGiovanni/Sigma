package org.assistant.sigma.utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility methods to format text
 *
 * Created by giovanni on 6/05/17.
 */
public class TextUtils {

    private static final NumberFormat currencyFormatter = NumberFormat
            .getCurrencyInstance(Locale.getDefault());

    public static String asMoney(double value) {
        return currencyFormatter.format(value);
    }
}
