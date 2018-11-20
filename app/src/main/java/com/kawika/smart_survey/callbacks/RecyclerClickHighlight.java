package com.kawika.smart_survey.callbacks;

import android.widget.RelativeLayout;

/**
 * Created by senthiljs on 27/02/18.
 */


public interface RecyclerClickHighlight {
    void onRowClick(RelativeLayout view, int position, int category_id, String topicUrl, String topicName);

    void languageChanged(String category_name);
}
