package org.assistant.sigma.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

/**
 *
 * Created by giovanni on 13/05/17.
 */
public class DrawableUtils {

    public static Drawable setColorFilter(Context mContext, @DrawableRes int drawableId,
                                      @ColorRes int color) {
        Drawable drawable = ContextCompat.getDrawable(mContext, drawableId);
        drawable.mutate();

        drawable.setColorFilter(new PorterDuffColorFilter(
                mContext.getResources().getColor(color),
                PorterDuff.Mode.SRC_ATOP
        ));
        return drawable;
    }
}
