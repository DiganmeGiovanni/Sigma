package org.assistant.sigma.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.assistant.sigma.R;
import org.assistant.sigma.utils.ActivityUtils;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class DashboardActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dashboard);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);

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
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
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

        mDrawerLayout.addDrawerListener(mDrawerToggle);

    }
}
