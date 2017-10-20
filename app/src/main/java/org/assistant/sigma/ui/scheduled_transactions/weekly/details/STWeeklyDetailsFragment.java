package org.assistant.sigma.ui.scheduled_transactions.weekly.details;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.assistant.sigma.R;
import org.assistant.sigma.databinding.FragStWeeklyDetailsBinding;
import org.assistant.sigma.model.entities.ScheduledTransaction;
import org.assistant.sigma.model.entities.ScheduledTransactionWeekly;
import org.assistant.sigma.ui.scheduled_transactions.weekly.form.STWeeklyFormActivity;
import org.assistant.sigma.ui.util.AlertPresenter;
import org.assistant.sigma.utils.TextUtils;
import org.assistant.sigma.utils.services.CategoryIconProvider;

/**
 * Created by giovanni on 19/10/17.
 *
 */
public class STWeeklyDetailsFragment extends Fragment implements STWeeklyDetailsContract.View {
    private STWeeklyDetailsContract.Presenter mPresenter;
    private FragStWeeklyDetailsBinding viewBinding;

    private CategoryIconProvider categoryIconProvider;
    private String stWeeklyId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_st_weekly_details, container, false);
        viewBinding = FragStWeeklyDetailsBinding.bind(rootView);

        // Init icon provider
        categoryIconProvider = new CategoryIconProvider(getContext(), R.color.blue_dark, 16);

        // Setup fab
        IconDrawable iconEdit = new IconDrawable(getContext(), MaterialIcons.md_edit)
                .colorRes(R.color.gray_light)
                .sizeDp(24);
        viewBinding.btnEdit.setImageDrawable(iconEdit);
        viewBinding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditSTWeekly(stWeeklyId);
            }
        });

        // Retrieve id of scheduled transaction to display
        stWeeklyId = getArguments().getString(STWeeklyDetailsActivity.ST_WEEKLY_ID);
        mPresenter.loadSTWeekly(stWeeklyId);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_btn_delete, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_delete) {
            onDeleteBtnClicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void setPresenter(STWeeklyDetailsContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void renderSTWeekly(ScheduledTransactionWeekly stWeekly) {
        ScheduledTransaction sTrans = stWeekly.getScheduledTransaction();

        viewBinding.tvAccount.setText(sTrans.getAccount().getName());
        if (sTrans.getQuantity() < 0) {
            viewBinding.tvQuantity.setText(TextUtils.asMoney(sTrans.getQuantity() * -1));
        } else {
            viewBinding.tvQuantity.setText(TextUtils.asMoney(sTrans.getQuantity()));
        }

        viewBinding.tvCategory.setText(sTrans.getTransactionCategory().getName());
        categoryIconProvider.setCompoundIcon(
                viewBinding.tvCategory,
                sTrans.getTransactionCategory()
        );

        viewBinding.tvExcludeFromSpentResume.setText(sTrans.isExcludeFromSpentResume()
                ? getString(R.string.yes)
                : getString(R.string.no)
        );

        viewBinding.tvTime.setText(sTrans.getHourOfDay() + ":" + sTrans.getMinute());
        viewBinding.tvDescription.setText(sTrans.getDescription());

        // Enable/Disable week days
        changeBackground(viewBinding.tvWeekDaySun, stWeekly.isOnSunday());
        changeBackground(viewBinding.tvWeekDayMon, stWeekly.isOnMonday());
        changeBackground(viewBinding.tvWeekDayTue, stWeekly.isOnTuesday());
        changeBackground(viewBinding.tvWeekDayWed, stWeekly.isOnWednesday());
        changeBackground(viewBinding.tvWeekDayThu, stWeekly.isOnThursday());
        changeBackground(viewBinding.tvWeekDayFri, stWeekly.isOnFriday());
        changeBackground(viewBinding.tvWeekDayFri, stWeekly.isOnFriday());
    }

    @Override
    public void goToEditSTWeekly(String stWeeklyId) {
        Intent intent = new Intent(getContext(), STWeeklyFormActivity.class);
        intent.putExtra(
                STWeeklyFormActivity.ST_WEEKLY_ID,
                stWeeklyId
        );

        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDeleteBtnClicked() {
        AlertPresenter.confirm(
                getContext(),
                R.string.u_sure,
                R.string.confirm_delete_scheduled_transaction,
                R.string.delete,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenter.deleteSTWeekly(stWeeklyId);
                        getActivity().finish();
                    }
                }
        );
    }

    private void changeBackground(TextView tvWeekDay, boolean isActive) {
        tvWeekDay.setBackground(ContextCompat.getDrawable(
                getContext(),
                isActive
                        ? R.drawable.background_circle_choosen
                        : R.drawable.background_circle_bordered
        ));
    }
}
