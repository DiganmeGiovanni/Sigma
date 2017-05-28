package org.assistant.sigma.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.assistant.sigma.R;
import org.assistant.sigma.databinding.FragDashboardBinding;
import org.assistant.sigma.model.catalogs.Periods;
import org.assistant.sigma.model.entities.Settings;
import org.assistant.sigma.utils.TextUtils;
import org.assistant.sigma.utils.Warning;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

        if (mPresenter != null) {
            mPresenter.loadSpentPeriodLg(getExcludedCategoriesForLgAmount());
            mPresenter.loadSpentPeriodSm();
            mPresenter.loadLastTransactionTime();

            showShortPeriodLabel();
        }

        return rootView;
    }

    @Override
    public void setPresenter(DashboardContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void showLastTransactionTime(Date date) {
        if (date == null) {
            viewBinding.tvLastTransactionTime.setText(getString(R.string.never));
        } else {
            viewBinding
                    .tvLastTransactionTime
                    .setText(TextUtils.relativeTime(getContext(), date.getTime()));
        }
    }

    @Override
    public void showSpentPeriodLg(double spent, double beforeLimit, int WARNING_LEVEL) {
        viewBinding.dpSpentLargePeriod.setText(TextUtils.asMoney(spent));
        viewBinding.dpSpentLargePeriod.setMax((int) (spent + beforeLimit));
        if (beforeLimit < 0) {
            viewBinding.dpSpentLargePeriod.setProgress((float) (spent + beforeLimit));
        } else {
            viewBinding.dpSpentLargePeriod.setProgress((float) spent);
        }

        switch (WARNING_LEVEL) {
            case Warning.LEVEL_NORMAL:
                viewBinding
                        .dpSpentLargePeriod
                        .setFinishedStrokeColor(ContextCompat.getColor(
                                getContext(),
                                R.color.blueWhite
                        ));
                break;
            case Warning.LEVEL_WARNING:
                viewBinding
                        .dpSpentLargePeriod
                        .setFinishedStrokeColor(ContextCompat.getColor(
                                getContext(),
                                R.color.textColorWarning
                        ));
                break;
            case Warning.LEVEL_DANGER:
                viewBinding
                        .dpSpentLargePeriod
                        .setFinishedStrokeColor(ContextCompat.getColor(
                                getContext(),
                                R.color.textColorDanger
                        ));
                break;
        }
    }

    @Override
    public void showShortPeriodLabel() {
        Settings settings = mPresenter.activeUserSettings();
        switch (settings.getSmallPeriod()) {
            case Periods.SM_DAILY:
                viewBinding.tvLbSpentShortPeriod.setText(getString(R.string.spent_today));
                break;

            case Periods.SM_DAILY_GROUPED_WEEKEND:
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mPresenter.shortPeriodStartDate());
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                    viewBinding.tvLbSpentShortPeriod.setText(getString(R.string.spent_this_weekend));
                } else {
                    viewBinding.tvLbSpentShortPeriod.setText(getString(R.string.spent_today));
                }
                break;
        }
    }

    @Override
    public void showSpentPeriodSm(double periodSmSpent, int WARNING_LEVEL) {
        viewBinding.tvSpentShortPeriod.setText(TextUtils.asMoney(periodSmSpent));
    }

    private String[] getExcludedCategoriesForLgAmount() {
        ArrayList<String> excludedCategories = new ArrayList<>();
        if (!mPresenter.activeUserSettings().isIncludeHomeSpentForLimit()) {
            excludedCategories.add(getString(R.string.category_name_home));
        }

        return excludedCategories.toArray(new String[excludedCategories.size()]);
    }
}
