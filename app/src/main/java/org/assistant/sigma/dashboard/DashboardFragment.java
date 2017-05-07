package org.assistant.sigma.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.assistant.sigma.R;
import org.assistant.sigma.accounts.AccountsActivity;
import org.assistant.sigma.adapters.TransactionsAdapter;
import org.assistant.sigma.databinding.FragDashboardBinding;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.transactions.TransactionsFormFragment;
import org.assistant.sigma.transactions.TransactionsFormPresenter;
import org.assistant.sigma.utils.ActivityUtils;
import org.assistant.sigma.utils.TextUtils;

import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class DashboardFragment extends Fragment implements DashboardContract.View {

    private DashboardContract.Presenter mPresenter;
    private FragDashboardBinding viewBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_dashboard, container, false);
        viewBinding = FragDashboardBinding.bind(rootView);

        setupAddButton();
        if (mPresenter != null) {
            mPresenter.loadPeriodSmAmount();
            mPresenter.loadPeriodSmTransactionsCount();
            mPresenter.loadLastTransactions();
        }

        return rootView;
    }

    @Override
    public void setPresenter(DashboardContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void goToAccounts() {
        Intent intent = new Intent(getActivity(), AccountsActivity.class);
        startActivity(intent);
    }

    @Override
    public void goToAddTransaction() {
        TransactionsFormFragment fragment = new TransactionsFormFragment();
        new TransactionsFormPresenter(fragment); // Initialize presenter

        ActivityUtils.replaceFragmentInActivity(
                getFragmentManager(),
                fragment,
                R.id.content,
                "transactionsForm"
        );
    }

    @Override
    public void showPeriodSmAmount(double periodSmSpent, int WARNING_LEVEL) {
        viewBinding.tvPeriodSmAmount.setText(TextUtils.asMoney(periodSmSpent));
    }

    @Override
    public void showPeriodSmTransactionsCount(long transactionsCount) {
        viewBinding.tvPeriodSmTransactions.setText(String.valueOf(transactionsCount));
    }

    @Override
    public void updateLastTransactions(RealmResults<Transaction> transactions) {
        TransactionsAdapter adapter = new TransactionsAdapter(getContext(), transactions);
        viewBinding.lvLastTransactions.setAdapter(adapter);
    }

    /**
     * Configure add button to go to add transaction or to
     * add account if user has not registered accounts
     */
    private void setupAddButton() {
        viewBinding.btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPresenter.allowAddTransaction()) {
                    DashboardFragment.this.goToAddTransaction();
                } else {
                    DashboardFragment.this.goToAccounts();
                }
            }
        });
    }
}
