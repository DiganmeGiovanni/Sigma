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
import org.assistant.sigma.databinding.FragDashboardBinding;
import org.assistant.sigma.transactions.TransactionsFormFragment;
import org.assistant.sigma.transactions.TransactionsFormPresenter;
import org.assistant.sigma.utils.ActivityUtils;

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
