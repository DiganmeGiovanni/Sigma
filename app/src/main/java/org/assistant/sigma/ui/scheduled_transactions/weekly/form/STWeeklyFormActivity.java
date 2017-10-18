package org.assistant.sigma.ui.scheduled_transactions.weekly.form;

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
 * Created by giovanni on 17/10/17.
 *
 */
public class STWeeklyFormActivity extends AppCompatActivity {

    private ActToolbarBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.act_toolbar);
        setupToolbar();

        // Load st weekly form fragment
        STWeeklyFormFragment formFragment = (STWeeklyFormFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content);
        if (formFragment == null) {
            formFragment = new STWeeklyFormFragment();

            // Load scheduled transaction to edit
            // TODO

            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    formFragment,
                    R.id.content
            );
        }

        // Init presenter
        new STWeeklyFormPresenter(formFragment);
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

//            if (getIntent().getExtras() != null && getIntent().hasExtra(TRANSACTION_ID)) {
//                actionBar.setTitle(R.string.edit_transaction);
//            } else {
//                actionBar.setTitle(R.string.add_transaction);
//            }
        }
    }
}
