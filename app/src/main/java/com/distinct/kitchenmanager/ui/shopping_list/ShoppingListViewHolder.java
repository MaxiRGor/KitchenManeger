package com.distinct.kitchenmanager.ui.shopping_list;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.model.enums.IngredientStageType;
import com.distinct.kitchenmanager.model.room.entity.Ingredient;
import com.distinct.kitchenmanager.ui.dialogs.ChangeIngredientDialogFragment;
import com.google.android.material.snackbar.Snackbar;

public class ShoppingListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, View.OnLongClickListener {

    FragmentManager fragmentManager;
    ShoppingListViewModel shoppingListViewModel;
    private Ingredient ingredient;
    private CheckBox isInBasketCheckbox;
    private LinearLayout amountOfIngredientsLinearLayout;
    private LinearLayout ingredientAmountLinearLayout;
    private TextView amountOfIngredientsTextView;
    private TextView ingredientNameTextView;
    private TextView ingredientAmountTextView;
    private TextView ingredientWeightTypeTextView;
    private TextView manufacturerTextView;


    ShoppingListViewHolder(@NonNull View itemView) {
        super(itemView);
        isInBasketCheckbox = itemView.findViewById(R.id.is_in_basket_checkbox);
        amountOfIngredientsLinearLayout = itemView.findViewById(R.id.amount_of_ingredients_linear_layout);
        amountOfIngredientsTextView = itemView.findViewById(R.id.amount_of_ingredients_text_view);
        ingredientNameTextView = itemView.findViewById(R.id.ingredient_name_text_view);
        ingredientAmountTextView = itemView.findViewById(R.id.ingredient_amount_text_view);
        ingredientWeightTypeTextView = itemView.findViewById(R.id.weight_type_text_view);
        ingredientAmountLinearLayout = itemView.findViewById(R.id.ingredient_amount_linear_layout);
        manufacturerTextView = itemView.findViewById(R.id.manufacturer_text_view);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        isInBasketCheckbox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        DialogFragment dialog = ChangeIngredientDialogFragment.newInstance(ingredient.id);
        dialog.show(fragmentManager, "ChangeIngredientDialog");
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if (ingredient != null)
            shoppingListViewModel.changeIngredientState(ingredient.id, b);
    }

    @Override
    public boolean onLongClick(View view) {
        Snackbar.make(view, R.string.want_to_delete_item, Snackbar.LENGTH_LONG)
                .setAction(R.string.yes, v -> shoppingListViewModel.deleteIngredient(ingredient.id))
                .show();
        return true;
    }

    void bind(Ingredient ingredient, String weightTypeString) {
        this.ingredient = ingredient;

        if (ingredient.stageType == IngredientStageType.ToBuy.ordinal())
            isInBasketCheckbox.setChecked(false);
        else if (ingredient.stageType == IngredientStageType.InBasket.ordinal())
            isInBasketCheckbox.setChecked(true);

        ingredientNameTextView.setText(ingredient.name);

        if (!ingredient.manufacturer.equals(""))
            manufacturerTextView.setText(ingredient.manufacturer);
        else manufacturerTextView.setVisibility(View.GONE);

        if (ingredient.amountOfIngredients > 1) {
            amountOfIngredientsLinearLayout.setVisibility(View.VISIBLE);
            amountOfIngredientsTextView.setText(String.valueOf(ingredient.amountOfIngredients));
        } else {
            amountOfIngredientsLinearLayout.setVisibility(View.GONE);
        }


        if (ingredient.amount != 0) {
            ingredientAmountLinearLayout.setVisibility(View.VISIBLE);

            if (ingredient.amount % (int) ingredient.amount == 0)
                ingredientAmountTextView.setText(String.valueOf((int) ingredient.amount));

            else ingredientAmountTextView.setText(String.valueOf(ingredient.amount));

            ingredientWeightTypeTextView.setText(weightTypeString);

        } else ingredientAmountLinearLayout.setVisibility(View.GONE);

    }


}