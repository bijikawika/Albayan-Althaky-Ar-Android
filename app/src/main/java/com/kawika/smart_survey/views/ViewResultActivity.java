package com.kawika.smart_survey.views;
/*
 * Created by akhil on 13/03/18.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kawika.smart_survey.R;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.utils.DigitsLocalization;
import com.kawika.smart_survey.utils.LocaleManager;

public class ViewResultActivity extends AppCompatActivity implements View.OnClickListener {

    private int  currentLevelId, totalMark;
    private TextView currentLevelTextView, scoreTextView;
    private ImageView quizCloseImageView;
    private LinearLayout currentLevelLinearLayout;
    private CardView quickChallengeCardView;
    private AppPreferences appPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result);

        appPreferences = AppPreferences.getInstance(ViewResultActivity.this, AppConfiguration.SMART_SURVEY_PREFS);

        Intent myIntent = getIntent();
        Bundle extras = myIntent.getExtras();
        if (extras == null) {
            //without data activity is closing
            finish();
            return;
        }

        currentLevelId = myIntent.getIntExtra("currentLevelId", 0);
        totalMark = myIntent.getIntExtra("totalMark", 0);

        currentLevelTextView = findViewById(R.id.currentLevelTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        quizCloseImageView = findViewById(R.id.quizCloseImageView);
        currentLevelLinearLayout = findViewById(R.id.currentLevelLinearLayout);
        quickChallengeCardView = findViewById(R.id.quickChallengeCardView);

        quizCloseImageView.setOnClickListener(this);

        setData();

    }

    private void setData(){
        if (currentLevelId == -1){
            quickChallengeCardView.setVisibility(View.VISIBLE);
            currentLevelLinearLayout.setVisibility(View.GONE);
        }else {
            quickChallengeCardView.setVisibility(View.GONE);
            currentLevelLinearLayout.setVisibility(View.VISIBLE);
        }
        if (appPreferences.getInt(AppConfiguration.LANGUAGE_ID) == 1){
            currentLevelTextView.setText(String.valueOf(currentLevelId));
            scoreTextView.setText(String.valueOf(totalMark));
        }else{
            currentLevelTextView.setText(DigitsLocalization.convertEnglishDigitsToArabic(String.valueOf(currentLevelId)));
            scoreTextView.setText(DigitsLocalization.convertEnglishDigitsToArabic(String.valueOf(totalMark)));
        }

    }
    //language wrapper
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.quizCloseImageView){
            finish();
        }
    }
}
