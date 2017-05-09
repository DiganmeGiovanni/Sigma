package org.assistant.sigma.dashboard;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import org.assistant.sigma.R;
import org.assistant.sigma.accounts.AccountsActivity;
import org.assistant.sigma.databinding.ActDashboardBinding;
import org.assistant.sigma.settings.SettingsFragment;
import org.assistant.sigma.settings.SettingsPresenter;
import org.assistant.sigma.utils.ActivityUtils;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class DashboardActivity extends AppCompatActivity {

    private ActDashboardBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.act_dashboard);

        viewBinding.toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(viewBinding.toolbar);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        setupDrawerLayout();

        DashboardFragment dashboardFragment = (DashboardFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content);
        if (dashboardFragment == null) {
            dashboardFragment = new DashboardFragment();

            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    dashboardFragment,
                    R.id.content
            );
        }

        // Initialize presenter and bind to view
        new DashboardPresenter(dashboardFragment);
    }

    private void setupDrawerLayout() {
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,
                viewBinding.drawerLayout,
                viewBinding.toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        viewBinding.drawerLayout.addDrawerListener(mDrawerToggle);
        setupDrawerMenuItems();
    }

    private void setupDrawerMenuItems() {
        TextView tvPreferences = (TextView) findViewById(R.id.tv_preferences);
        tvPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsFragment fragment = new SettingsFragment();
                new SettingsPresenter(fragment);

                ActivityUtils.replaceFragmentInActivity(
                        getSupportFragmentManager(),
                        fragment,
                        R.id.content,
                        "settings"
                );

                viewBinding.drawerLayout.closeDrawer(Gravity.START);
            }
        });

        TextView tvAccounts = (TextView) findViewById(R.id.tv_accounts);
        tvAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewBinding.drawerLayout.closeDrawer(Gravity.START);

                Intent intent = new Intent(DashboardActivity.this, AccountsActivity.class);
                startActivity(intent);
            }
        });
    }
}
