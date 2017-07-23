package org.assistant.sigma.transactions.form;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import org.assistant.sigma.R;
import org.assistant.sigma.databinding.ActToolbarBinding;
import org.assistant.sigma.utils.ActivityUtils;

/**
 *
 * Created by giovanni on 22/07/17.
 */
public class TransactionsFormActivity extends AppCompatActivity {

    private ActToolbarBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.act_toolbar);
        setupToolbar();

        // Load transactions form fragment
        TransactionsFormFragment formFragment = (TransactionsFormFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content);
        if (formFragment == null) {
            formFragment = new TransactionsFormFragment();

            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    formFragment,
                    R.id.content
            );
        }

        // Init presenter
        new TransactionsFormPresenter(formFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupToolbar() {
        setSupportActionBar(viewBinding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }
}
