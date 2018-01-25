package org.assistant.sigma.ui.overview;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.assistant.sigma.DrawerActivity;
import org.assistant.sigma.R;
import org.assistant.sigma.databinding.ActOverviewBinding;
import org.assistant.sigma.model.entities.Settings;
import org.assistant.sigma.ui.adapters.FragPagerAdapter;
import org.assistant.sigma.utils.PeriodUtils;

import java.util.Date;

/**
 * Created by giovanni on 24/12/17.
 *
 */
public class ActOverview extends DrawerActivity {
    private ActOverviewBinding vBind;
    private OverviewPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new OverviewPresenter();
        vBind = DataBindingUtil.setContentView(
                this,
                R.layout.act_overview
        );
        super.setupToolbar(
                vBind.toolbar,
                vBind.drawerLayout,
                R.id.tv_current_period
        );

        setupTabLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    private void setupTabLayout() {
        Settings settings = mPresenter.getCurrentUserSettings();
        FragPagerAdapter fragPagerAdapter = new FragPagerAdapter(getSupportFragmentManager());

        Date shPStart = PeriodUtils.getCurrentShortPeriodStart(settings);
        Bundle shortPeriodArgs = new Bundle();
        shortPeriodArgs.putLong(FragOverview.START_TIME_MILLIS, shPStart.getTime());
        FragOverview shortPeriodOverviewFrag = new FragOverview();
        shortPeriodOverviewFrag.setArguments(shortPeriodArgs);
        fragPagerAdapter.addFragment(
                shortPeriodOverviewFrag,
                getString(PeriodUtils.getCurrentShortPeriodLabel(settings))
        );

        Date lgPStart = PeriodUtils.getCurrentLargePeriodStart(settings);
        Bundle largePeriodArgs = new Bundle();
        largePeriodArgs.putLong(FragOverview.START_TIME_MILLIS, lgPStart.getTime());
        FragOverview largePeriodOverviewFrag = new FragOverview();
        largePeriodOverviewFrag.setArguments(largePeriodArgs);
        fragPagerAdapter.addFragment(
                largePeriodOverviewFrag,
                getString(PeriodUtils.getCurrentLargePeriodLabel(settings))
        );

        vBind.pager.setAdapter(fragPagerAdapter);
        vBind.tabLayout.setupWithViewPager(vBind.pager);
    }
}
