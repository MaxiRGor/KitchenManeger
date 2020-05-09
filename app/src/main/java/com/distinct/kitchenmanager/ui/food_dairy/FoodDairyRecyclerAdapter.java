package com.distinct.kitchenmanager.ui.food_dairy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.model.database.entity.Consumed;

import java.util.ArrayList;
import java.util.List;

public class FoodDairyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Consumed> consumedItems;

    FoodDairyRecyclerAdapter(Context context, List<Consumed> consumedItems) {
        this.context = context;
        this.consumedItems = new ArrayList<>();
        this.consumedItems.addAll(consumedItems);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_consumed, parent, false);
        return new FoodDairyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FoodDairyViewHolder) holder).bind(consumedItems.get(position));
    }

    @Override
    public int getItemCount() {
        return consumedItems.size();
    }
}
