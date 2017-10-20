package org.assistant.sigma.ui.scheduled_transactions.weekly.form;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.assistant.sigma.R;
import org.assistant.sigma.adapters.SPAccountsAdapter;
import org.assistant.sigma.categories.picker.CategoryPickerDFragment;
import org.assistant.sigma.categories.picker.CategoryPickerPresenter;
import org.assistant.sigma.categories.picker.OnCategorySelectedListener;
import org.assistant.sigma.databinding.FragStWeeklyFormBinding;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.ScheduledTransaction;
import org.assistant.sigma.model.entities.ScheduledTransactionWeekly;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.model.entities.TransactionCategory;
import org.assistant.sigma.ui.util.AlertPresenter;
import org.assistant.sigma.utils.services.CategoryIconProvider;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmResults;

/**
 * Created by giovanni on 17/10/17.
 *
 */
public class STWeeklyFormFragment extends Fragment implements STWeeklyFormContract.View, TimePickerDialog.OnTimeSetListener {
    private FragStWeeklyFormBinding viewBinding;
    private STWeeklyFormContract.Presenter mPresenter;
    private SPAccountsAdapter accountsAdapter;

    private CategoryIconProvider categoryIconProvider;
    private ScheduledTransactionWeekly scheduledTransactionWeekly;
    private Account account;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init object to fill through this fragment if there is not st weekly to edit
        if (getArguments() == null || !getArguments().containsKey(STWeeklyFormActivity.ST_WEEKLY_ID)) {
            scheduledTransactionWeekly = new ScheduledTransactionWeekly();
            scheduledTransactionWeekly.setScheduledTransaction(new ScheduledTransaction());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_st_weekly_form, container, false);
        viewBinding = FragStWeeklyFormBinding.bind(rootView);
        setupForm();

        // Init icon provider
        categoryIconProvider = new CategoryIconProvider(
                getContext(),
                R.color.blue_dark,
                16
        );

