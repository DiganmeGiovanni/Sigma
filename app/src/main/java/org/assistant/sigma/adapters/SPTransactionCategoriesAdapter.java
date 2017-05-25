package org.assistant.sigma.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.assistant.sigma.R;
import org.assistant.sigma.model.entities.TransactionCategory;
import org.assistant.sigma.utils.DrawableUtils;

import java.util.List;

/**
 *
 * Created by giovanni on 6/05/17.
 */
public class SPTransactionCategoriesAdapter extends BaseAdapter {

    private List<TransactionCategory> categories;
    private Context mContext;
    private LayoutInflater inflater;

    private Drawable dTransport;
    private Drawable dMarket;
    private Drawable dClothes;
    private Drawable dHome;
    private Drawable dRestaurant;

    public SPTransactionCategoriesAdapter(Context mContext, List<TransactionCategory> categories) {
        this.categories = categories;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);

        prepareDrawables();
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TransactionCategory category = categories.get(i);

        View containerView = view;
        if (view == null) {
            containerView = inflater.inflate(R.layout.item_generic_ic_name_desc, viewGroup, false);
        }

        TextView tvName = (TextView) containerView.findViewById(R.id.tv_name);
        tvName.setText(category.getName());

        TextView tvDescription = (TextView) containerView.findViewById(R.id.tv_description);
        tvDescription.setText(category.getDescription());

        // Category icon
        ImageView ivIcon = (ImageView) containerView.findViewById(R.id.iv_icon);
        String categoryName = category.getName();
        if (categoryName.equals(mContext.getString(R.string.category_name_transport))) {
            ivIcon.setImageDrawable(dTransport);
        } else if (categoryName.equals(mContext.getString(R.string.category_name_provisions))) {
            ivIcon.setImageDrawable(dMarket);
        } else if (categoryName.equals(mContext.getString(R.string.category_name_clothes_shoes))) {
            ivIcon.setImageDrawable(dClothes);
        } else if (categoryName.equals(mContext.getString(R.string.category_name_home))) {
            ivIcon.setImageDrawable(dHome);
        } else if (categoryName.equals(mContext.getString(R.string.category_name_restaurants))) {
            ivIcon.setImageDrawable(dRestaurant);
        }

        return containerView;
    }

    private void prepareDrawables() {
        dTransport = DrawableUtils.setColorFilter(
                mContext,
                R.drawable.ic_category_transport_2x,
                R.color.colorPrimary
        );
        dMarket = DrawableUtils.setColorFilter(
                mContext,
                R.drawable.ic_category_market_2x,
                R.color.colorPrimary
        );
        dClothes = DrawableUtils.setColorFilter(
                mContext,
                R.drawable.ic_category_clothes_2x,
                R.color.colorPrimary
        );
        dHome = DrawableUtils.setColorFilter(
                mContext,
                R.drawable.ic_category_home_2x,
                R.color.colorPrimary
        );
        dRestaurant = DrawableUtils.setColorFilter(
                mContext,
                R.drawable.ic_category_restaurant_2x,
                R.color.colorPrimary
        );
    }
}
