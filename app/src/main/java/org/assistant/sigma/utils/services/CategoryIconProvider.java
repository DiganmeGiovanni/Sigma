package org.assistant.sigma.utils.services;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.widget.ImageView;

import org.assistant.sigma.R;
import org.assistant.sigma.model.entities.TransactionCategory;
import org.assistant.sigma.utils.DrawableUtils;

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
        dTransport = DrawableUtils.setColorFilter(
                mContext,
                R.drawable.ic_category_transport_2x,
                color
        );
        dMarket = DrawableUtils.setColorFilter(
                mContext,
                R.drawable.ic_local_mall_2x,
                color
        );
        dClothing = DrawableUtils.setColorFilter(
                mContext,
                R.drawable.ic_category_clothes_2x,
                color
        );
        dHome = DrawableUtils.setColorFilter(
                mContext,
                R.drawable.ic_category_home_2x,
                color
        );
        dRestaurant = DrawableUtils.setColorFilter(
                mContext,
                R.drawable.ic_category_restaurant_2x,
                color
        );
        dBar = DrawableUtils.setColorFilter(
                mContext,
                R.drawable.ic_category_bar_2x,
                color
        );
        dWallet = DrawableUtils.setColorFilter(
                mContext,
                R.drawable.ic_wallet_2x,
                color
        );
        dOther = DrawableUtils.setColorFilter(
                mContext,
                R.drawable.ic_category_other_2x,
                color
        );
    }
}
