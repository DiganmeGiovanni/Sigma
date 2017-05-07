package org.assistant.sigma.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.assistant.sigma.R;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.utils.TextUtils;

import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 6/05/17.
 */
public class TransactionsAdapter extends BaseAdapter {

    private RealmResults<Transaction> transactions;
    private LayoutInflater inflater;
    private Context mContext;

    public TransactionsAdapter(Context mContext, RealmResults<Transaction> transactions) {
        this.mContext = mContext;
        this.transactions = transactions;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return transactions.size();
    }

    @Override
    public Object getItem(int i) {
        return transactions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Transaction transaction = transactions.get(i);

        View containerView = view;
        if (containerView == null) {
            containerView = inflater.inflate(R.layout.item_transaction, viewGroup, false);
        }

        // Account name
        TextView tvAccount = (TextView) containerView.findViewById(R.id.tv_account);
        tvAccount.setText(transaction.getAccount().getName());

        // Category name
        TextView tvCategory = (TextView) containerView.findViewById(R.id.tv_category);
        tvCategory.setText(transaction.getTransactionCategory().getName());

        // Quantity
        TextView tvQuantity = (TextView) containerView.findViewById(R.id.tv_quantity);
        tvQuantity.setText(TextUtils.asMoney(Math.abs(transaction.getQuantity())));
        if (transaction.getQuantity() > 0) {
            tvQuantity.setTextColor(mContext.getResources().getColor(R.color.green));
        } else {
            tvQuantity.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        }

        // Time
        TextView tvTime = (TextView) containerView.findViewById(R.id.tv_time);
        tvTime.setText(android.text.format.DateUtils.getRelativeTimeSpanString(transaction.getCreatedAt().getTime()));

        // Description
        TextView tvDescription = (TextView) containerView.findViewById(R.id.tv_description);
        tvDescription.setText(transaction.getDescription());
        if (transaction.getDescription() != null && transaction.getDescription().length() > 0) {
            tvDescription.setVisibility(View.VISIBLE);
        } else {
            tvDescription.setVisibility(View.GONE);
        }

        return containerView;
    }
}
