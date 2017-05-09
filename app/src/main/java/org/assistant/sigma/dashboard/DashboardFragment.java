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
import org.assistant.sigma.model.catalogs.Periods;
import org.assistant.sigma.model.entities.Settings;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.transactions.TransactionsFormFragment;
import org.assistant.sigma.transactions.TransactionsFormPresenter;
import org.assistant.sigma.utils.ActivityUtils;
import org.assistant.sigma.utils.TextUtils;
import org.assistant.sigma.utils.Warning;

import java.util.ArrayList;
import java.util.Calendar;

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
            mPresenter.loadPeriodLgAmount(getExcludedCategoriesForLgAmount());
            mPresenter.loadPeriodLgTransactionsCount();
            mPresenter.loadLastTransactions();

            showPeriodsNames();
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
    public void showPeriodLgAmount(double spent, double beforeLimit, int WARNING_LEVEL) {
        viewBinding.tvPeriodAmount.setText(TextUtils.asMoney(spent));
        switch (WARNING_LEVEL) {
            case Warning.LEVEL_NORMAL:
                viewBinding.tvPeriodAmount
                        .setTextColor(getResources().getColor(R.color.textColorLightMain));
                viewBinding.tvUnderLimit
                        .setTextColor(getResources().getColor(R.color.textColorLightMain));
                break;
            case Warning.LEVEL_WARNING:
                viewBinding.tvPeriodAmount
                        .setTextColor(getResources().getColor(R.color.textColorWarning));
                viewBinding.tvUnderLimit
                        .setTextColor(getResources().getColor(R.color.textColorWarning));
                break;
            case Warning.LEVEL_DANGER:
                viewBinding.tvPeriodAmount
                        .setTextColor(getResources().getColor(R.color.textColorDanger));
                viewBinding.tvUnderLimit
                        .setTextColor(getResources().getColor(R.color.textColorDanger));
                break;
        }

        viewBinding.tvUnderLimit.setText(TextUtils.asMoney(Math.abs(beforeLimit)));
        if (beforeLimit < 0) {
            viewBinding.tvLbLimit.setText(getString(R.string.over_limit));
        }
    }

    @Override
    public void showPeriodLgTransactionsCount(long count) {
        viewBinding.tvPeriodTransactions.setText(String.valueOf(count));
    }

    @Override
    public void showPeriodsNames() {
        Settings settings = mPresenter.activeUserSettings();
        switch (settings.getSmallPeriod()) {
            case Periods.SM_DAILY:
                viewBinding.tvPeriodSmName.setText(getString(R.string.today));
                break;

            case Periods.SM_DAILY_GROUPED_WEEKEND:
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mPresenter.shortPeriodStartDate());
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                    viewBinding.tvPeriodSmName.setText(getString(R.string.this_weekend));
                } else {
                    viewBinding.tvPeriodSmName.setText(getString(R.string.today));
                }
                break;
        }

        switch (settings.getLargePeriod()) {
            case Periods.LG_WEEKLY:
                viewBinding.tvPeriodName.setText(getString(R.string.this_week));
                break;
            case Periods.LG_FORTNIGHT:
                viewBinding.tvPeriodName.setText(getString(R.string.this_fortnight));
                break;
            case Periods.LG_MONTHLY:
                viewBinding.tvPeriodName.setText(getString(R.string.this_month));
                break;
        }
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

    private String[] getExcludedCategoriesForLgAmount() {
        ArrayList<String> excludedCategories = new ArrayList<>();
        if (!mPresenter.activeUserSettings().isIncludeHomeSpentForLimit()) {
            excludedCategories.add(getString(R.string.category_name_home));
        }

        return excludedCategories.toArray(new String[excludedCategories.size()]);
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
