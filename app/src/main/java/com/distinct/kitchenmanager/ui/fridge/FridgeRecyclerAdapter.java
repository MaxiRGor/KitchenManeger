package com.distinct.kitchenmanager.ui.fridge;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.element_behaviour.ItemTouchHelper.ItemTouchHelperAdapter;
import com.distinct.kitchenmanager.model.database.database.FirestoreSource;
import com.distinct.kitchenmanager.model.database.database.RoomDatabaseSource;
import com.distinct.kitchenmanager.model.database.entity.Consumed;
import com.distinct.kitchenmanager.model.database.entity.Ingredient;
import com.distinct.kitchenmanager.model.enums.IngredientStageType;
import com.distinct.kitchenmanager.ui.dialogs.consume_ingridient.ConsumeIngredientDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FridgeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {

    private Activity activity;
    private FragmentManager fragmentManager;
    private FridgeViewModel fridgeViewModel;
    private ArrayList<Ingredient> ingredients;
    private String[] weightTypes;

    FridgeRecyclerAdapter(Activity context, List<Ingredient> ingredients, FragmentManager fragmentManager, FridgeViewModel fridgeViewModel) {
        this.activity = context;
        this.fragmentManager = fragmentManager;
        this.ingredients = new ArrayList<>();
        this.ingredients.clear();
        if (ingredients != null)
            this.ingredients.addAll(ingredients);
        this.fridgeViewModel = fridgeViewModel;
        this.weightTypes = context.getResources().getStringArray(R.array.weight_types);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_fridge, parent, false);
        return new FridgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        FridgeViewHolder viewHolder = (FridgeViewHolder) holder;
        viewHolder.fragmentManager = fragmentManager;
        viewHolder.fridgeViewModel = fridgeViewModel;
        viewHolder.bind(ingredient, weightTypes[ingredient.weightType]);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    @Override
    public void onItemMovedToRight(int position) {
        //fully consume
        if (arrayListNotContainsPosition(position)) {
            notifyDataSetChanged();
            showSnackbar(activity.getResources().getString(R.string.oops_try_again));
        } else {
            fullyConsumeIngredient(ingredients.get(position));
        }
    }


    @Override
    public void onItemMovedToLeft(int position) {
        //partly consume
        if (arrayListNotContainsPosition(position)) {
            showSnackbar(activity.getResources().getString(R.string.oops_try_again));
        } else {
            DialogFragment dialog = ConsumeIngredientDialogFragment.newInstance(ingredients.get(position).id);
            dialog.show(fragmentManager, "ConsumeIngredientDialogFragment");
        }


    }

    private void fullyConsumeIngredient(Ingredient ingredient) {
        int calories = (int) (ingredient.caloriesInDistinct * (ingredient.fullAmount / ingredient.amountOfDistinct));
        Consumed consumed = new Consumed(ingredient.name, calories, ingredient.fullAmount + " " + weightTypes[ingredient.weightType], new Date().getTime());
        ingredient.stageType = IngredientStageType.Consumed.ordinal();

        AsyncTask.execute(() -> {
            RoomDatabaseSource.getInstance(activity).consumedDao().insert(consumed);
            FirestoreSource.getInstance().ingredientDao.update(ingredient);
            showSnackbar(activity.getResources().getString(R.string.consumed));
        });

    }

    private void showSnackbar(String text) {
        Snackbar.make(activity.findViewById(R.id.fridge_recycler_view), text, Snackbar.LENGTH_LONG)
                .show();
    }

    private boolean arrayListNotContainsPosition(int position) {
        return ingredients.size() <= position || ingredients.get(position) == null;
    }
}