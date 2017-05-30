package org.assistant.sigma.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.assistant.sigma.R;
import org.assistant.sigma.model.entities.TransactionCategory;
import org.assistant.sigma.utils.services.CategoryIconProvider;

import java.util.List;

/**
 *
 * Created by giovanni on 6/05/17.
 */
public class TransactionCategoriesAdapter extends BaseAdapter {

    private List<TransactionCategory> categories;
    private LayoutInflater inflater;
    private CategoryIconProvider categoryIconProvider;

    public TransactionCategoriesAdapter(Context mContext, List<TransactionCategory> categories) {
        this.categories = categories;
        inflater = LayoutInflater.from(mContext);
        categoryIconProvider = new CategoryIconProvider(mContext, R.color.colorPrimary);
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
        categoryIconProvider.setIcon(ivIcon, category);

        return containerView;
    }
}
