package org.assistant.sigma.ui.accounts.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.assistant.sigma.R;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.utils.TextUtils;

import io.realm.RealmList;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class AccountsAdapter extends BaseAdapter {
    private AccountsListPresenter mPresenter;
    private RealmList<Account> accounts;
    private LayoutInflater inflater;

    AccountsAdapter(Context mContext, RealmList<Account> accounts,
                    AccountsListPresenter mPresenter) {
        this.mPresenter = mPresenter;
        this.accounts = accounts;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Object getItem(int i) {
        return accounts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Account account = accounts.get(i);
        View contentView = view;

        if (contentView == null) {
            contentView = inflater.inflate(R.layout.item_account, viewGroup, false);
        }

        // Set name
        TextView tvName = contentView.findViewById(R.id.tv_name);
        tvName.setText(account.getName());

        // Set card digits
        TextView tvCardDigits = contentView.findViewById(R.id.tv_last_card_digits);
        tvCardDigits.setText(account.lastCardDigits());

        // Set current account balance
        TextView tvBalance = contentView.findViewById(R.id.tv_balance);
        tvBalance.setText(TextUtils.asMoney(mPresenter.currentBalance(account.getId())));

        return contentView;
    }
}
