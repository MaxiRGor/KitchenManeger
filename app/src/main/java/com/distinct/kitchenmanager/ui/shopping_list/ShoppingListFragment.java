package com.distinct.kitchenmanager.ui.shopping_list;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.element_behaviour.ItemTouchHelper.SimpleItemTouchHelperCallback;
import com.distinct.kitchenmanager.model.room.entity.Ingredient;
import com.distinct.kitchenmanager.ui.dialogs.ChangeIngredientDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ShoppingListFragment extends Fragment {

    private ShoppingListViewModel shoppingListViewModel;
    private RecyclerView recyclerView;
    private ShoppingListRecyclerAdapter shoppingListRecyclerAdapter;
    private FloatingActionButton addIngredientButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);

        View root = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        recyclerView = root.findViewById(R.id.shopping_list_recycler_view);
        addIngredientButton = root.findViewById(R.id.add_ingredient_floating_action_button);
        addIngredientButton.setOnClickListener(openAddIngredientDialog);
        shoppingListViewModel.getShoppingListItemsToShow().observe(getViewLifecycleOwner(), shoppingListItemsObserver);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getString(R.string.ingredient));

        searchView.setOnQueryTextFocusChangeListener((view, b) -> {
            setInputMethod(b, view.findFocus(), view);

        });

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                searchView.setQuery("", true);
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                String newFilter = !TextUtils.isEmpty(searchText) ? searchText : null;
                if (newFilter == null) {
                    shoppingListViewModel.getAllShoppingListItems();
                } else {
                    shoppingListViewModel.searchByIngredientName(searchText);
                }

                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setInputMethod(boolean b, View focusView, View view) {
        if (getActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                if (b)
                    imm.showSoftInput(focusView, 0);
                else imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }


    private View.OnClickListener openAddIngredientDialog = view -> {
        showDialog();
    };


    private void showDialog() {

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getChildFragmentManager().findFragmentByTag("AddIngredientDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment dialog = ChangeIngredientDialogFragment.newInstance(-1);
        dialog.show(ft, "AddIngredientDialog");
    }

    private Observer<List<Ingredient>> shoppingListItemsObserver = shoppingListItems -> {
        if (getActivity() != null) {
            shoppingListRecyclerAdapter = new ShoppingListRecyclerAdapter(getActivity(), shoppingListItems, getChildFragmentManager(), shoppingListViewModel);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(shoppingListRecyclerAdapter);

            ItemTouchHelper.Callback callback =
                    new SimpleItemTouchHelperCallback(shoppingListRecyclerAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recyclerView);


        }
    };

}
