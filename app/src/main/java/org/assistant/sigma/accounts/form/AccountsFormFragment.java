package org.assistant.sigma.accounts.form;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.assistant.sigma.R;
import org.assistant.sigma.databinding.FragAccountsFormBinding;
import org.assistant.sigma.model.entities.Account;

import java.util.Date;

/**
 *
 * Created by giovanni on 5/05/17.
 */
public class AccountsFormFragment extends Fragment implements AccountsFormContract.View {

    private FragAccountsFormBinding viewBinding;
    private AccountsFormContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_accounts_form, container, false);
        viewBinding = FragAccountsFormBinding.bind(rootView);

        setupSaveBtn();
        setupETCard();
        return rootView;
    }

    @Override
    public void setPresenter(AccountsFormContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public boolean validateFields() {
        boolean formOk = true;

        // Clear previous errors
        viewBinding.tilName.setError(null);
        viewBinding.tilCardNumber.setError(null);

        // Validate name
        if (viewBinding.etName.getText().length() < 3) {
            formOk = false;
            viewBinding.tilName.setError(getString(R.string.error_name_required));
        }

        // Validate card number
        if (!validateCreditCardNumber(viewBinding.etCardNumber.getText().toString())) {
            formOk = false;
            viewBinding.tilCardNumber.setError(getString(R.string.error_card_number_invalid));
        }

        return formOk;
    }

    @Override
    public void setupSaveBtn() {
        viewBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    Account account = new Account();
                    account.setName(viewBinding.etName.getText().toString());
                    account.setCardNumber(viewBinding.etCardNumber.getText().toString());
                    account.setCreatedAt(new Date());

                    mPresenter.saveAccount(account);
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    private void setupETCard() {
        viewBinding.etCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {

                // Remove spacing char
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (' ' == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }

                // Insert char where needed.
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) &&
                            TextUtils.split(s.toString(), String.valueOf(' ')).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(' '));
                    }
                }
            }
        });
    }

    private boolean validateCreditCardNumber(String number) {
        if (number.length() == 0) return true;

        // Four blocks of four digits?
        String[] split = number.split(" ");
        if (split.length != 4) return false;

        for (String portion : split) {
            if (portion.length() != 4 || !isInteger(portion)) {
                return false;
            }
        }

        return true;
    }

    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
