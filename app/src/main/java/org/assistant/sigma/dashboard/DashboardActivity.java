package org.assistant.sigma.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.assistant.sigma.R;
import org.assistant.sigma.utils.ActivityUtils;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
}
