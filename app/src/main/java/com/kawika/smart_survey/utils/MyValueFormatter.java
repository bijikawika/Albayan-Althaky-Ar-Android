package com.kawika.smart_survey.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by senthiljs on 07/03/18.
 */

public class MyValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,###"); // use no decimals
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

        // write your logic here
        if(value < 10) return "";

        System.out.println("mFormat.format(value) + \"%\" = " + mFormat.format(value) + "%");
        return mFormat.format(value) + "%"; // in case you want to add percent
    }
}