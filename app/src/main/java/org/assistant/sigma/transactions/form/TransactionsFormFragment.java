package org.assistant.sigma.transactions.form;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.assistant.sigma.R;
import org.assistant.sigma.adapters.SPAccountsAdapter;
import org.assistant.sigma.categories.picker.CategoryPickerDFragment;
import org.assistant.sigma.categories.picker.CategoryPickerPresenter;
import org.assistant.sigma.categories.picker.OnCategorySelectedListener;
import org.assistant.sigma.databinding.FragTransactionsFormBinding;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.model.entities.TransactionCategory;
import org.assistant.sigma.utils.TextUtils;
import org.assistant.sigma.utils.callbacks.CBGeneric;
import org.assistant.sigma.utils.services.CategoryIconProvider;

import java.util.Calendar;

import io.realm.RealmResults;

/**
 * Created by giovanni on 6/05/17.
 *
 */
public class TransactionsFormFragment extends Fragment implements TransactionsFormContract.View,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private FragTransactionsFormBinding viewBinding;
    private TransactionsFormContract.Presenter mPresenter;
    private SPAccountsAdapter accountsAdapter;

    private Calendar transactionDate;
    private Account account;
    private TransactionCategory category;
    private CategoryIconProvider categoryIconProvider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_transactions_form, container, false);
        viewBinding = FragTransactionsFormBinding.bind(rootView);
        setupForm();

        // Init icon provider
        categoryIconProvider = new CategoryIconProvider(getContext(), R.color.blue_dark, 16);

        // Setup save FAB button
        IconDrawable iconEdit = new IconDrawable(getContext(), MaterialIcons.md_done)
                .colorRes(R.color.gray_light)
                .sizeDp(24);
        viewBinding.btnSave.setImageDrawable(iconEdit);
        viewBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveBtnClicked();
            }
        });

        // Preload transaction to edit if necessary
        if (getArguments() != null && getArguments().containsKey(TransactionsFormActivity.TRANSACTION_ID)) {
            String transactionId = getArguments()
                    .getString(TransactionsFormActivity.TRANSACTION_ID);
            mPresenter.loadTransaction(transactionId);
        }
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void setPresenter(TransactionsFormContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
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
                        TransactionsFormFragment.this.category = category;
                        viewBinding.tvCategory.setText(category.getName());
                        categoryIconProvider.setCompoundIcon(viewBinding.tvCategory, category);
                    }
                });
                dFragment.show(getFragmentManager(), "categoryPicker");
            }
        });
    }

    @Override
    public void updateAccountsSpinner(final RealmResults<Account> accounts) {
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

        Transaction lastTransaction = mPresenter.lastTransaction();
        if (lastTransaction != null) {
            int pos = accountsAdapter.getPosition(lastTransaction.getAccount());
            viewBinding.spAccount.setSelection(pos);
            account = lastTransaction.getAccount();
        } else {
            account = accounts.get(0);
        }
    }

    @Override
    public void setupForm() {
        setupCategoriesPicker();
        mPresenter.loadAccounts();

        transactionDate = Calendar.getInstance();
        viewBinding.etDate.setText(TextUtils.forHumans(transactionDate));
        viewBinding.etDate.setKeyListener(null);
        viewBinding.etDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                            TransactionsFormFragment.this,
                            transactionDate.get(Calendar.YEAR),
                            transactionDate.get(Calendar.MONTH),
                            transactionDate.get(Calendar.DAY_OF_MONTH)
                    );
                    datePickerDialog.vibrate(false);
                    datePickerDialog.show(getActivity().getFragmentManager(), "datePicker");
                }

                return false;
            }
        });
    }

    @Override
    public void onSaveBtnClicked() {
        if (validateForm()) {
            double qty = Double.parseDouble(viewBinding.etQuantity.getText().toString());
            if (!category.isIncomeCategory()) {

                // Validate account funds before create transaction
                if (!mPresenter.hasEnoughFunds(account, qty)) {
                    // TODO Show alert dialog
                    return;
                }

                qty = qty * -1;
            }

            Transaction transaction = new Transaction();
            transaction.setCreatedAt(transactionDate.getTime());
            transaction.setQuantity(qty);
            transaction.setDescription(viewBinding.etDescription.getText().toString());
            transaction.setExcludeFromSpentResume(viewBinding.cbExcludeFromSpentResume.isChecked());
            transaction.setTransactionCategory(category);
            transaction.setAccount(account);

            Bundle args = getArguments();
            if (args != null && args.containsKey(TransactionsFormActivity.TRANSACTION_ID)) {
                transaction.setId(args.getString(TransactionsFormActivity.TRANSACTION_ID));

                // TODO Show progress dialog
                mPresenter.updateTransaction(transaction, new CBGeneric<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        // TODO Hide progress dialog
                        getActivity().finish();
                    }
                });
            } else {
                mPresenter.saveTransaction(transaction);
                getActivity().finish();
            }
        }
    }

    @Override
    public boolean validateForm() {
        boolean formOk = true;

        // Remove previous errors
        viewBinding.tilQuantity.setError(null);
        viewBinding.tilDate.setError(null);

        // Validate category
        if (category == null) {
            formOk = false;

            new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.error_category_required_title))
                    .setMessage(getString(R.string.error_category_required))
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }

        // Validate quantity
        if (viewBinding.etQuantity.getText().toString().length() < 1 ||
                Double.parseDouble(viewBinding.etQuantity.getText().toString()) < 0) {
            formOk = false;
            viewBinding.tilQuantity.setError(getString(R.string.error_quantity_greater_zero));
        }

        // Validate date
        if (transactionDate.after(Calendar.getInstance())) {
            formOk = false;
            viewBinding.tilDate.setError(getString(R.string.error_date_before_required));
        }

        return formOk;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        transactionDate.set(Calendar.YEAR, year);
        transactionDate.set(Calendar.MONTH, monthOfYear);
        transactionDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                this,
                transactionDate.get(Calendar.HOUR_OF_DAY),
                transactionDate.get(Calendar.MINUTE),
                false
        );
        timePickerDialog.vibrate(false);
        timePickerDialog.show(getActivity().getFragmentManager(), "TimePicker");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        transactionDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        transactionDate.set(Calendar.MINUTE, minute);

        viewBinding.etDate.setText(TextUtils.forHumans(transactionDate));
    }

    @Override
    public void preloadTransaction(Transaction transaction) {
        category = transaction.getTransactionCategory();
        viewBinding.tvCategory.setText(category.getName());
        categoryIconProvider.setCompoundIcon(viewBinding.tvCategory, category);

        if (transaction.getQuantity() < 0) {
            viewBinding.etQuantity.setText(String.valueOf(transaction.getQuantity() * -1));
        } else {
            viewBinding.etQuantity.setText(String.valueOf(transaction.getQuantity()));
        }
        viewBinding.etQuantity.selectAll();

        viewBinding.etDate.setText(TextUtils.forHumans(transaction.getCreatedAt()));
        viewBinding.etDescription.setText(transaction.getDescription());
        viewBinding.cbExcludeFromSpentResume.setChecked(transaction.isExcludeFromSpentResume());

        int accountPos = accountsAdapter.getPosition(transaction.getAccount());
        viewBinding.spAccount.setSelection(accountPos);

        transactionDate.setTime(transaction.getCreatedAt());
        account = transaction.getAccount();
    }
}
