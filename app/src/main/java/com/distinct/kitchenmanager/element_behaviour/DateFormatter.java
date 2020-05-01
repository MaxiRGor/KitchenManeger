package com.distinct.kitchenmanager.element_behaviour;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    public static String getStringFromDateTime(long time) {
        Date date = new Date();
        date.setTime(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd / MM / yyyy", Locale.US);
        return dateFormat.format(time);
    }
}
