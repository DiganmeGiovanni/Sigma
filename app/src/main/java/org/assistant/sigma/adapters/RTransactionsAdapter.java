package org.assistant.sigma.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.assistant.sigma.R;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.utils.TextUtils;

import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 24/05/17.
 */
public class RTransactionsAdapter extends RecyclerView.Adapter<RTransactionsAdapter.ViewHolder> {

    private RealmResults<Transaction> transactions;

    public RTransactionsAdapter(RealmResults<Transaction> transactions) {
        this.transactions = transactions;
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
        holder.tvCategory.setText(transaction.getTransactionCategory().getName());
        holder.tvQuantity.setText(TextUtils.asMoney(transaction.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAccount;
        TextView tvCategory;
        TextView tvQuantity;

        public ViewHolder(View itemView) {
            super(itemView);

            tvAccount = (TextView) itemView.findViewById(R.id.tv_account);
            tvCategory = (TextView) itemView.findViewById(R.id.tv_category);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
        }
    }
}
