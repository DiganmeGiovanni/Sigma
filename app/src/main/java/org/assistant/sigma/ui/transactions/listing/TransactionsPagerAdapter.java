package org.assistant.sigma.ui.transactions.listing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.assistant.sigma.model.entities.Account;

import io.realm.RealmResults;

/**
 * Created by giovanni on 24/12/17.
 *
 */
public class TransactionsPagerAdapter extends FragmentPagerAdapter {
    private RealmResults<Account> accounts;

    TransactionsPagerAdapter(FragmentManager fm, RealmResults<Account> accounts) {
        super(fm);
        this.accounts = accounts;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arguments = new Bundle();
        arguments.putString(
                FragTransactionsList.ACCOUNT_ID,
                accounts.get(position).getId()
        );

        FragTransactionsList mFragment = new FragTransactionsList();
        mFragment.setArguments(arguments);
        return mFragment;
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return accounts.get(position).getName();
    }
}
