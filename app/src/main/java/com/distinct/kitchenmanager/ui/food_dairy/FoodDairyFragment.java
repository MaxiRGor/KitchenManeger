package com.distinct.kitchenmanager.ui.food_dairy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.distinct.kitchenmanager.R;

public class FoodDairyFragment extends Fragment {

    private FoodDairyViewModel foodDairyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foodDairyViewModel =
                ViewModelProviders.of(this).get(FoodDairyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_food_dairy, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        foodDairyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
