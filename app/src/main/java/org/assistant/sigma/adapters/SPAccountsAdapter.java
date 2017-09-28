package org.assistant.sigma.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.assistant.sigma.R;
import org.assistant.sigma.model.entities.Account;

import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 6/05/17.
 */
public class SPAccountsAdapter extends BaseAdapter {

    private RealmResults<Account> accounts;
    private LayoutInflater inflater;

    public SPAccountsAdapter(Context mContext, RealmResults<Account> accounts) {
        this.accounts = accounts;
        this.inflater = LayoutInflater.from(mContext);
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

        View containerView = view;
        if (containerView == null) {
            containerView = inflater.inflate(R.layout.item_generic_name_only, viewGroup, false);
        }

        TextView tvName = (TextView) containerView.findViewById(R.id.tv_name);
        tvName.setText(account.getName());

        return containerView;
    }

    public int getPosition(Account account) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId().equals(account.getId())) {
                return i;
            }
        }

        return -1;
    }
}
