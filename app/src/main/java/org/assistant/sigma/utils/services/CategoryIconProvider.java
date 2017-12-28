package org.assistant.sigma.utils.services;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.assistant.sigma.model.catalogs.DefaultTransactionCategories;
import org.assistant.sigma.model.entities.TransactionCategory;

/**
 * Created by giovanni on 29/05/17.
 * @Deprecated
 */
public class CategoryIconProvider {

    private Context mContext;

    @ColorRes
    private int color;
    private int size = 0;

    private IconDrawable dTransport;
    private IconDrawable dMarket;
    private IconDrawable dClothing;
    private IconDrawable dHome;
    private IconDrawable dRestaurant;
    private IconDrawable dBar;
    private IconDrawable dOther;
    private IconDrawable dWallet;


    public CategoryIconProvider(Context mContext, @ColorRes int color) {
        this.mContext = mContext;
        this.color = color;

        initIcons();
    }

    public CategoryIconProvider(Context mContext, @ColorRes int color, int size) {
        this.mContext = mContext;
        this.color = color;
        this.size = size;

        initIcons();
    }

    public void setIcon(ImageView imageView, TransactionCategory transactionCategory) {

        // Set category icon
        imageView.setImageDrawable(drawableForCategory(transactionCategory));
    }

    public void setCompoundIcon(TextView tvCategory, TransactionCategory category) {
        tvCategory.setCompoundDrawablesWithIntrinsicBounds(
                drawableForCategory(category),
                null,
                null,
                null
        );
    }

    private Drawable drawableForCategory(TransactionCategory category) {
        if (category.getId() == DefaultTransactionCategories.ID_TRANSPORT) {
            return dTransport;
        } else if (category.getId() == DefaultTransactionCategories.ID_PROVISIONS) {
            return dMarket;
        } else if (category.getId() == DefaultTransactionCategories.ID_CLOTHES) {
            return dClothing;
        } else if (category.getId() == DefaultTransactionCategories.ID_HOME) {
            return dHome;
        } else if (category.getId() == DefaultTransactionCategories.ID_RESTAURANT) {
            return dRestaurant;
        } else if (category.getId() == DefaultTransactionCategories.ID_BAR) {
            return dBar;
        } else if (category.getId() == DefaultTransactionCategories.ID_SALARY) {
            return dWallet;
        } else {
            return dOther;
        }
    }

    private void initIcons() {
        dTransport = new IconDrawable(mContext, MaterialIcons.md_directions_bus);
        dMarket = new IconDrawable(mContext, MaterialIcons.md_local_mall);
        dClothing = new IconDrawable(mContext, MaterialIcons.md_local_offer);
        dHome = new IconDrawable(mContext, MaterialIcons.md_home);
        dRestaurant = new IconDrawable(mContext, MaterialIcons.md_restaurant_menu);
        dBar = new IconDrawable(mContext, MaterialIcons.md_local_bar);
        dWallet = new IconDrawable(mContext, MaterialIcons.md_account_balance_wallet);
        dOther = new IconDrawable(mContext, MaterialIcons.md_help_outline);

        tintDrawables();
        if (size > 0) {
            setupSizes();
        }
    }

    private void tintDrawables() {
        dTransport.color(color);
        dMarket.color(color);
        dClothing.color(color);
        dHome.color(color);
        dRestaurant.color(color);
        dBar.color(color);
        dWallet.color(color);
        dOther.color(color);
    }

    private void setupSizes() {
        dTransport.sizeDp(size);
        dMarket.sizeDp(size);
        dClothing.sizeDp(size);
        dHome.sizeDp(size);
        dRestaurant.sizeDp(size);
        dBar.sizeDp(size);
        dWallet.sizeDp(size);
        dOther.sizeDp(size);
    }
}
