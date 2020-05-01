package com.distinct.kitchenmanager.ui.fridge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.element_behaviour.ItemTouchHelper.SimpleItemTouchHelperCallback;
import com.distinct.kitchenmanager.model.room.entity.Ingredient;
import com.distinct.kitchenmanager.ui.fragment_with_search_view.FragmentWithSearchView;
import com.distinct.kitchenmanager.ui.shopping_list.ShoppingListRecyclerAdapter;

import java.util.List;

public class FridgeFragment extends FragmentWithSearchView {

    private FridgeViewModel fridgeViewModel;

    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fridgeViewModel = new ViewModelProvider(this).get(FridgeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_fridge, container, false);
        recyclerView = root.findViewById(R.id.fridge_recycler_view);

      //  fridgeViewModel.


        return root;
    }

    @Override
    public void searchOnNullFilter() {

    }

    @Override
    public void searchByIngredientName(String searchText) {

    }

/*    private Observer<List<Ingredient>> shoppingListItemsObserver = shoppingListItems -> {
        if (getActivity() != null) {
            ShoppingListRecyclerAdapter shoppingListRecyclerAdapter = new ShoppingListRecyclerAdapter(getActivity(), shoppingListItems, getChildFragmentManager(), shoppingListViewModel);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(shoppingListRecyclerAdapter);

            ItemTouchHelper.Callback callback =
                    new SimpleItemTouchHelperCallback(shoppingListRecyclerAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recyclerView);
        }
    };*/
}
