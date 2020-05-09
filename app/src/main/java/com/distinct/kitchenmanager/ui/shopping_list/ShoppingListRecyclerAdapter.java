package com.distinct.kitchenmanager.ui.shopping_list;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.element_behaviour.ItemTouchHelper.ItemTouchHelperAdapter;
import com.distinct.kitchenmanager.model.database.database.FirestoreSource;
import com.distinct.kitchenmanager.model.database.entity.Ingredient;
import com.distinct.kitchenmanager.model.enums.IngredientStageType;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {

    private Activity activity;
    private FragmentManager fragmentManager;
    private ShoppingListViewModel shoppingListViewModel;
    private ArrayList<Ingredient> ingredients;
    private String[] weightTypes;

    ShoppingListRecyclerAdapter(Activity context, List<Ingredient> shoppingListItems, FragmentManager fragmentManager, ShoppingListViewModel shoppingListViewModel) {
        this.activity = context;
        this.fragmentManager = fragmentManager;
        this.ingredients = new ArrayList<>();
        this.ingredients.clear();
        if (shoppingListItems != null)
            this.ingredients.addAll(shoppingListItems);
        this.shoppingListViewModel = shoppingListViewModel;
        this.weightTypes = context.getResources().getStringArray(R.array.weight_types);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_shopping_list, parent, false);
        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("asad", "ingredients.size() = " + this.ingredients.size());
        Ingredient ingredient = ingredients.get(position);
        ShoppingListViewHolder viewHolder = (ShoppingListViewHolder) holder;
        viewHolder.fragmentManager = fragmentManager;
        viewHolder.shoppingListViewModel = shoppingListViewModel;
        viewHolder.bind(ingredient, weightTypes[ingredient.weightType]);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }


    @Override
    public void onItemMovedToRight(int position) {
        //delete
        if (arrayListNotContainsPosition(position)) {
            notifyDataSetChanged();
            showSnackbar(activity.getResources().getString(R.string.oops_try_again));
        } else {
            AsyncTask.execute(() -> {
                FirestoreSource.getInstance().ingredientDao.delete(ingredients.get(position));
                showSnackbar(activity.getResources().getString(R.string.deleted));
            });
        }
    }

    @Override
    public void onItemMovedToLeft(int position) {
        Log.d("aaa", "pos = " + position);
        //move to fridge
        if (arrayListNotContainsPosition(position)) {
            showSnackbar(activity.getResources().getString(R.string.oops_try_again));
        } else {
            AsyncTask.execute(() -> {
                Ingredient ingredient = ingredients.get(position);
                ingredient.stageType = IngredientStageType.InFridge.ordinal();
                FirestoreSource.getInstance().ingredientDao.update(ingredient);
                showSnackbar(activity.getResources().getString(R.string.moved_to_fridge));
            });
        }
    }


    private void showSnackbar(String text) {
        Snackbar.make(activity.findViewById(R.id.shopping_list_recycler_view), text, Snackbar.LENGTH_LONG)
                .show();
    }

    private boolean arrayListNotContainsPosition(int position) {
        Log.d("aaa", "ingredients.size() = " + this.ingredients.size());
       // Log.d("aaa", "ingredients.size() = " + ingredients.size());
        return this.ingredients.size() <= position || this.ingredients.get(position) == null;
    }
}