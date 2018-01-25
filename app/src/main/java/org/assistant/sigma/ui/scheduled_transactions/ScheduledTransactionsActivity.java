package org.assistant.sigma.ui.scheduled_transactions;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.assistant.sigma.DrawerActivity;
import org.assistant.sigma.R;
import org.assistant.sigma.databinding.ActScheduledTransactionsBinding;
import org.assistant.sigma.ui.adapters.scheduledtransactions.ScheduledTransactionsPagerAdapter;
import org.assistant.sigma.ui.scheduled_transactions.monthly.list.STMonthlyListFragment;
import org.assistant.sigma.ui.scheduled_transactions.monthly.list.STMonthlyListPresenter;
import org.assistant.sigma.ui.scheduled_transactions.weekly.form.STWeeklyFormActivity;
import org.assistant.sigma.ui.scheduled_transactions.weekly.list.STWeeklyListFragment;
import org.assistant.sigma.ui.scheduled_transactions.weekly.list.STWeeklyListPresenter;
import org.assistant.sigma.ui.util.AlertPresenter;

/**
 * Created by giovanni on 12/10/17.
 *
 */
public class ScheduledTransactionsActivity extends DrawerActivity {
    private SchTransActPresenter schTransPresenter;
    private ActScheduledTransactionsBinding viewBinding;
    private int selectedTabPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(
                this,
                R.layout.act_scheduled_transactions
        );
        super.setupToolbar(
                viewBinding.toolbar,
                viewBinding.drawerLayout,
                R.id.tv_scheduled_transactions
        );

        this.schTransPresenter = new SchTransActPresenter();

        loadTabsContent();
        setupFabButton();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.schTransPresenter != null) {
            this.schTransPresenter.onDestroy();
        }
    }

    private void loadTabsContent() {
        STWeeklyListFragment weeklyListFragment = new STWeeklyListFragment();
        new STWeeklyListPresenter(weeklyListFragment);

        STMonthlyListFragment monthlyListFragment = new STMonthlyListFragment();
        new STMonthlyListPresenter(monthlyListFragment);

        ScheduledTransactionsPagerAdapter pagerAdapter =
                new ScheduledTransactionsPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(weeklyListFragment, getString(R.string.weekly));
        pagerAdapter.addFragment(monthlyListFragment, getString(R.string.monthly));

        viewBinding.viewpager.setAdapter(pagerAdapter);
        viewBinding.tabLayout.setupWithViewPager(viewBinding.viewpager);
        viewBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTabPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void setupFabButton() {
        IconDrawable iconAdd = new IconDrawable(this, MaterialIcons.md_add)
                .colorRes(R.color.gray_light)
                .sizeDp(24);
        viewBinding.btnAdd.setImageDrawable(iconAdd);
        viewBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!schTransPresenter.haveActiveAccounts()) {
                    AlertPresenter.error(
                            ScheduledTransactionsActivity.this,
                            R.string.without_accounts,
                            R.string.active_accounts_required
                    );
                    return;
                }

                // Go to add scheduled transaction
                if (selectedTabPosition == 0) {
                    Intent intent = new Intent(
                            ScheduledTransactionsActivity.this,
                            STWeeklyFormActivity.class
                    );
                    startActivity(intent);
                } else {
                    // TODO
                }
            }
        });
    }
}
