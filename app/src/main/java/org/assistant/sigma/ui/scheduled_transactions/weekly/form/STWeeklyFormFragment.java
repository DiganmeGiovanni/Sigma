package org.assistant.sigma.ui.scheduled_transactions.weekly.form;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.assistant.sigma.R;
import org.assistant.sigma.adapters.SPAccountsAdapter;
import org.assistant.sigma.categories.picker.CategoryPickerDFragment;
import org.assistant.sigma.categories.picker.CategoryPickerPresenter;
import org.assistant.sigma.categories.picker.OnCategorySelectedListener;
import org.assistant.sigma.databinding.FragStWeeklyBinding;
import org.assistant.sigma.model.entities.Account;
import org.assistant.sigma.model.entities.ScheduledTransactionWeekly;
import org.assistant.sigma.model.entities.Transaction;
import org.assistant.sigma.model.entities.TransactionCategory;
import org.assistant.sigma.utils.services.CategoryIconProvider;

import io.realm.RealmResults;

/**
 * Created by giovanni on 17/10/17.
 *
 */
public class STWeeklyFormFragment extends Fragment implements STWeeklyFormContract.View {
    private FragStWeeklyBinding viewBinding;
    private STWeeklyFormContract.Presenter mPresenter;
    private SPAccountsAdapter accountsAdapter;

    private Account account;
    private TransactionCategory category;
    private CategoryIconProvider categoryIconProvider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_st_weekly, container, false);
        viewBinding = FragStWeeklyBinding.bind(rootView);
        setupForm();

        // Init icon provider
        categoryIconProvider = new CategoryIconProvider(
                getContext(),
                R.color.blue_dark,
                16
        );

        // Setup save FAB button
        IconDrawable iconSave = new IconDrawable(getContext(), MaterialIcons.md_done)
                .colorRes(R.color.gray_light)
                .sizeDp(24);
        viewBinding.btnSave.setImageDrawable(iconSave);
        viewBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveBtnClicked();
            }
        });

        // Preload transaction to edit if necessary
        // TODO

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void setPresenter(STWeeklyFormContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void renderAccounts(final RealmResults<Account> accounts) {
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

        // Select account from last transaction by default
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
    public void setupCategoriesPicker() {
        viewBinding.tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryPickerDFragment dFragment = new CategoryPickerDFragment();
                new CategoryPickerPresenter(dFragment);

                dFragment.setOnCategorySelectedListener(new OnCategorySelectedListener() {
                    @Override
                    public void onCategorySelected(TransactionCategory category) {
                        STWeeklyFormFragment.this.category = category;
                        viewBinding.tvCategory.setText(category.getName());
                        categoryIconProvider.setCompoundIcon(viewBinding.tvCategory, category);
                    }
                });
                dFragment.show(getFragmentManager(), "categoryPicker");
            }
        });
    }

    @Override
    public void setupForm() {
        setupCategoriesPicker();
        mPresenter.loadAccounts();
    }

    @Override
    public void onSaveBtnClicked() {

    }

    @Override
    public boolean validateForm() {
        return false;
    }

    @Override
    public void preloadTransaction(ScheduledTransactionWeekly stWeekly) {

    }
}
