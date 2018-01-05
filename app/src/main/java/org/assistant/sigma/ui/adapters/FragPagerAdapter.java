package org.assistant.sigma.ui.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giovanni on 5/01/18.
 * Generic adapter to use for fragment pagers
 */
public class FragPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();

    public FragPagerAdapter(FragmentManager fm) {
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

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    public void addFragment(Fragment mFragment, String title) {
        mFragments.add(mFragment);
        mTitles.add(title);
    }
}
