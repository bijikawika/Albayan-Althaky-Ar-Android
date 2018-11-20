package com.kawika.smart_survey.utils;

/**
 * Created by senthiljs on 10/08/17.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.kawika.smart_survey.R;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.BallSpinFadeLoaderIndicator;


/**
 * Created by zakapps on 4/8/16.
 */
public class CustomProgress {
    Context context;
    android.app.Dialog progressDialog;

    public CustomProgress(Context context){
        this.context = context;
    }

    public void show(){
        progressDialog = new android.app.Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();

        TextView progressTextView = progressDialog.findViewById(R.id.progressTextView);
        progressTextView.setVisibility(View.GONE);
        AVLoadingIndicatorView progressLoading = (AVLoadingIndicatorView) progressDialog.findViewById(R.id.progressLoading);
        BallSpinFadeLoaderIndicator ballSpinFadeLoaderIndicator = new BallSpinFadeLoaderIndicator();
        progressLoading.setIndicator(ballSpinFadeLoaderIndicator);
        progressLoading.smoothToShow();
    }

    public void show(String title){
        progressDialog = new android.app.Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();

        TextView progressTextView = (TextView) progressDialog.findViewById(R.id.progressTextView);
        progressTextView.setVisibility(View.VISIBLE);
        progressTextView.setText(title);
        AVLoadingIndicatorView progressLoading = (AVLoadingIndicatorView) progressDialog.findViewById(R.id.progressLoading);
        BallSpinFadeLoaderIndicator ballSpinFadeLoaderIndicator = new BallSpinFadeLoaderIndicator();
        progressLoading.setIndicator(ballSpinFadeLoaderIndicator);
        progressLoading.smoothToShow();
    }

    public void dismiss(){
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}
