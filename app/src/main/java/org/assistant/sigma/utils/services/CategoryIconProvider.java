package org.assistant.sigma.utils.services;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.widget.ImageView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.assistant.sigma.R;
import org.assistant.sigma.model.entities.TransactionCategory;

/**
 *
 * Created by giovanni on 29/05/17.
 */
public class CategoryIconProvider {

    private Context mContext;

    @ColorRes
    private int color;

    private Drawable dTransport;
    private Drawable dMarket;
    private Drawable dClothing;
    private Drawable dHome;
    private Drawable dRestaurant;
    private Drawable dBar;
    private Drawable dOther;
    private Drawable dWallet;


    public CategoryIconProvider(Context mContext, int color) {
        this.mContext = mContext;
        this.color = color;

        tintDrawables();
    }

    public void setIcon(ImageView imageView, TransactionCategory transactionCategory) {

        // Set category icon
        String categoryName = transactionCategory.getName();
        if (categoryName.equals(mContext.getString(R.string.category_name_transport))) {
            imageView.setImageDrawable(dTransport);
        } else if (categoryName.equals(mContext.getString(R.string.category_name_provisions))) {
            imageView.setImageDrawable(dMarket);
        } else if (categoryName.equals(mContext.getString(R.string.category_name_clothes_shoes))) {
            imageView.setImageDrawable(dClothing);
        } else if (categoryName.equals(mContext.getString(R.string.category_name_home))) {
            imageView.setImageDrawable(dHome);
        } else if (categoryName.equals(mContext.getString(R.string.category_name_restaurants))) {
            imageView.setImageDrawable(dRestaurant);
        } else if (categoryName.equals(mContext.getString(R.string.category_name_bar))) {
            imageView.setImageDrawable(dBar);
        } else if (categoryName.equals(mContext.getString(R.string.category_name_salary))) {
            imageView.setImageDrawable(dWallet);
        } else if (categoryName.equals(mContext.getString(R.string.category_name_other))) {
            imageView.setImageDrawable(dOther);
        }
    }

    private void tintDrawables() {
        dTransport = new IconDrawable(mContext, MaterialIcons.md_directions_bus).color(color);
        dMarket = new IconDrawable(mContext, MaterialIcons.md_local_mall).color(color);
        dClothing = new IconDrawable(mContext, MaterialIcons.md_local_offer).color(color);
        dHome = new IconDrawable(mContext, MaterialIcons.md_home).color(color);
        dRestaurant = new IconDrawable(mContext, MaterialIcons.md_restaurant_menu).color(color);
        dBar = new IconDrawable(mContext, MaterialIcons.md_local_bar).color(color);
        dWallet = new IconDrawable(mContext, MaterialIcons.md_account_balance_wallet).color(color);
        dOther = new IconDrawable(mContext, MaterialIcons.md_help_outline).color(color);
    }
}
