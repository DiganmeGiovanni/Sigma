package org.assistant.sigma.ui.accounts.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.assistant.sigma.R;
import org.assistant.sigma.databinding.AccountDetailBinding;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.utils.TextUtils;

/**
 * Created by giovanni on 29/01/18.
 *
 */
public class FragAccountDetail extends Fragment implements AccountDetailContract.View {
    public static final String ACCOUNT_ID = "ACCOUNT_ID";
    private AccountDetailBinding vBind;
    private AccountDetailPresenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = new AccountDetailPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.account_detail, container, false);
        vBind = AccountDetailBinding.bind(rootView);

        if (getArguments() != null && getArguments().containsKey(ACCOUNT_ID)) {
            final String accountId = getArguments().getString(ACCOUNT_ID);

            mPresenter.loadAccount(accountId);
            mPresenter.loadCurrentBalance(accountId);
            mPresenter.loadBalanceAtCurrShortPeriod(accountId);
            mPresenter.loadBalanceAtCurrLargePeriod(accountId);

            vBind.btnRecalculateBalance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.recalculateBalance(accountId);
                }
            });
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void renderAccount(Account account) {
        vBind.tvAccountName.setText(account.getName());
        if (account.getCardNumber() != null && account.getCardNumber().length() > 0) {
            vBind.tvAccountNumber.setText(account.getCardNumber());
            vBind.tvAccountNumber.setVisibility(View.VISIBLE);
        } else {
            vBind.tvAccountNumber.setVisibility(View.GONE);
        }
    }

    @Override
    public void renderCurrentBalance(Double balance) {
        vBind.tvBalanceNow.setText(TextUtils.asMoney(balance));
    }

    @Override
    public void renderCurrShortPeriodLabel(String label) {
        vBind.tvDateShortPeriod.setText(label);
    }

    @Override
    public void renderCurrShortPeriodBalance(Double balance) {
        vBind.tvBalanceShortPeriod.setText(TextUtils.asMoney(balance));
    }

    @Override
    public void renderCurrLargePeriodLabel(String label) {
        vBind.tvDateLargePeriod.setText(label);
    }

    @Override
    public void renderCurrLargePeriodBalance(Double balance) {
        vBind.tvBalanceLargePeriod.setText(TextUtils.asMoney(balance));
    }

    @Override
    public void toggleLoading(boolean isLoading) {
        if (isLoading) {
            vBind.llDetailsContainer.setVisibility(View.GONE);
            vBind.llLoadingContainer.setVisibility(View.VISIBLE);
        } else {
            vBind.llLoadingContainer.setVisibility(View.GONE);
            vBind.llDetailsContainer.setVisibility(View.VISIBLE);
        }
    }
}
