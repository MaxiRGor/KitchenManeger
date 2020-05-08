package com.distinct.kitchenmanager.element_behaviour;

import java.text.DecimalFormat;

public class FullAmountFormatter {
    public static String geFormattedString(float number) {
        if (number % (int) number == 0)
            return String.valueOf((int) number);
        else {
            DecimalFormat decimalFormat = new DecimalFormat(".##");
            return decimalFormat.format(number);
        }
    }
}
