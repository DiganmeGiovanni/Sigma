package org.assistant.sigma.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.assistant.sigma.R;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.utils.TextUtils;
import org.assistant.sigma.utils.services.CategoryIconProvider;

import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 24/05/17.
 */
public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

    private RealmResults<Transaction> transactions;
    private CategoryIconProvider categoryIconProvider;
    private Context mContext;

    private int paddingTopAsPixels16 = 0;

    public TransactionsAdapter(Context mContext, RealmResults<Transaction> transactions) {
        this.mContext = mContext;
        this.transactions = transactions;
        categoryIconProvider = new CategoryIconProvider(mContext, R.color.colorPrimary);

        // Calculate the top 16dp for first item
        float scale = mContext.getResources().getDisplayMetrics().density;
        paddingTopAsPixels16 = (int) (16 * scale + 0.5f);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_transaction,
                parent,
                false
        );

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.tvAccount.setText(transaction.getAccount().getName());
        holder.tvTime.setText(TextUtils.relativeTime(mContext, transaction.getCreatedAt().getTime()));
        holder.tvQuantity.setText(TextUtils.asMoney(Math.abs(transaction.getQuantity())));

        // Set padding top if is first item
        if (position == 0) {
            holder.tvQuantity.setPadding(0, paddingTopAsPixels16, 0, 0);
        } else {
            holder.tvQuantity.setPadding(0, 0, 0, 0);
        }

        categoryIconProvider.setIcon(holder.ivIconCategory, transaction.getTransactionCategory());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIconCategory;
        TextView tvAccount;
        TextView tvTime;
        TextView tvQuantity;

        ViewHolder(View itemView) {
            super(itemView);

            ivIconCategory = (ImageView) itemView.findViewById(R.id.iv_icon_category);
            tvAccount = (TextView) itemView.findViewById(R.id.tv_account);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
        }
    }
}
