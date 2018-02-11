package org.assistant.sigma.ui.transactions.form;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.assistant.sigma.R;
import org.assistant.sigma.adapters.SPAccountsAdapter;
import org.assistant.sigma.databinding.ActTransactionFormBinding;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.model.entities.TransactionCategory;
import org.assistant.sigma.ui.categories.picker.CatPickerPresenter;
import org.assistant.sigma.ui.categories.picker.CatPickerView;
import org.assistant.sigma.ui.categories.picker.OnCategorySelectedListener;
import org.assistant.sigma.ui.util.CategoryStylesProvider;
import org.assistant.sigma.utils.DateFormatter;
import org.assistant.sigma.utils.callbacks.CBGeneric;

import java.util.Calendar;

import io.realm.RealmResults;

/**
 * Created by giovanni on 10/02/18.
 *
 */
public class ActTransactionForm extends AppCompatActivity implements TransactionsFormContract.View,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public static final String TRANSACTION_ID = "TRAN_ID";
    private ActTransactionFormBinding vBind;
    private TransactionsFormPresenter mPresenter = new TransactionsFormPresenter(this);
    private SPAccountsAdapter accountsAdapter;

    private CatPickerPresenter catPickerPresenter;
    private CatPickerView catPickerView;

    private String transactionId;
    private Calendar transactionDate;
    private Account account;
    private TransactionCategory category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vBind = DataBindingUtil.setContentView(
                this,
                R.layout.act_transaction_form
        );
        setupToolbar();

        mPresenter.onCreate();
        mPresenter.loadAccounts();
        setupDateAndTimePickers();

        // Check if transaction was passed to edit it
        if (getIntent().hasExtra(TRANSACTION_ID)) {
            transactionId = getIntent().getStringExtra(TRANSACTION_ID);
            mPresenter.loadTransaction(transactionId);
        }

        setupCategoryPicker();
        setupSaveBtn();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        catPickerPresenter.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        transactionDate.set(Calendar.YEAR, year);
        transactionDate.set(Calendar.MONTH, monthOfYear);
        transactionDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        vBind.formView.etDate.setText(
                DateFormatter.asSimpleDateMonth(transactionDate.getTime()).toUpperCase()
        );
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        transactionDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        transactionDate.set(Calendar.MINUTE, minute);

        vBind.formView.etTime.setText(DateFormatter.asHourMinute(transactionDate));
    }

    @Override
    public void renderAccounts(final RealmResults<Account> accounts) {
        accountsAdapter = new SPAccountsAdapter(this, accounts);
        vBind.formView.spAccount.setAdapter(accountsAdapter);
        vBind.formView.spAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                account = accounts.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        Transaction lastTransaction = mPresenter.lastTransaction();
        if (lastTransaction != null) {
            int pos = accountsAdapter.getPosition(lastTransaction.getAccount());
            vBind.formView.spAccount.setSelection(pos);
            account = lastTransaction.getAccount();
        } else {
            account = accounts.get(0);
        }
    }

    @Override
    public void toggleLoading(boolean isLoading) {
        if (isLoading) {
            vBind.toolbar.setVisibility(View.GONE);
            vBind.svFormContainer.setVisibility(View.GONE);
            vBind.btnSave.setVisibility(View.GONE);
            vBind.pbSaving.setVisibility(View.VISIBLE);
        } else {
            vBind.pbSaving.setVisibility(View.GONE);
            vBind.toolbar.setVisibility(View.VISIBLE);
            vBind.svFormContainer.setVisibility(View.VISIBLE);
            vBind.btnSave.setVisibility(View.VISIBLE);
        }
    }

    public void preloadTransaction(Transaction transaction) {
        if (transaction.getQuantity() < 0) {
            vBind.formView.etQuantity.setText(String.valueOf(transaction.getQuantity() * -1));
        } else {
            vBind.formView.etQuantity.setText(String.valueOf(transaction.getQuantity()));
        }
        vBind.formView.etQuantity.selectAll();

        int accountPos = accountsAdapter.getPosition(transaction.getAccount());
        vBind.formView.spAccount.setSelection(accountPos);
        account = transaction.getAccount();

        category = transaction.getTransactionCategory();
        // TODO .tvCategory.setText(category.getName());
        // TODO categoryIconProvider.setCompoundIcon(viewBinding.tvCategory, category);

        transactionDate.setTime(transaction.getCreatedAt());
        vBind.formView.etDate.setText(DateFormatter.asSimpleDateMonth(transactionDate.getTime()));
        vBind.formView.etTime.setText(DateFormatter.asHourMinute(transactionDate));

        vBind.formView.swExcludeFromSpentResume.setChecked(transaction.isExcludeFromSpentResume());

        vBind.formView.etDescription.setText(transaction.getDescription());
    }

    private void setupCategoryPicker() {
        final BottomSheetBehavior<View> bsBehavior = BottomSheetBehavior
                .from(vBind.bottomSheetContainer.getRoot());

        catPickerView = new CatPickerView(
                vBind.bottomSheetContainer.catPicker.getRoot(),
                new OnCategorySelectedListener() {
                    @Override
                    public void onCategorySelected(TransactionCategory category) {
                        ActTransactionForm.this.category = category;
                        bsBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                        // Set category icon
                        vBind.formView.tvCategory.setCompoundDrawablePadding(8);
                        final IconDrawable icon = CategoryStylesProvider.makeCategoryIcon(
                                ActTransactionForm.this,
                                category
                        );
                        icon.sizeDp(16);

                        // Set category text
                        vBind.formView.tvCategory.setCompoundDrawablesWithIntrinsicBounds(
                                icon,
                                null,
                                null,
                                null
                        );
                        vBind.formView.tvCategory.setTextColor(CategoryStylesProvider.getCategoryColor(
                                ActTransactionForm.this,
                                category
                        ));
                        vBind.formView.tvCategory.setText(category.getName());
                    }
                }
        );
        catPickerPresenter = new CatPickerPresenter(catPickerView);
        catPickerPresenter.onCreate();
        catPickerView.setPresenter(catPickerPresenter);
        catPickerView.init();

        vBind.formView.tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bsBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        bsBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void setupDateAndTimePickers() {
        transactionDate = Calendar.getInstance();
        vBind.formView.etDate.setText(
                DateFormatter.asSimpleDateMonth(transactionDate.getTime()).toUpperCase()
        );
        vBind.formView.etDate.setKeyListener(null);
        vBind.formView.etDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                            ActTransactionForm.this,
                            transactionDate.get(Calendar.YEAR),
                            transactionDate.get(Calendar.MONTH),
                            transactionDate.get(Calendar.DAY_OF_MONTH)
                    );
                    datePickerDialog.show(
                            getFragmentManager(),
                            "datePicker"
                    );
                }
                return false;
            }
        });

        vBind.formView.etTime.setText(DateFormatter.asHourMinute(transactionDate));
        vBind.formView.etTime.setKeyListener(null);
        vBind.formView.etTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                            ActTransactionForm.this,
                            transactionDate.get(Calendar.HOUR_OF_DAY),
                            transactionDate.get(Calendar.MINUTE),
                            false
                    );
                    timePickerDialog.show(getFragmentManager(), "timePicker");
                }
                return false;
            }
        });
    }

    private void setupSaveBtn() {
        IconDrawable iconEdit = new IconDrawable(this, MaterialIcons.md_done)
                .colorRes(R.color.gray_light)
                .sizeDp(24);
        vBind.btnSave.setImageDrawable(iconEdit);
        vBind.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    upsertTransaction();
                }
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(vBind.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            IconDrawable iconBack = new IconDrawable(this, MaterialIcons.md_arrow_back)
                    .colorRes(R.color.gray_light)
                    .actionBarSize();
            actionBar.setHomeAsUpIndicator(iconBack);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

            if (getIntent().getExtras() != null && getIntent().hasExtra(TRANSACTION_ID)) {
                actionBar.setTitle(R.string.edit_transaction);
            } else {
                actionBar.setTitle(R.string.add_transaction);
            }
        }
    }

    private void upsertTransaction() {
        double qty = Double.parseDouble(vBind.formView.etQuantity.getText().toString());
        if (!category.isIncomeCategory()) {

            // Validate account funds before create transaction
            if (!mPresenter.hasEnoughFunds(account, qty, transactionId)) {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.error_insufficient_funds))
                        .setMessage(getString(R.string.error_insufficient_funds_detail))
                        .setPositiveButton(R.string.ok, null)
                        .show();
                return;
            }

            qty = qty * -1;
        }

        Transaction transaction = new Transaction();
        transaction.setCreatedAt(transactionDate.getTime());
        transaction.setQuantity(qty);
        transaction.setDescription(vBind.formView.etDescription.getText().toString());
        transaction.setExcludeFromSpentResume(vBind.formView.swExcludeFromSpentResume.isChecked());
        transaction.setTransactionCategory(category);
        transaction.setAccount(account);
        if (transactionId != null) {
            transaction.setId(transactionId);
        }

        toggleLoading(true);
        mPresenter.upsert(transaction, new CBGeneric<Boolean>() {
            @Override
            public void onResponse(Boolean success) {
                if (success) {
                    ActTransactionForm.this.finish();
                } else {
                    toggleLoading(false);
                    new AlertDialog.Builder(ActTransactionForm.this)
                            .setTitle(getString(R.string.error_transaction_save_title))
                            .setMessage(getString(R.string.error_transaction_save))
                            .setPositiveButton(R.string.ok, null)
                            .show();
                }
            }
        });
    }

    public boolean validateForm() {
        boolean formOk = true;

        // Remove previous errors
        vBind.formView.etQuantity.setError(null);
        vBind.formView.etDate.setError(null);

        // Validate category
        if (category == null) {
            formOk = false;

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.error_category_required_title))
                    .setMessage(getString(R.string.error_category_required))
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }

        // Validate quantity
        if (vBind.formView.etQuantity.getText().toString().length() < 1 ||
                Double.parseDouble(vBind.formView.etQuantity.getText().toString()) < 0) {
            formOk = false;
            vBind.formView.etQuantity.setError(getString(R.string.error_quantity_greater_zero));
        }

        // Validate date
        if (transactionDate.after(Calendar.getInstance())) {
            formOk = false;
            vBind.formView.etDate.setError(getString(R.string.error_date_before_required));
        }

        return formOk;
    }
}
