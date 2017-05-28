package org.assistant.sigma.landing;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.assistant.sigma.R;
import org.assistant.sigma.accounts.AccountsActivity;
import org.assistant.sigma.adapters.RTransactionsAdapter;
import org.assistant.sigma.dashboard.DashboardFragment;
import org.assistant.sigma.dashboard.DashboardPresenter;
import org.assistant.sigma.databinding.ActLandingBinding;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.transactions.form.TransactionsFormDFragment;
import org.assistant.sigma.transactions.form.TransactionsFormPresenter;
import org.assistant.sigma.transactions.list.TransactionsListContract;
import org.assistant.sigma.transactions.list.TransactionsListPresenter;
import org.assistant.sigma.utils.ActivityUtils;

import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 18/05/17.
 */
public class LandingActivity extends AppCompatActivity implements TransactionsListContract.View {

    private ActLandingBinding viewBinding;

    private TransactionsListContract.Presenter mPresenter;
    private DashboardPresenter dashboardPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.act_landing);

        // Initialize presenter
        new TransactionsListPresenter(this);

        loadDashboardFragment();
        setupAddButton();
        setupRVTransactions();
        mPresenter.loadLastTransactions();
    }

    @Override
    public void setPresenter(TransactionsListContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void toggleLoadingAnimation(boolean isLoading) {

    }

    @Override
    public void renderTransactions(RealmResults<Transaction> transactions) {
        RTransactionsAdapter adapter = new RTransactionsAdapter(getBaseContext(), transactions);
        viewBinding.rvTransactions.setAdapter(adapter);
    }

    private void loadDashboardFragment() {
        DashboardFragment dashboardFragment = new DashboardFragment();
        dashboardPresenter = new DashboardPresenter(dashboardFragment);

        ActivityUtils.addFragmentToActivity(
                getSupportFragmentManager(),
                dashboardFragment,
                R.id.header_content_frame
        );
    }

    private void setupAddButton() {
        viewBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dashboardPresenter.allowAddTransaction()) {

                    // Go to add transactions
                    TransactionsFormDFragment dFragment = new TransactionsFormDFragment();
                    new TransactionsFormPresenter(dFragment);
                    dFragment.show(getSupportFragmentManager(), "transactionFormDialog");

//                    TransactionsFormFragment fragment = new TransactionsFormFragment();
//                    new TransactionsFormPresenter(fragment);
//
//                    ActivityUtils.replaceFragmentInActivity(
//                            getSupportFragmentManager(),
//                            fragment,
//                            R.id.body_content_frame,
//                            "form_transactions"
//                    );
                } else {
                    // Go to accounts
                    Intent intent = new Intent(LandingActivity.this, AccountsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setupRVTransactions() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        viewBinding.rvTransactions.setLayoutManager(layoutManager);
        viewBinding.rvTransactions.setItemAnimator(new DefaultItemAnimator());
    }
}
