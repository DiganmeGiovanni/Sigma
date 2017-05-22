package org.assistant.sigma.landing;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.assistant.sigma.R;
import org.assistant.sigma.accounts.AccountsActivity;
import org.assistant.sigma.dashboard.DashboardFragment;
import org.assistant.sigma.dashboard.DashboardPresenter;
import org.assistant.sigma.databinding.ActLandingBinding;
import org.assistant.sigma.transactions.form.TransactionsFormFragment;
import org.assistant.sigma.transactions.form.TransactionsFormPresenter;
import org.assistant.sigma.transactions.list.TransactionsListFragment;
import org.assistant.sigma.transactions.list.TransactionsListPresenter;
import org.assistant.sigma.utils.ActivityUtils;

/**
 *
 * Created by giovanni on 18/05/17.
 */
public class LandingActivity extends AppCompatActivity {

    private ActLandingBinding viewBinding;

    private DashboardPresenter dashboardPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.act_landing);

        loadDashboardFragment();
        loadTransactionsListFragment();
        setupAddButton();
    }

    private void loadDashboardFragment() {
        DashboardFragment dashboardFragment = new DashboardFragment();
        dashboardPresenter = new DashboardPresenter(dashboardFragment);

        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                dashboardFragment,
                R.id.header_content_frame,
                "dashboard"
        );
    }

    private void loadTransactionsListFragment() {
        TransactionsListFragment fragment = new TransactionsListFragment();
        new TransactionsListPresenter(fragment);

        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                fragment,
                R.id.body_content_frame,
                "list_transactions"
        );
    }

    private void setupAddButton() {
        viewBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dashboardPresenter.allowAddTransaction()) {
                    // TODO Go to add transactions
                    TransactionsFormFragment fragment = new TransactionsFormFragment();
                    new TransactionsFormPresenter(fragment);

                    ActivityUtils.replaceFragmentInActivity(
                            getSupportFragmentManager(),
                            fragment,
                            R.id.body_content_frame,
                            "form_transactions"
                    );
                } else {
                    // Go to accounts
                    Intent intent = new Intent(LandingActivity.this, AccountsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
