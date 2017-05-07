package org.assistant.sigma.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.assistant.sigma.R;
import org.assistant.sigma.model.entities.TransactionCategory;

import java.util.List;

/**
 *
 * Created by giovanni on 6/05/17.
 */
public class SPTransactionCategoriesAdapter extends BaseAdapter {

    private List<TransactionCategory> categories;
    private LayoutInflater inflater;

    public SPTransactionCategoriesAdapter(Context mContext, List<TransactionCategory> categories) {
        this.categories = categories;
        inflater = LayoutInflater.from(mContext);
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
            containerView = inflater.inflate(R.layout.item_generic_name_description, viewGroup, false);
        }

        TextView tvName = (TextView) containerView.findViewById(R.id.tv_name);
        tvName.setText(category.getName());

        TextView tvDescription = (TextView) containerView.findViewById(R.id.tv_description);
        tvDescription.setText(category.getDescription());

        return containerView;
    }
}
