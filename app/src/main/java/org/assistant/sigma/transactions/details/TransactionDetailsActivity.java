package org.assistant.sigma.transactions.details;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.assistant.sigma.R;
import org.assistant.sigma.databinding.ActToolbarBinding;
import org.assistant.sigma.utils.ActivityUtils;

/**
 * Created by giovanni on 27/09/17.
 *
 */
public class TransactionDetailsActivity extends AppCompatActivity {
    public static final String TRANSACTION_ID = "TRANSACTION_ID";

    private ActToolbarBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.act_toolbar);
        setupToolbar();

        // Load details fragment
        TransactionDetailsFragment detailsFragment = (TransactionDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content);
        if (detailsFragment == null) {
            detailsFragment = new TransactionDetailsFragment();

            Bundle args = new Bundle();
            args.putString(TRANSACTION_ID, getIntent().getStringExtra(TRANSACTION_ID));
            detailsFragment.setArguments(args);

            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    detailsFragment,
                    R.id.content
            );
        }

        new TransactionDetailsPresenter(detailsFragment);
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
            IconDrawable iconBack = new IconDrawable(this, MaterialIcons.md_arrow_back)
                    .colorRes(R.color.gray_light)
                    .actionBarSize();
            actionBar.setHomeAsUpIndicator(iconBack);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

            actionBar.setTitle(R.string.transaction_details);
        }
    }
}
