package com.distinct.kitchenmanager.ui.fridge;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.distinct.kitchenmanager.ApplicationContextSingleton;
import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.element_behaviour.ItemTouchHelper.SimpleItemTouchHelperCallback;
import com.distinct.kitchenmanager.model.database.entity.Ingredient;
import com.distinct.kitchenmanager.ui.fragment_with_search_view.FragmentWithSearchView;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class FridgeFragment extends FragmentWithSearchView {

    private FridgeViewModel fridgeViewModel;
    private RecyclerView recyclerView;
    private ItemTouchHelper touchHelper;
    private LinearLayout categoriesChipsLinearLayout;
    private ArrayList<Chip> chips;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fridgeViewModel = new ViewModelProvider(this).get(FridgeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_fridge, container, false);
        recyclerView = root.findViewById(R.id.fridge_recycler_view);
        categoriesChipsLinearLayout = root.findViewById(R.id.categories_chips_linear_layout);
        addChips();
        fridgeViewModel.getItemsToShow().observe(getViewLifecycleOwner(), fridgeItemsObserver);
        return root;
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
                if (b){
                    for(Chip other : chips){
                        if(chip!=other)
                        other.setChecked(false);
                    }
                    fridgeViewModel.searchByCategory(chip.getText().toString());
                }

                else{
                    fridgeViewModel.showAllItems();
                }

            });
        }
    }

    @Override
    public void searchOnNullFilter() {
        fridgeViewModel.showAllItems();
    }

    @Override
    public void searchByIngredientName(String searchText) {
        fridgeViewModel.searchByIngredientName(searchText);
    }

    private Observer<List<Ingredient>> fridgeItemsObserver = fridgeItems -> {
        if (getActivity() != null) {
            FridgeRecyclerAdapter fridgeRecyclerAdapter = new FridgeRecyclerAdapter(getActivity(), fridgeItems, getChildFragmentManager(), fridgeViewModel);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(fridgeRecyclerAdapter);

            if (touchHelper != null)
                touchHelper.attachToRecyclerView(null);

            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(fridgeRecyclerAdapter);
            touchHelper = new ItemTouchHelper(callback);

            touchHelper.attachToRecyclerView(recyclerView);
        }
    };
}
