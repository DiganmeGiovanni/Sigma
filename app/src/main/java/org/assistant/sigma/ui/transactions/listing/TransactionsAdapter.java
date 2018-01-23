package org.assistant.sigma.ui.transactions.listing;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;

import org.assistant.sigma.R;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.model.entities.TransactionCategory;
import org.assistant.sigma.ui.util.CategoryStylesProvider;
import org.assistant.sigma.utils.DateFormatter;
import org.assistant.sigma.utils.TextUtils;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 24/05/17.
 */
public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {
    private RealmResults<Transaction> transactions;
    private Context mContext;
    private OnTransactionClickListener onClickListener;

    private Map<Integer, IconDrawable> iconsCache = new HashMap<>();

    TransactionsAdapter(Context mContext, RealmResults<Transaction> transactions) {
        this.mContext = mContext;
        this.transactions = transactions;
    }

    public TransactionsAdapter(Context mContext, RealmResults<Transaction> transactions,
                               OnTransactionClickListener onClickListener) {
        this(mContext, transactions);
        this.onClickListener = onClickListener;
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
        final Transaction transaction = transactions.get(position);
        holder.tvTitle.setText(transaction.getTransactionCategory().getName());
        holder.tvSubtitle.setText(DateFormatter.asPastTime(mContext, transaction.getCreatedAt()));
        holder.ivIconCategory.setImageDrawable(getCategoryIcon(transaction.getTransactionCategory()));

        if (transaction.getQuantity() < 0) {
            holder.tvQuantity.setTextColor(ContextCompat.getColor(mContext, R.color.red));
        } else {
            holder.tvQuantity.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        }
        holder.tvQuantity.setText(TextUtils.asMoney(Math.abs(transaction.getQuantity())));

        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onTransactionClicked(transaction);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    private IconDrawable getCategoryIcon(TransactionCategory category) {
        if (iconsCache.containsKey(category.getId())) {
            return iconsCache.get(category.getId());
        }

        IconDrawable icon = CategoryStylesProvider.makeCategoryIcon(mContext, category);
        iconsCache.put(category.getId(), icon);
        return icon;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIconCategory;
        TextView tvTitle;
        TextView tvSubtitle;
        TextView tvQuantity;

        ViewHolder(View itemView) {
            super(itemView);

            ivIconCategory = itemView.findViewById(R.id.iv_icon_category);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSubtitle = itemView.findViewById(R.id.tv_subtitle);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
        }
    }

    public interface OnTransactionClickListener {
        void onTransactionClicked(Transaction transaction);
    }
}
