package com.distinct.kitchenmanager.ui.fridge;

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
import com.distinct.kitchenmanager.ui.shopping_list.ShoppingListViewHolder;
import com.distinct.kitchenmanager.ui.shopping_list.ShoppingListViewModel;

import java.util.ArrayList;
import java.util.List;

public class FridgeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {

    private Activity activity;
    private FragmentManager fragmentManager;
    private FridgeViewModel fridgeViewModel;
    private ArrayList<Ingredient> ingredients;
    private String[] weightTypes;

    FridgeRecyclerAdapter(Activity context, FragmentManager fragmentManager, FridgeViewModel fridgeViewModel) {
        this.activity = context;
        this.fragmentManager = fragmentManager;
        this.ingredients = new ArrayList<>();
        this.fridgeViewModel = fridgeViewModel;
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


/*    @Override
    public void onItemMoveVertically(int fromPosition, int toPosition) {

    }*/

    @Override
    public void onItemMovedToRight(int position) {
/*        mItems.remove(position);
        notifyItemRemoved(position);*/
    }

    @Override
    public void onItemMovedToLeft(int position) {

    }
}