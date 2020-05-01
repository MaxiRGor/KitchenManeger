package com.distinct.kitchenmanager.ui.fragment_with_search_view;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.distinct.kitchenmanager.R;

public abstract class FragmentWithSearchView extends Fragment implements OnSearchListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getString(R.string.ingredient));
        searchView.setOnQueryTextFocusChangeListener((view, b) -> setInputMethod(b, view.findFocus(), view));

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
                    searchOnNullFilter();
                } else {
                    searchByIngredientName(searchText);
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

    @Override
    public abstract void searchOnNullFilter();

    @Override
    public abstract void searchByIngredientName(String searchText);
}
