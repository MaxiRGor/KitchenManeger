package com.distinct.kitchenmanager.ui.shopping_list;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.element_behaviour.ItemTouchHelper.ItemTouchHelperAdapter;
import com.distinct.kitchenmanager.model.room.entity.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {

    private Activity activity;
    private FragmentManager fragmentManager;
    private ShoppingListViewModel shoppingListViewModel;
    private ArrayList<Ingredient> ingredients;
    private String[] weightTypes;

    ShoppingListRecyclerAdapter(Activity context, FragmentManager fragmentManager, ShoppingListViewModel shoppingListViewModel) {
        this.activity = context;
        this.fragmentManager = fragmentManager;
        this.ingredients = new ArrayList<>();
        this.shoppingListViewModel = shoppingListViewModel;
        this.weightTypes = context.getResources().getStringArray(R.array.weight_types);
    }

    void setItems(List<Ingredient> ingredients) {
        if (ingredients != null){
            this.ingredients.clear();
            this.ingredients.addAll(ingredients);
        } else Log.d("a","no items");

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_shopping_list, parent, false);
        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
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
    public void onItemMoveVertically(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemMovedToRight(int position) {
/*        mItems.remove(position);
        notifyItemRemoved(position);*/
    }

    @Override
    public void onItemMovedToLeft(int position) {

    }
}