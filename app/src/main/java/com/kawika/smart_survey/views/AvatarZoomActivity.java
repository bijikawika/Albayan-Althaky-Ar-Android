package com.kawika.smart_survey.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.kawika.smart_survey.R;
import com.kawika.smart_survey.utils.LocaleManager;

import static com.kawika.smart_survey.application.SmartSurveyApplication.smartSurveyApplication;


public class AvatarZoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avatar_zoom_activity);
        Bitmap bitmap=smartSurveyApplication.getBitmap();
        if (bitmap != null) {
            ImageView previewImageView = findViewById(R.id.previewImageView);
            previewImageView.setImageBitmap(bitmap);
        }
    }

    //language wrapper
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }
}
