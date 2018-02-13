package org.assistant.sigma.ui.util;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.assistant.sigma.R;
import org.assistant.sigma.model.catalogs.DefaultTransactionCategories;
import org.assistant.sigma.model.entities.TransactionCategory;

/**
 * Created by giovanni on 27/12/17.
 *
 */
public class CategoryStylesProvider {

    public static IconDrawable makeCategoryIcon(Context mContext, TransactionCategory category) {
        return makeCategoryIcon(mContext, category, getCategoryColor(mContext, category));
    }

    public static IconDrawable makeCategoryIcon(Context mContext, TransactionCategory category,
                                                @ColorInt int color) {
        IconDrawable iconDrawable;
        switch (category.getId()) {
            case DefaultTransactionCategories.ID_TRANSPORT:
                iconDrawable = new IconDrawable(mContext, MaterialIcons.md_directions_bus);
                break;
            case DefaultTransactionCategories.ID_PROVISIONS:
                iconDrawable = new IconDrawable(mContext, MaterialIcons.md_local_mall);
                break;
            case DefaultTransactionCategories.ID_CLOTHES:
                iconDrawable = new IconDrawable(mContext, MaterialIcons.md_local_offer);
                break;
            case DefaultTransactionCategories.ID_HOME:
                iconDrawable = new IconDrawable(mContext, MaterialIcons.md_home);
                break;
            case DefaultTransactionCategories.ID_RESTAURANT:
                iconDrawable = new IconDrawable(mContext, MaterialIcons.md_restaurant_menu);
                break;
            case DefaultTransactionCategories.ID_BAR:
                iconDrawable = new IconDrawable(mContext, MaterialIcons.md_local_bar);
                break;
            case DefaultTransactionCategories.ID_SALARY:
                iconDrawable = new IconDrawable(mContext, MaterialIcons.md_account_balance_wallet);
                break;
            case DefaultTransactionCategories.ID_OTHER_SPENT:
            default:
                iconDrawable = new IconDrawable(mContext, MaterialIcons.md_help_outline);
                break;
        }

        iconDrawable.color(color);
        return iconDrawable;
    }

    public static int getCategoryColor(Context mContext, TransactionCategory category) {
        return getCategoryColor(mContext, category.getId());
    }

    public static int getCategoryColor(Context mContext, int categoryId) {
        int colorId;
        switch (categoryId) {
            case DefaultTransactionCategories.ID_PROVISIONS:
                colorId = R.color.colorCategoryProvisions;
                break;
            case DefaultTransactionCategories.ID_BAR:
                colorId = R.color.colorCategoryBar;
                break;
            case DefaultTransactionCategories.ID_HOME:
                colorId = R.color.colorCategoryHome;
                break;
            case DefaultTransactionCategories.ID_RESTAURANT:
                colorId = R.color.colorCategoryRestaurant;
                break;
            case DefaultTransactionCategories.ID_CLOTHES:
                colorId = R.color.colorCategoryClothes;
                break;
            case DefaultTransactionCategories.ID_TRANSPORT:
                colorId = R.color.colorCategoryTransport;
                break;
            case DefaultTransactionCategories.ID_OTHER_SPENT:
                colorId = R.color.colorCategoryOther;
                break;
            default:
                colorId = R.color.colorCategoryProvisions;
                break;
        }

        return ContextCompat.getColor(
                mContext,
                colorId
        );
    }

    public static TextView makeCatPickerItem(final Context mContext,
                                             final TransactionCategory category,
                                             final View.OnClickListener onClickListener) {
        int dp10 = (int) mContext.getResources().getDimension(R.dimen.dp10);

        final Drawable drawable = ContextCompat
                .getDrawable(mContext, R.drawable.background_category);
        final int grayColor = ContextCompat.getColor(mContext, R.color.gray);
        final int catColor = getCategoryColor(mContext, category);

        final TextView textView = new TextView(mContext);
        textView.setBackground(drawable);
        textView.setPadding(dp10, dp10, dp10, dp10);
        textView.setText(category.getName());
        textView.setTextColor(grayColor);

        // Set category icon
        textView.setCompoundDrawablePadding(8);
        final IconDrawable icon = makeCategoryIcon(mContext, category, grayColor);
        icon.sizeDp(16);
        textView.setCompoundDrawablesWithIntrinsicBounds(
                icon,
                null,
                null,
                null
        );

        // Setup click listener
        if (onClickListener != null) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final GradientDrawable background = (GradientDrawable) view.getBackground();

                    ObjectAnimator textColorAnimation = ObjectAnimator.ofObject(
                            textView,
                            "textColor",
                            new ArgbEvaluator(),
                            grayColor,
                            catColor
                    );
                    textColorAnimation.start();

                    ValueAnimator icColorAnimation = ValueAnimator.ofObject(
                            new ArgbEvaluator(),
                            grayColor,
                            catColor
                    );
                    icColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            icon.color((Integer) valueAnimator.getAnimatedValue());
                            background.setStroke(3, (int) valueAnimator.getAnimatedValue());
                        }
                    });
                    icColorAnimation.start();
                    onClickListener.onClick(view);
                }
            });
        }
        return textView;
    }

    public static void unSelectCatPickerItem(final TextView textView) {
        final GradientDrawable background = (GradientDrawable) textView.getBackground();
        final IconDrawable icon = (IconDrawable) textView.getCompoundDrawables()[0];
        final int currentTextColor = textView.getCurrentTextColor();
        final int grayColor = ContextCompat.getColor(textView.getContext(), R.color.gray);

        ValueAnimator icColorAnimation = ValueAnimator.ofObject(
                new ArgbEvaluator(),
                currentTextColor,
                grayColor
        );
        icColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                icon.color((Integer) valueAnimator.getAnimatedValue());
                textView.setTextColor((Integer) valueAnimator.getAnimatedValue());
                background.setStroke(3, grayColor);
            }
        });
        icColorAnimation.start();
    }
}
