package com.kawika.smart_survey.callbacks;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by senthiljs on 27/02/18.
 */


public interface OptionClickListener {
    void onOptionClick(int position, View throwView, View optionView, RelativeLayout validRelativeLayout, RelativeLayout invalidRelativeLayout,
                       TextView optionTextView);
}
