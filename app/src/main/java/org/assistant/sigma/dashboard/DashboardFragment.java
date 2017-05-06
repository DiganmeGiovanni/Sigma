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

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class DashboardFragment extends Fragment implements DashboardContract.View {

    private DashboardContract.Presenter mPresenter;
    private FragDashboardBinding viewBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_dashboard, container, false);
        viewBinding = FragDashboardBinding.bind(rootView);

        setupAddTransactionButton();
        return rootView;
    }

    private void setupAddTransactionButton() {
        viewBinding.btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Remove this temporal code
                DashboardFragment.this.goToAccounts();
            }
        });
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
}
