package org.assistant.sigma.ui.scheduled_transactions;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.assistant.sigma.DrawerActivity;
import org.assistant.sigma.R;
import org.assistant.sigma.databinding.ActScheduledTransactionsBinding;
import org.assistant.sigma.model.repositories.UsersRepository;
import org.assistant.sigma.ui.adapters.scheduledtransactions.ScheduledTransactionsPagerAdapter;
import org.assistant.sigma.ui.scheduled_transactions.monthly.list.STMonthlyListFragment;
import org.assistant.sigma.ui.scheduled_transactions.monthly.list.STMonthlyListPresenter;
import org.assistant.sigma.ui.scheduled_transactions.weekly.form.STWeeklyFormActivity;
import org.assistant.sigma.ui.scheduled_transactions.weekly.list.STWeeklyListFragment;
import org.assistant.sigma.ui.scheduled_transactions.weekly.list.STWeeklyListPresenter;

/**
 * Created by giovanni on 12/10/17.
 *
 */
public class ScheduledTransactionsActivity extends DrawerActivity {
    private ActScheduledTransactionsBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup toolbar
        viewBinding = DataBindingUtil.setContentView(this, R.layout.act_scheduled_transactions);
        UsersRepository usersRepository = new UsersRepository();
        super.setupToolbar(
                viewBinding.toolbar,
                viewBinding.drawerLayout,
                usersRepository.activeUser(),
                R.id.tv_scheduled_transactions
        );

        loadTabsContent();
        setupFabButton();
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
    }

    private void setupFabButton() {
        IconDrawable iconAdd = new IconDrawable(this, MaterialIcons.md_add)
                .colorRes(R.color.gray_light)
                .sizeDp(24);
        viewBinding.btnAdd.setImageDrawable(iconAdd);
        viewBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Verify if user has active accounts


                // Go to add scheduled transaction
                Intent intent = new Intent(
                        ScheduledTransactionsActivity.this,
                        STWeeklyFormActivity.class
                );
                startActivity(intent);
            }
        });
    }
}
