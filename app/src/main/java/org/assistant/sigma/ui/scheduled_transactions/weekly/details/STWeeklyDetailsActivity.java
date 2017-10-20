package org.assistant.sigma.ui.scheduled_transactions.weekly.details;

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
 * Created by giovanni on 19/10/17.
 *
 */
public class STWeeklyDetailsActivity extends AppCompatActivity {
    public static final String ST_WEEKLY_ID = "ST_WEEKLY_ID";

    private ActToolbarBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.act_toolbar);
        setupToolbar();

        // Load details fragment
        STWeeklyDetailsFragment detailsFragment = (STWeeklyDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content);
        if (detailsFragment == null) {
            detailsFragment = new STWeeklyDetailsFragment();

            // Retrieve st weekly id
            Bundle args = new Bundle();
            args.putString(ST_WEEKLY_ID, getIntent().getStringExtra(ST_WEEKLY_ID));
            detailsFragment.setArguments(args);

            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    detailsFragment,
                    R.id.content
            );
        }

        new STWeeklyDetailsPresenter(detailsFragment);
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

            actionBar.setTitle(R.string.scheduled_transaction);
        }
    }
}