        // Setup save FAB button
        IconDrawable iconSave = new IconDrawable(getContext(), MaterialIcons.md_done)
                .colorRes(R.color.gray_light)
                .sizeDp(24);
        viewBinding.btnSave.setImageDrawable(iconSave);
        viewBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveBtnClicked();
            }
        });

        // Preload transaction to edit if necessary
        if (getArguments() != null && getArguments().containsKey(STWeeklyFormActivity.ST_WEEKLY_ID)) {
            String stWeeklyId = getArguments()
                    .getString(STWeeklyFormActivity.ST_WEEKLY_ID);
            mPresenter.loadTransaction(stWeeklyId);
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void setPresenter(STWeeklyFormContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void renderAccounts(final RealmResults<Account> accounts) {
        accountsAdapter = new SPAccountsAdapter(getContext(), accounts);
        viewBinding.spAccount.setAdapter(accountsAdapter);
        viewBinding.spAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                account = accounts.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        // Select account from last transaction by default if non editing 
        if (getArguments() == null || !getArguments().containsKey(STWeeklyFormActivity.ST_WEEKLY_ID)) {
            Transaction lastTransaction = mPresenter.lastTransaction();
            if (lastTransaction != null) {
                int pos = accountsAdapter.getPosition(lastTransaction.getAccount());
                viewBinding.spAccount.setSelection(pos);
                scheduledTransactionWeekly
                        .getScheduledTransaction()
                        .setAccount(lastTransaction.getAccount());
            } else {
                scheduledTransactionWeekly
                        .getScheduledTransaction()
                        .setAccount(accounts.get(0));
            }
        }
    }

    @Override
    public void setupCategoriesPicker() {
        viewBinding.tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryPickerDFragment dFragment = new CategoryPickerDFragment();
                new CategoryPickerPresenter(dFragment);

                dFragment.setOnCategorySelectedListener(new OnCategorySelectedListener() {
                    @Override
                    public void onCategorySelected(TransactionCategory category) {
                        scheduledTransactionWeekly
                                .getScheduledTransaction()
                                .setTransactionCategory(category);
                        viewBinding.tvCategory.setText(category.getName());
                        categoryIconProvider.setCompoundIcon(viewBinding.tvCategory, category);
                    }
                });
                dFragment.show(getFragmentManager(), "categoryPicker");
            }
        });
    }

    @Override
    public void setupForm() {
        setupCategoriesPicker();
        mPresenter.loadAccounts();

        // Setup the listener for week day text views
        setupToggleListener(viewBinding.tvWeekDaySun);
        setupToggleListener(viewBinding.tvWeekDayMon);
        setupToggleListener(viewBinding.tvWeekDayTue);
        setupToggleListener(viewBinding.tvWeekDayWed);
        setupToggleListener(viewBinding.tvWeekDayThu);
        setupToggleListener(viewBinding.tvWeekDayFri);
        setupToggleListener(viewBinding.tvWeekDaySat);

        // Setup time listener
        viewBinding.etTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                        STWeeklyFormFragment.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                timePickerDialog.vibrate(false);
                timePickerDialog.show(getActivity().getFragmentManager(), "TimePicker");

                return false;
            }
        });
    }

    @Override
    public void onSaveBtnClicked() {
        if (validateForm()) {
            double qty = Double.parseDouble(viewBinding.etQuantity.getText().toString());
            TransactionCategory category = scheduledTransactionWeekly
                    .getScheduledTransaction()
                    .getTransactionCategory();
            if (!category.isIncomeCategory()) {
                qty = qty * -1;
            }

            // Fill object with form values
            scheduledTransactionWeekly.getScheduledTransaction().setAccount(account);
            scheduledTransactionWeekly.getScheduledTransaction().setCreatedAt(new Date());
            scheduledTransactionWeekly.getScheduledTransaction().setQuantity(qty);
            scheduledTransactionWeekly.getScheduledTransaction().setDescription(
                    viewBinding.etDescription.getText().toString()
            );
            scheduledTransactionWeekly.getScheduledTransaction().setExcludeFromSpentResume(
                    viewBinding.cbExcludeFromSpentResume.isChecked()
            );

            mPresenter.upsertTransaction(scheduledTransactionWeekly);
            getActivity().finish();
        }
    }

    @Override
    public boolean validateForm() {
        boolean formOk = true;

        // Remove previous errors
        viewBinding.tilQuantity.setError(null);
        viewBinding.tilTime.setError(null);

        // Validate quantity
        if (viewBinding.etQuantity.getText().toString().length() < 1 ||
                Double.parseDouble(viewBinding.etQuantity.getText().toString()) < 0) {
            formOk = false;
            viewBinding.tilQuantity.setError(getString(R.string.error_quantity_greater_zero));
        }

        // Validate category
        if (scheduledTransactionWeekly.getScheduledTransaction().getTransactionCategory() == null) {
            formOk = false;
            AlertPresenter.error(
                    getContext(),
                    R.string.error_category_required_title,
                    R.string.error_category_required_title
            );
        }

        // At least one day selected?
        boolean atLeastOneDay = scheduledTransactionWeekly.atLeastOneDay();
        if (!atLeastOneDay) {
            AlertPresenter.error(
                    getContext(),
                    0,
                    R.string.choose_at_least_one_day
            );
        }

        // Time validation
        if (viewBinding.etTime.getText().length() == 0) {
            formOk = false;
            viewBinding.tilTime.setError(getString(R.string.scheduled_transaction_hour_required));
        }

        formOk = formOk && atLeastOneDay;
        return formOk;
    }

    @Override
    public void preloadTransaction(ScheduledTransactionWeekly stWeekly) {
        this.scheduledTransactionWeekly = stWeekly;
        ScheduledTransaction sTrans = stWeekly.getScheduledTransaction();

        viewBinding.tvCategory.setText(sTrans.getTransactionCategory().getName());
        categoryIconProvider.setCompoundIcon(viewBinding.tvCategory, sTrans.getTransactionCategory());

        if (sTrans.getQuantity() < 0) {
            viewBinding.etQuantity.setText(String.valueOf(sTrans.getQuantity() * -1));
        } else {
            viewBinding.etQuantity.setText(String.valueOf(sTrans.getQuantity()));
        }
        viewBinding.etQuantity.selectAll();

        viewBinding.etTime.setText(sTrans.getHourOfDay() + ":" + sTrans.getMinute());
        viewBinding.etDescription.setText(sTrans.getDescription());
        viewBinding.cbExcludeFromSpentResume.setChecked(sTrans.isExcludeFromSpentResume());

        int accountPos = accountsAdapter.getPosition(sTrans.getAccount());
        account = sTrans.getAccount();
        viewBinding.spAccount.setSelection(accountPos);

        // Preload days selectors
        changeBackground(viewBinding.tvWeekDaySun, scheduledTransactionWeekly.isOnSunday());
        changeBackground(viewBinding.tvWeekDayMon, scheduledTransactionWeekly.isOnMonday());
        changeBackground(viewBinding.tvWeekDayTue, scheduledTransactionWeekly.isOnTuesday());
        changeBackground(viewBinding.tvWeekDayWed, scheduledTransactionWeekly.isOnWednesday());
        changeBackground(viewBinding.tvWeekDayThu, scheduledTransactionWeekly.isOnThursday());
        changeBackground(viewBinding.tvWeekDayFri, scheduledTransactionWeekly.isOnFriday());
        changeBackground(viewBinding.tvWeekDaySat, scheduledTransactionWeekly.isOnSaturday());
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        viewBinding.etTime.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
        scheduledTransactionWeekly.getScheduledTransaction().setHourOfDay(hourOfDay);
        scheduledTransactionWeekly.getScheduledTransaction().setMinute(minute);
    }

    private void setupToggleListener(final TextView weekDayTv) {
        weekDayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (weekDayTv.getId()) {
                    case R.id.tv_week_day_sun:
                        scheduledTransactionWeekly
                                .setOnMonday(!scheduledTransactionWeekly.isOnSunday());
                        changeBackground(weekDayTv, scheduledTransactionWeekly.isOnSunday());
                        break;
                    case R.id.tv_week_day_mon:
                        scheduledTransactionWeekly
                                .setOnMonday(!scheduledTransactionWeekly.isOnMonday());
                        changeBackground(weekDayTv, scheduledTransactionWeekly.isOnMonday());
                        break;
                    case R.id.tv_week_day_tue:
                        scheduledTransactionWeekly
                                .setOnTuesday(!scheduledTransactionWeekly.isOnTuesday());
                        changeBackground(weekDayTv, scheduledTransactionWeekly.isOnTuesday());
                        break;
                    case R.id.tv_week_day_wed:
                        scheduledTransactionWeekly
                                .setOnWednesday(!scheduledTransactionWeekly.isOnWednesday());
                        changeBackground(weekDayTv, scheduledTransactionWeekly.isOnWednesday());
                        break;
                    case R.id.tv_week_day_thu:
                        scheduledTransactionWeekly
                                .setOnThursday(!scheduledTransactionWeekly.isOnThursday());
                        changeBackground(weekDayTv, scheduledTransactionWeekly.isOnThursday());
                        break;
                    case R.id.tv_week_day_fri:
                        scheduledTransactionWeekly
                                .setOnFriday(!scheduledTransactionWeekly.isOnFriday());
                        changeBackground(weekDayTv, scheduledTransactionWeekly.isOnFriday());
                        break;
                    case R.id.tv_week_day_sat:
                        scheduledTransactionWeekly
                                .setOnSaturday(!scheduledTransactionWeekly.isOnSaturday());
                        changeBackground(weekDayTv, scheduledTransactionWeekly.isOnSaturday());
                        break;
                }
            }
        });
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
