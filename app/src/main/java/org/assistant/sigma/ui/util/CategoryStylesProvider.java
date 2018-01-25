package org.assistant.sigma.ui.util;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;

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
}
