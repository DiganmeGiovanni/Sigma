package org.assistant.sigma.accounts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.assistant.sigma.R;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.repositories.AccountsRepository;
import org.assistant.sigma.utils.TextUtils;

import io.realm.RealmList;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class AccountsAdapter extends BaseAdapter {

    private AccountsRepository accountsRepository;
    private RealmList<Account> accounts;
    private LayoutInflater inflater;

    public AccountsAdapter(Context mContext, RealmList<Account> accounts) {
        this.accounts = accounts;
        inflater = LayoutInflater.from(mContext);

        accountsRepository = new AccountsRepository();
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
        TextView tvName = (TextView) contentView.findViewById(R.id.tv_name);
        tvName.setText(account.getName());

        // Set card digits
        TextView tvCardDigits = (TextView) contentView.findViewById(R.id.tv_last_card_digits);
        tvCardDigits.setText(account.lastCardDigits());

        // Set current account balance
        TextView tvBalance = (TextView) contentView.findViewById(R.id.tv_balance);
        tvBalance.setText(TextUtils.asMoney(accountsRepository.currentBalance(account)));

        return contentView;
    }
}
