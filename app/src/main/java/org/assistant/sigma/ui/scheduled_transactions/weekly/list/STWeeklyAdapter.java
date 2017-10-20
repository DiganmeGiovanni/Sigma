package org.assistant.sigma.ui.scheduled_transactions.weekly.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.assistant.sigma.R;
import org.assistant.sigma.model.entities.ScheduledTransactionWeekly;
import org.assistant.sigma.utils.TextUtils;
import org.assistant.sigma.utils.services.CategoryIconProvider;

import io.realm.RealmResults;

/**
 * Created by giovanni on 15/10/17.
 *
 */
public class STWeeklyAdapter extends RecyclerView.Adapter<STWeeklyAdapter.ViewHolder> {
    private RealmResults<ScheduledTransactionWeekly> transactions;
    private CategoryIconProvider categoryIconProvider;
    private OnSTWeeklyClickListener onClickListener;

    private Context mContext;

    STWeeklyAdapter(Context mContext, RealmResults<ScheduledTransactionWeekly> transactions) {
        this.mContext = mContext;
        this.transactions = transactions;
        categoryIconProvider = new CategoryIconProvider(mContext, R.color.colorPrimary);
    }

    STWeeklyAdapter(Context mContext, RealmResults<ScheduledTransactionWeekly> transactions,
                    OnSTWeeklyClickListener onClickListener) {
        this(mContext, transactions);
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_transaction_weekly,
                parent,
                false
        );

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ScheduledTransactionWeekly stWeekly = transactions.get(position);
        holder.tvAccount.setText(stWeekly.getScheduledTransaction().getAccount().getName());
        holder.tvQuantity.setText(TextUtils.asMoney(
                Math.abs(stWeekly.getScheduledTransaction().getQuantity())
        ));

        // Show active days
        StringBuilder activeDaysBuilder = new StringBuilder();
        if (stWeekly.isOnSunday()) {
            activeDaysBuilder.append(mContext.getString(R.string.sunday_abbr_md));
        }
        if (stWeekly.isOnMonday()) {
            if (activeDaysBuilder.length() > 0) {
                activeDaysBuilder.append(" | ");
            }
            activeDaysBuilder.append(mContext.getString(R.string.monday_abbr_md));
        }
        if (stWeekly.isOnTuesday()) {
            if (activeDaysBuilder.length() > 0) {
                activeDaysBuilder.append(" | ");
            }
            activeDaysBuilder.append(mContext.getString(R.string.tuesday_abbr_md));
        }
        if (stWeekly.isOnWednesday()) {
            if (activeDaysBuilder.length() > 0) {
                activeDaysBuilder.append(" | ");
            }
            activeDaysBuilder.append(mContext.getString(R.string.wednesday_abbr_md));
        }
        if (stWeekly.isOnThursday()) {
            if (activeDaysBuilder.length() > 0) {
                activeDaysBuilder.append(" | ");
            }
            activeDaysBuilder.append(mContext.getString(R.string.thursday_abbr_md));
        }
        if (stWeekly.isOnFriday()) {
            if (activeDaysBuilder.length() > 0) {
                activeDaysBuilder.append(" | ");
            }
            activeDaysBuilder.append(mContext.getString(R.string.friday_abbr_md));
        }
        if (stWeekly.isOnSaturday()) {
            if (activeDaysBuilder.length() > 0) {
                activeDaysBuilder.append(" | ");
            }
            activeDaysBuilder.append(mContext.getString(R.string.saturday_abbr_md));
        }
        holder.tvDays.setText(activeDaysBuilder.toString());

        categoryIconProvider.setIcon(
                holder.ivIconCategory,
                stWeekly.getScheduledTransaction().getTransactionCategory()
        );

        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onSTWeeklyClicked(stWeekly);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIconCategory;
        TextView tvAccount;
        TextView tvDays;
        TextView tvQuantity;

        ViewHolder(View itemView) {
            super(itemView);

            ivIconCategory = (ImageView) itemView.findViewById(R.id.iv_icon_category);
            tvAccount = (TextView) itemView.findViewById(R.id.tv_account);
            tvDays = (TextView) itemView.findViewById(R.id.tv_days);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
        }
    }

    public interface OnSTWeeklyClickListener {
        void onSTWeeklyClicked(ScheduledTransactionWeekly sTWeekly);
    }
}
