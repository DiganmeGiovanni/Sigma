package org.assistant.sigma.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.assistant.sigma.R;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.utils.DrawableUtils;
import org.assistant.sigma.utils.TextUtils;

import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 24/05/17.
 */
public class RTransactionsAdapter extends RecyclerView.Adapter<RTransactionsAdapter.ViewHolder> {

    private Context mContext;
    private RealmResults<Transaction> transactions;

    private Drawable dTransport;
    private Drawable dMarket;
    private Drawable dClothes;
    private Drawable dHome;
    private Drawable dRestaurant;

    public RTransactionsAdapter(Context mContext, RealmResults<Transaction> transactions) {
        this.transactions = transactions;
        this.mContext = mContext;

        prepareDrawables();
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
        holder.tvQuantity.setText(TextUtils.asMoney(Math.abs(transaction.getQuantity())));

        // Set category icon
        String categoryName = transaction.getTransactionCategory().getName();
        if (categoryName.equals(mContext.getString(R.string.category_name_transport))) {
            holder.ivIconCategory.setImageDrawable(dTransport);
        } else if (categoryName.equals(mContext.getString(R.string.category_name_provisions))) {
            holder.ivIconCategory.setImageDrawable(dMarket);
        } else if (categoryName.equals(mContext.getString(R.string.category_name_clothes_shoes))) {
            holder.ivIconCategory.setImageDrawable(dClothes);
        } else if (categoryName.equals(mContext.getString(R.string.category_name_home))) {
            holder.ivIconCategory.setImageDrawable(dHome);
        } else if (categoryName.equals(mContext.getString(R.string.category_name_restaurants))) {
            holder.ivIconCategory.setImageDrawable(dRestaurant);
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
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

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIconCategory;
        TextView tvAccount;
        TextView tvCategory;
        TextView tvQuantity;

        ViewHolder(View itemView) {
            super(itemView);

            ivIconCategory = (ImageView) itemView.findViewById(R.id.iv_icon_category);
            tvAccount = (TextView) itemView.findViewById(R.id.tv_account);
            tvCategory = (TextView) itemView.findViewById(R.id.tv_category);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
        }
    }
}
