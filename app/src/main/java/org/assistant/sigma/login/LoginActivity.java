package org.assistant.sigma.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.assistant.sigma.R;
import org.assistant.sigma.model.repositories.TransactionCategoriesRepository;
import org.assistant.sigma.utils.ActivityUtils;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        this.ensureTransactionsCategoriesExistence();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content);
        if (loginFragment == null) {
            loginFragment = new LoginFragment();

            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    loginFragment,
                    R.id.content
            );
        }

        // Initialize presenter
        new LoginPresenter(loginFragment);
    }

    private void ensureTransactionsCategoriesExistence() {
        Map<String, String> categoriesSpent = new HashMap<>();
        categoriesSpent.put(
                getString(R.string.category_name_transport),
                getString(R.string.category_description_transport)
        );
        categoriesSpent.put(
                getString(R.string.category_name_provisions),
                getString(R.string.category_description_provisions)
        );
        categoriesSpent.put(
                getString(R.string.category_name_clothes_shoes),
                getString(R.string.category_description_clother_shoes)
        );
        categoriesSpent.put(
                getString(R.string.category_name_home),
                getString(R.string.category_description_home)
        );
        categoriesSpent.put(
                getString(R.string.category_name_restaurants),
                getString(R.string.category_description_restaurants)
        );
        categoriesSpent.put(
                getString(R.string.category_name_bar),
                getString(R.string.category_description_bar)
        );
        categoriesSpent.put(
                getString(R.string.category_name_other),
                getString(R.string.category_description_other)
        );

        Map<String, String> categoriesIncomes = new HashMap<>();
        categoriesIncomes.put(
                getString(R.string.category_name_salary),
                getString(R.string.category_description_salary)
        );
        categoriesIncomes.put(
                getString(R.string.category_name_other),
                getString(R.string.category_description_other)
        );

        new TransactionCategoriesRepository().ensureCategoriesExistence(
                categoriesSpent,
                categoriesIncomes
        );
    }
}
