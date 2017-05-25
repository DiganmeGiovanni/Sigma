package org.assistant.sigma.transactions.form;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.assistant.sigma.R;
import org.assistant.sigma.adapters.SPAccountsAdapter;
import org.assistant.sigma.adapters.SPTransactionCategoriesAdapter;
import org.assistant.sigma.databinding.FragTransactionsFormBinding;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.model.entities.TransactionCategory;
import org.assistant.sigma.utils.TextUtils;

import java.util.Calendar;
import java.util.List;

import io.realm.RealmResults;

/**
 *
 * Created by giovanni on 23/05/17.
 */
public class TransactionsFormDFragment extends DialogFragment implements TransactionsFormContract.View,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private FragTransactionsFormBinding viewBinding;
    private TransactionsFormContract.Presenter mPresenter;

    private Calendar transactionDate;
    private Account account;
    private TransactionCategory category;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_transactions_form, container, false);
        viewBinding = FragTransactionsFormBinding.bind(rootView);

        setupForm();
        setupSaveBtn();

        getDialog().setTitle(getString(R.string.register_transaction));
        return rootView;
    }

    @Override
    public void setPresenter(TransactionsFormContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void updateAccountsSpinner(final RealmResults<Account> accounts) {
        SPAccountsAdapter adapter = new SPAccountsAdapter(getDialog().getContext(), accounts);
        viewBinding.spAccount.setAdapter(adapter);
        viewBinding.spAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                account = accounts.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        account = accounts.get(0);
    }

    @Override
    public void updateCategoriesSpinner(final List<TransactionCategory> categories) {
        SPTransactionCategoriesAdapter adapter =
                new SPTransactionCategoriesAdapter(getDialog().getContext(), categories);
        viewBinding.spCategory.setAdapter(adapter);
        viewBinding.spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = categories.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        category = categories.get(0);
    }

    @Override
    public void setupForm() {
        mPresenter.loadAccounts();
        mPresenter.loadSpentCategories(getDialog().getContext());
        viewBinding.swIsIncome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mPresenter.loadIncomeCategories();
                } else {
                    mPresenter.loadSpentCategories(getDialog().getContext());
                }
            }
        });

        transactionDate = Calendar.getInstance();
        viewBinding.etDate.setText(getString(R.string.now));
        viewBinding.etDate.setKeyListener(null);
        viewBinding.etDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                            TransactionsFormDFragment.this,
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
    public void setupSaveBtn() {
        viewBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    double qty = Double.parseDouble(viewBinding.etQuantity.getText().toString());
                    if (!viewBinding.swIsIncome.isChecked()) {
                        qty = qty * -1;
                    }

                    Transaction transaction = new Transaction();
                    transaction.setCreatedAt(transactionDate.getTime());
                    transaction.setQuantity(qty);
                    transaction.setDescription(viewBinding.etDescription.getText().toString());
                    transaction.setTransactionCategory(category);
                    transaction.setAccount(account);

                    mPresenter.saveTransaction(transaction);
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    @Override
    public boolean validateForm() {
        boolean formOk = true;

        // Remove previous errors
        viewBinding.tilQuantity.setError(null);
        viewBinding.tilDate.setError(null);

        // Validate quantity
        if (viewBinding.etQuantity.getText().length() < 1 ||
                Double.parseDouble(viewBinding.etQuantity.getText().toString()) < 0) {
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

        viewBinding.etDate.setText(TextUtils.relative(transactionDate, true));
    }
}
