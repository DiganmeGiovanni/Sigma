package org.assistant.sigma.ui.accounts.detail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.assistant.sigma.R;
import org.assistant.sigma.databinding.AccountDetailBinding;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.utils.TextUtils;

/**
 * Created by giovanni on 24/01/18.
 *
 */
public class ActAccountDetail extends AppCompatActivity implements AccountDetailContract.View {
    public static final String ACCOUNT_ID = "ACCOUNT_ID";
    private AccountDetailBinding vBind;
    private AccountDetailPresenter mPresenter;
    private String accountId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vBind = DataBindingUtil.setContentView(this, R.layout.account_detail);
        setupToolbar();

        if (getIntent().hasExtra(ACCOUNT_ID)) {
            accountId = getIntent().getStringExtra(ACCOUNT_ID);

            mPresenter = new AccountDetailPresenter(this);
            mPresenter.loadAccount(accountId);
            mPresenter.loadCurrentBalance(accountId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_details, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_recalculate_balance) {
            mPresenter.recalculateBalance(accountId);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
    public void toggleLoading(boolean isLoading) {
        if (isLoading) {
            vBind.llDetailsContainer.setVisibility(View.GONE);
            vBind.llLoadingContainer.setVisibility(View.VISIBLE);
        } else {
            vBind.llLoadingContainer.setVisibility(View.GONE);
            vBind.llDetailsContainer.setVisibility(View.VISIBLE);
        }
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

            actionBar.setTitle(R.string.account_details);
        }
    }
}
