package org.assistant.sigma.ui.adapters.scheduledtransactions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.assistant.sigma.ui.adapters.FragPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giovanni on 12/10/17.
 * @Deprecated Use {@link FragPagerAdapter} instead
 */
public class ScheduledTransactionsPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();

    public ScheduledTransactionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    public void addFragment(Fragment mFragment, String title) {
        mFragments.add(mFragment);
        mTitles.add(title);
    }
}
