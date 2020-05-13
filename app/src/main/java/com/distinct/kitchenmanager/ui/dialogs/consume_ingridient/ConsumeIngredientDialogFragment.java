package com.distinct.kitchenmanager.ui.dialogs.consume_ingridient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.element_behaviour.IngredientAmountFormatter;

public class ConsumeIngredientDialogFragment extends DialogFragment {

    private static String ingredientIdString = "ingredientId";
    private ConsumeIngredientViewModel consumeIngredientViewModel;
    private View root;

    private TextView amountWasInFridgeTextView;
    private TextView ingredientNameTextView;
    private TextView amountToConsumeTextView;
    private SeekBar amountToConsumeSeekBar;


    public static ConsumeIngredientDialogFragment newInstance(String ingredientId) {

        ConsumeIngredientDialogFragment fragment = new ConsumeIngredientDialogFragment();
        Bundle args = new Bundle();
        args.putString(ingredientIdString, ingredientId);
        fragment.setArguments(args);
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = inflater.inflate(R.layout.dialog_consume_ingridient, container, false);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        consumeIngredientViewModel = new ViewModelProvider(this).get(ConsumeIngredientViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            findViews();
            setWeightTypes();
            loadIngredient();
            setOnClickListener();
            setOnProgressChanged();
            setObservers();
        });
    }

    private void setWeightTypes() {
        if (getContext() != null) {
            String[] weightTypes = (getContext().getResources().getStringArray(R.array.weight_types));
            consumeIngredientViewModel.setWeightTypes(weightTypes);
        }
    }

    private void findViews() {
        ingredientNameTextView = root.findViewById(R.id.ingredient_name_text_view);
        amountWasInFridgeTextView = root.findViewById(R.id.was_in_fridge_text_view);
        amountToConsumeTextView = root.findViewById(R.id.to_consume_text_view);
        amountToConsumeSeekBar = root.findViewById(R.id.to_consume_seekBar);

    }

    private void loadIngredient() {
        if (getArguments() != null) {
            consumeIngredientViewModel.loadIngredientFromDatabase(getArguments().getString(ingredientIdString));
        }
    }

    private void setOnClickListener() {
        root.findViewById(R.id.consume_ingredient_button).setOnClickListener(view -> {
            consumeIngredientViewModel.consumeIngredient(getContext());
            dismiss();
        });

        root.findViewById(R.id.add_to_consume_weight_button).setOnClickListener(view -> {
            consumeIngredientViewModel.incrementConsumedWeight();
        });

        root.findViewById(R.id.subtract_from_consume_weight_button).setOnClickListener(view -> {
            consumeIngredientViewModel.decrementConsumedWeight();
        });
    }

    private void setOnProgressChanged() {
        amountToConsumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean isFromUser) {
                consumeIngredientViewModel.setCurrentAmountToConsume(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setObservers() {
        consumeIngredientViewModel.getIngredientLiveData().observe(this, ingredient -> {
            ingredientNameTextView.setText(ingredient.name);
            amountWasInFridgeTextView.setText(amountWithWeightType(ingredient.fullAmount));
        });
        consumeIngredientViewModel.getCurrentAmountToConsume().observe(this, amountToConsume -> {
            amountToConsumeTextView.setText(amountWithWeightType(amountToConsume));
        });
    }

    private String amountWithWeightType(float amount) {
        return IngredientAmountFormatter.geFormattedString(amount) + " " + consumeIngredientViewModel.weightType;
    }

}
