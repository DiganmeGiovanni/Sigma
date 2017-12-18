package org.assistant.sigma.landing;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.assistant.sigma.DrawerActivity;
import org.assistant.sigma.R;
import org.assistant.sigma.accounts.AccountsActivity;
import org.assistant.sigma.dashboard.DashboardFragment;
import org.assistant.sigma.dashboard.DashboardPresenter;
import org.assistant.sigma.databinding.ActLandingBinding;
import org.assistant.sigma.model.repositories.UsersRepository;
import org.assistant.sigma.transactions.form.TransactionsFormActivity;
import org.assistant.sigma.transactions.list.TransactionsListFragment;
import org.assistant.sigma.transactions.list.TransactionsListPresenter;
import org.assistant.sigma.utils.ActivityUtils;

/**
 *
 * Created by giovanni on 18/05/17.
 */
public class LandingActivity extends DrawerActivity {

    private ActLandingBinding viewBinding;

    private DashboardPresenter dashboardPresenter;
    private UsersRepository usersRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersRepository = new UsersRepository();

        // Setup toolbar
        viewBinding = DataBindingUtil.setContentView(this, R.layout.act_landing);
        super.setupToolbar(
                viewBinding.toolbar,
                viewBinding.drawerLayout,
                usersRepository.activeUser(),
                R.id.tv_transactions
        );

        // Setup dashboard
        loadDashboardFragment();

        // Setup transactions list
        loadTransactionsListFragment();

        // Setup add transactions button
        setupAddButton();


        // Temporal alarm
//        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, ScheduledTransactionReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//
//        // Set alarm fire time
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 21);
//        calendar.set(Calendar.MINUTE, 5);
//
//        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5 * 1000, pendingIntent);
//        Toast.makeText(this, "Alarm scheduled", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        usersRepository.onDestroy();
    }

    private void loadDashboardFragment() {
        DashboardFragment dashboardFragment = (DashboardFragment) getSupportFragmentManager()
                .findFragmentById(R.id.header_content_frame);

        if (dashboardFragment == null) {
            dashboardFragment = new DashboardFragment();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    dashboardFragment,
                    R.id.header_content_frame
            );
        }

        dashboardPresenter = new DashboardPresenter(dashboardFragment);
    }

    private void loadTransactionsListFragment() {
        TransactionsListFragment listFragment  = (TransactionsListFragment)
                getSupportFragmentManager().findFragmentById(R.id.body_content_frame);

        if (listFragment == null) {
            listFragment = new TransactionsListFragment();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    listFragment,
                    R.id.body_content_frame
            );
        }

        new TransactionsListPresenter(listFragment);
    }

    private void setupAddButton() {
        IconDrawable iconAdd = new IconDrawable(this, MaterialIcons.md_add)
                .colorRes(R.color.gray_light)
                .sizeDp(24);
        viewBinding.btnAdd.setImageDrawable(iconAdd);
        viewBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dashboardPresenter.allowAddTransaction()) {

                    // Go to add transactions
                    Intent intent = new Intent(LandingActivity.this, TransactionsFormActivity.class);
                    startActivity(intent);
                } else {
                    // Go to accounts
                    Intent intent = new Intent(LandingActivity.this, AccountsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
