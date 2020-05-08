package com.distinct.kitchenmanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.distinct.kitchenmanager.ui.dialogs.change_ingridient.ChangeIngredientDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static String fridgeId;
    private static String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApplicationContextSingleton.getInstance().initialize(this);

        fridgeId = getIntent().getStringExtra(getResources().getString(R.string.id_fridge_preferences_field));
        userName = getIntent().getStringExtra(getResources().getString(R.string.user_name_preferences_field));

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_fridge, R.id.navigation_shopping_list, R.id.navigation_food_dairy, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        findViewById(R.id.add_ingredient_floating_action_button).setOnClickListener(view -> {
            DialogFragment dialog = ChangeIngredientDialogFragment.newInstance(-1);
            dialog.show(getSupportFragmentManager(), "AddIngredientDialog");
        });

    }

    public static String getFridgeId() {
        return fridgeId;
    }

    public static String getUserName() {
        return userName;
    }

}
