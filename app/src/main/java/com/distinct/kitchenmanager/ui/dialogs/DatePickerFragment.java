package com.distinct.kitchenmanager.ui.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private OnDatePickedListener onDatePickedListener;
    private int year;
    private int month;
    private int day;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        if (year == 0)
            year = c.get(Calendar.YEAR);
        if (month == 0)
            month = c.get(Calendar.MONTH);
        if (day == 0)
            day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    static DatePickerFragment newInstance(DialogFragment dialogFragment, Calendar calendar) {

        Bundle args = new Bundle();

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        fragment.onDatePickedListener = (OnDatePickedListener) dialogFragment;
        if (calendar != null) {
            fragment.day = calendar.get(Calendar.DATE);
            fragment.month = calendar.get(Calendar.MONTH);
            fragment.year = calendar.get(Calendar.YEAR);
        }
        return fragment;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        onDatePickedListener.onDateSet(year, month, day);
    }
}