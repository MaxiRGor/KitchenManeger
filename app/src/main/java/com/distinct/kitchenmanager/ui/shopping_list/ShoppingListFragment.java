package com.distinct.kitchenmanager.ui.shopping_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.element_behaviour.ItemTouchHelper.SimpleItemTouchHelperCallback;
import com.distinct.kitchenmanager.model.database.entity.Ingredient;
import com.distinct.kitchenmanager.ui.fragment_with_search_view.FragmentWithSearchView;

import java.util.List;

public class ShoppingListFragment extends FragmentWithSearchView {

    private ShoppingListViewModel shoppingListViewModel;
    private RecyclerView recyclerView;
    private ItemTouchHelper touchHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        recyclerView = root.findViewById(R.id.shopping_list_recycler_view);
        shoppingListViewModel.getItemsToShow().observe(getViewLifecycleOwner(), shoppingListItemsObserver);
        return root;
    }

    @Override
    public void searchOnNullFilter() {
        shoppingListViewModel.showAllItems();
    }

    @Override
    public void searchByIngredientName(String searchText) {
        shoppingListViewModel.searchByIngredientName(searchText);
    }

    private Observer<List<Ingredient>> shoppingListItemsObserver = shoppingListItems -> {
        if (getActivity() != null) {
            ShoppingListRecyclerAdapter shoppingListRecyclerAdapter = new ShoppingListRecyclerAdapter(getActivity(), shoppingListItems, getChildFragmentManager(), shoppingListViewModel);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(shoppingListRecyclerAdapter);
            shoppingListRecyclerAdapter.notifyDataSetChanged();

            if(touchHelper!=null)
                touchHelper.attachToRecyclerView(null);

            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(shoppingListRecyclerAdapter);
            touchHelper = new ItemTouchHelper(callback);

            touchHelper.attachToRecyclerView(recyclerView);
        }


    };

}
