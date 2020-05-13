package com.distinct.kitchenmanager.ui.dialogs.change_ingridient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.distinct.kitchenmanager.ApplicationContextSingleton;
import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.element_behaviour.DateFormatter;
import com.distinct.kitchenmanager.model.database.entity.Ingredient;
import com.distinct.kitchenmanager.model.enums.IngredientStageType;
import com.distinct.kitchenmanager.ui.dialogs.date_pick.DatePickerFragment;
import com.distinct.kitchenmanager.ui.dialogs.date_pick.OnDatePickedListener;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class ChangeIngredientDialogFragment extends DialogFragment implements OnDatePickedListener {

    private static String ingredientIdString = "ingredientId";

    private ChangeIngredientViewModel changeIngredientViewModel;
    private View root;

    private AutoCompleteTextView ingredientNameEditText;
    private AutoCompleteTextView manufacturerEditText;
    private EditText ingredientAmountEditText;
    private EditText commentEditText;
    private TextView amountOfIngredientsTextView;
    private TextView actionsDestinationNameTextView;
    private TextView shelfLifeTextView;
    private EditText caloriesEditText;
    private RadioGroup stageTypeRadioGroup;
    private Button changeIngredientButton;
    private Spinner weightTypesSpinner;

    private LinearLayout categoriesChipsLinearLayout;
    private ArrayList<Chip> chips;


    public static ChangeIngredientDialogFragment newInstance(String ingredientId) {

        ChangeIngredientDialogFragment fragment = new ChangeIngredientDialogFragment();
        Bundle args = new Bundle();
        args.putString(ingredientIdString, ingredientId);
        fragment.setArguments(args);
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = inflater.inflate(R.layout.dialog_change_ingridient, container, false);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        changeIngredientViewModel = new ViewModelProvider(this).get(ChangeIngredientViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            findViews(root);
            setAutoCompleteTextViews();
            setSpinners();
            checkIfIngredientExists();
            setOnClickListeners(root);
            setObservers();
            addChips();
        });
    }

    private void findViews(View root) {
        ingredientNameEditText = root.findViewById(R.id.ingredient_name_edit_text);
        manufacturerEditText = root.findViewById(R.id.manufacturer_edit_text);
        ingredientAmountEditText = root.findViewById(R.id.ingredient_amount_edit_text);
        commentEditText = root.findViewById(R.id.comment_edit_text);
        amountOfIngredientsTextView = root.findViewById(R.id.amount_of_ingredients_text_view);
        changeIngredientButton = root.findViewById(R.id.change_ingredient_button);
        caloriesEditText = root.findViewById(R.id.calories_edit_text);
        weightTypesSpinner = root.findViewById(R.id.weight_types_spinner);
        stageTypeRadioGroup = root.findViewById(R.id.stage_type_radio_group);
        actionsDestinationNameTextView = root.findViewById(R.id.actions_destination_name_text_view);
        shelfLifeTextView = root.findViewById(R.id.shelf_life_text_view);
        categoriesChipsLinearLayout = root.findViewById(R.id.categories_chips_linear_layout);
    }

    private void setAutoCompleteTextViews() {
        changeIngredientViewModel.getIngredientNamesLiveData().observe(getViewLifecycleOwner(), item -> {
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(ApplicationContextSingleton.getInstance().getApplicationContext(), android.R.layout.simple_list_item_1, item);
            ingredientNameEditText.setAdapter(adapter);
        });
    }

    private void setSpinners() {
        if (this.getContext() != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                    R.array.weight_types, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            weightTypesSpinner.setAdapter(adapter);
        }
    }

    private void checkIfIngredientExists() {
        if (getArguments() != null) {
            changeIngredientViewModel.idOfIngredientToChange = getArguments().getString(ingredientIdString);
            if (changeIngredientViewModel.idOfIngredientToChange != null && !changeIngredientViewModel.idOfIngredientToChange.equals("")) {
                if (getDialog() != null)
                    getDialog().setTitle(R.string.change_ingredient);
                actionsDestinationNameTextView.setText(R.string.move_to);
                changeIngredientViewModel.loadIngredientFromDatabase();
            } else {
                actionsDestinationNameTextView.setText(R.string.add_to);

                if (getDialog() != null)
                    getDialog().setTitle(R.string.add_ingredient);
            }
        }
    }


    private void setOnClickListeners(View root) {
        root.findViewById(R.id.subtract_from_amount_of_ingredients_button).setOnClickListener(subtractIngredient);
        root.findViewById(R.id.add_to_amount_of_ingredients_button).setOnClickListener(addIngredient);
        if (changeIngredientViewModel.idOfIngredientToChange != null && !changeIngredientViewModel.idOfIngredientToChange.equals("")) {
            changeIngredientButton.setOnClickListener(saveIngredient);
            changeIngredientButton.setText(R.string.save);
        } else {
            changeIngredientButton.setOnClickListener(addIngredientToShoppingList);
            changeIngredientButton.setText(R.string.add);
        }

        shelfLifeTextView.setOnClickListener(view -> showDatePickerDialog());
    }

    private void addChips() {
        String[] categories = getResources().getStringArray(R.array.categories);
        chips = new ArrayList<>();
        for (String genre : categories) {
            Chip chip = new Chip(getContext());
            chip.setText(genre);
            categoriesChipsLinearLayout.addView(chip);
            chips.add(chip);
            chip.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    for (Chip other : chips) {
                        if (chip != other)
                            other.setChecked(false);
                    }
                    changeIngredientViewModel.saveCategory(chip.getText().toString());
                }

            });
        }
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = DatePickerFragment.newInstance(this, changeIngredientViewModel.getShelfLifeDataCalendar());
        newFragment.show(getChildFragmentManager(), "datePicker");
    }


    private void setObservers() {
        changeIngredientViewModel.getIngredientLiveData().observe(getViewLifecycleOwner(), item -> {
            amountOfIngredientsTextView.setText(String.valueOf(item.amountOfIngredients));
            changeIngredientButton.setClickable(item.amountOfIngredients > 0);
            fillData(item);
        });
    }

    private void fillData(Ingredient item) {
        ingredientNameEditText.setText(item.name);
        manufacturerEditText.setText(item.manufacturer);

        commentEditText.setText(item.comment);

        weightTypesSpinner.setSelection(item.weightType);

        if (item.caloriesInDistinct != 0)
            caloriesEditText.setText(String.valueOf(item.caloriesInDistinct));

        if (item.amountOfDistinct != 0) {
            if (item.amountOfDistinct % (int) item.amountOfDistinct == 0)
                ingredientAmountEditText.setText(String.valueOf((int) item.amountOfDistinct));
            else ingredientAmountEditText.setText(String.valueOf(item.amountOfDistinct));
        }

        if (item.shelfLifeTime != 0)
            shelfLifeTextView.setText(DateFormatter.getStringFromDateTime(item.shelfLifeTime));

        int selectedRadioButtonId = 0;
        if (item.stageType == IngredientStageType.InBasket.ordinal())
            selectedRadioButtonId = R.id.in_basket_radio_button;
        else if (item.stageType == IngredientStageType.InFridge.ordinal())
            selectedRadioButtonId = R.id.in_fridge_radio_button;
        else if (item.stageType == IngredientStageType.ToBuy.ordinal())
            selectedRadioButtonId = R.id.to_buy_radio_button;

        if (selectedRadioButtonId != 0)
            stageTypeRadioGroup.check(selectedRadioButtonId);
    }

    private View.OnClickListener subtractIngredient = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            performActionAfterChecks(() -> {
                changeIngredientViewModel.decrementAmountOfIngredients();
                return null;
            });
        }
    };

    private View.OnClickListener addIngredient = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            performActionAfterChecks(() -> {
                changeIngredientViewModel.incrementAmountOfIngredients();
                return null;
            });

        }
    };

    private View.OnClickListener saveIngredient = view -> performActionAfterChecks(new Callable<Void>() {
        @Override
        public Void call() {
            changeIngredientViewModel.setIngredientId();
            changeIngredientViewModel.updateIngredientInDatabase();
            dismiss();
            return null;
        }
    });

    private View.OnClickListener addIngredientToShoppingList = view -> performActionAfterChecks(new Callable<Void>() {
        @Override
        public Void call() {
            changeIngredientViewModel.addIngredientToDatabase();
            dismiss();
            return null;
        }
    });

    private void performActionAfterChecks(Callable<Void> task) {

        if (validateForm(ingredientNameEditText)) return;
        if (validateForm(caloriesEditText)) return;
        if (validateForm(ingredientAmountEditText)) return;

        String ingredientName = ingredientNameEditText.getText().toString();
        String manufacturerName = manufacturerEditText.getText().toString();
        String ingredientAmountString = ingredientAmountEditText.getText().toString();
        String comment = commentEditText.getText().toString();

        int weightType = weightTypesSpinner.getSelectedItemPosition();
        int selectedRadioButtonStage = stageTypeRadioGroup.getCheckedRadioButtonId();
        int stageTypeOrdinal = 0;
        float ingredientAmount = 0;
        if (!ingredientAmountString.equals(""))
            ingredientAmount = Float.parseFloat(ingredientAmountString);

        int calories = 0;
        if (!caloriesEditText.getText().toString().equals(""))
            calories = Integer.parseInt(caloriesEditText.getText().toString());

        if (selectedRadioButtonStage == R.id.in_basket_radio_button)
            stageTypeOrdinal = IngredientStageType.InBasket.ordinal();
        else if (selectedRadioButtonStage == R.id.in_fridge_radio_button)
            stageTypeOrdinal = IngredientStageType.InFridge.ordinal();
        else if (selectedRadioButtonStage == R.id.to_buy_radio_button)
            stageTypeOrdinal = IngredientStageType.ToBuy.ordinal();

        changeIngredientViewModel.setValues(ingredientName, manufacturerName, ingredientAmount, weightType, comment, stageTypeOrdinal, calories);

        try {
            task.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        performActionAfterChecks(() -> {
            changeIngredientViewModel.setShelfLife(year, month, day);
            return null;
        });
    }

    private boolean validateForm(EditText editText) {
        String text = editText.getText().toString();

        boolean valid = true;
        if (TextUtils.isEmpty(text)) {
            editText.setError(getResources().getString(R.string.is_empty));
            valid = false;
        } else {
            editText.setError(null);
        }
        return !valid;
    }
}
