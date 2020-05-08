package com.distinct.kitchenmanager.ui.food_dairy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.element_behaviour.DateFormatter;
import com.distinct.kitchenmanager.model.room.entity.Consumed;
import com.distinct.kitchenmanager.ui.dialogs.date_pick.DatePickerFragment;
import com.distinct.kitchenmanager.ui.dialogs.date_pick.OnDatePickedListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;

public class FoodDairyFragment extends Fragment implements OnDatePickedListener {

    private FoodDairyViewModel foodDairyViewModel;
    private RecyclerView recyclerView;
    private View root;
    private TextView dateTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foodDairyViewModel = new ViewModelProvider(this).get(FoodDairyViewModel.class);
        root = inflater.inflate(R.layout.fragment_food_dairy, container, false);
        recyclerView = root.findViewById(R.id.food_dairy_recycler_view);
        dateTextView = root.findViewById(R.id.date_text_view);
        dateTextView.setOnClickListener(view -> showDatePickerDialog());
        dateTextView.setText(DateFormatter.getStringFromDateTime(Calendar.getInstance().getTime().getTime()));
        foodDairyViewModel.getConsumedItems().observe(getViewLifecycleOwner(), this::setRecyclerView);
        return root;
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        foodDairyViewModel.setPickedDate(calendar);
        dateTextView.setText(DateFormatter.getStringFromDateTime(calendar.getTime().getTime()));
    }


    private void showDatePickerDialog() {
        DialogFragment fragment = DatePickerFragment.newInstance(this, null);
        fragment.show(getChildFragmentManager(), "datePicker");
    }


    private void setRecyclerView(List<Consumed> consumedList) {
        if (getActivity() != null) {
            if (consumedList.size() == 0)
                Snackbar.make(root, getString(R.string.nothing_consumed_that_day), Snackbar.LENGTH_LONG).show();

            FoodDairyRecyclerAdapter foodDairyRecyclerAdapter = new FoodDairyRecyclerAdapter(getContext(), consumedList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(foodDairyRecyclerAdapter);

        }
    }


}
