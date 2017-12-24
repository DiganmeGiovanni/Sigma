package org.assistant.sigma.ui.util;

import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

/**
 * Created by giovanni on 24/12/17.
 *
 */
public class ButtonsUtils {

    public static void setupFAB(FloatingActionButton btn, MaterialIcons icon, int color,
                                View.OnClickListener clickListener) {
        IconDrawable iconDrawable = new IconDrawable(
                btn.getContext(),
                icon
        );
        iconDrawable.colorRes(color);
        iconDrawable.sizeDp(24);
        btn.setImageDrawable(iconDrawable);

        if (clickListener != null) {
            btn.setOnClickListener(clickListener);
        }
    }
}
