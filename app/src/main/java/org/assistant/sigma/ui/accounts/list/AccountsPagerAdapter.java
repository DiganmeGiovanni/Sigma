package org.assistant.sigma.ui.accounts.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.ui.accounts.detail.FragAccountDetail;

import io.realm.RealmResults;

/**
 * Created by giovanni on 29/01/18.
 *
 */
public class AccountsPagerAdapter extends FragmentPagerAdapter {
    private RealmResults<Account> accounts;

    public AccountsPagerAdapter(FragmentManager fm, RealmResults<Account> accounts) {
        super(fm);
        this.accounts = accounts;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arguments = new Bundle();
        arguments.putString(
                FragAccountDetail.ACCOUNT_ID,
                accounts.get(position).getId()
        );

        FragAccountDetail mFragment = new FragAccountDetail();
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
