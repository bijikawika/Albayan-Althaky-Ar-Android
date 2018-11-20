package com.kawika.smart_survey.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.alerts.AlertDialogCustom;
import com.kawika.smart_survey.utils.PlaySound;
import com.kawika.smart_survey.api_services.RetrofitService;
import com.kawika.smart_survey.application.SmartSurveyApplication;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.database.CategoriesTableQueries;
import com.kawika.smart_survey.database.UserDataTableQueries;
import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.kawika.smart_survey.models.CheckedResultsSqliteModel;
import com.kawika.smart_survey.models.ScoresSqliteModel;
import com.kawika.smart_survey.models.SubmitResultModel;
import com.kawika.smart_survey.models.SubmitResultsModel;
import com.kawika.smart_survey.networkCheck.NetworkCheck;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.utils.CustomProgress;
import com.kawika.smart_survey.utils.DigitsLocalization;
import com.kawika.smart_survey.utils.LocaleManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kawika.smart_survey.config.AppConfiguration.NORMAL;
import static com.kawika.smart_survey.database.CheckedResultsTableQueries.checkedResultsTableQueries;
import static com.kawika.smart_survey.database.ScoresTableQueries.scoresTableQueries;

/*
 * Created by senthiljs on 12/02/18.
 */

public class YouLoseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView selectedCategoryTextView, currentLevelTextView, userNameTextView, typeTextView,
            scoreTextView;
    private ImageView profileImageView;
    private int selectedCategoryId, currentStepId, totalMark, playerType;
    private Button continueButton, retryButton, viewResultButton;
    private AppPreferences appPreferences;
    private int stepIdToPlay = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lose_result_activity);

        appPreferences = AppPreferences.getInstance(YouLoseActivity.this, AppConfiguration.SMART_SURVEY_PREFS);

        Intent myIntent = getIntent();
        Bundle extras = myIntent.getExtras();
        if (extras == null) {
            //without data activity is closing
            finish();
            return;
        }
        selectedCategoryId = myIntent.getIntExtra("selectedCategoryId", 0);
        currentStepId = myIntent.getIntExtra("currentStepId", 0);
        totalMark = myIntent.getIntExtra("totalMark", 0);
        playerType = myIntent.getIntExtra("playerType", 0);

        System.out.println("totalMark = " + totalMark);

        selectedCategoryTextView = findViewById(R.id.selectedCategoryTextView);
        currentLevelTextView = findViewById(R.id.currentLevelTextView);
        profileImageView = findViewById(R.id.profileImageView);
        userNameTextView = findViewById(R.id.userNameTextView);
        typeTextView = findViewById(R.id.typeTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        continueButton = findViewById(R.id.continueButton);
        retryButton = findViewById(R.id.retryButton);
        viewResultButton = findViewById(R.id.viewResultButton);

        continueButton.setOnClickListener(this);
        retryButton.setOnClickListener(this);
        viewResultButton.setOnClickListener(this);

        setData();

    }

    private void setData() {
        CategoriesTableQueries followedTopicsTableQueries = CategoriesTableQueries.sharedInstance(YouLoseActivity.this);
        CategoriesSqliteModel courseDetailList = followedTopicsTableQueries.getSelectedCategoryBasedById(selectedCategoryId);
        if (courseDetailList != null) {
            selectedCategoryTextView.setText(courseDetailList.getCategory_name());
        }

        if (appPreferences.getInt(AppConfiguration.LANGUAGE_ID) == 1){
            scoreTextView.setText(String.valueOf(totalMark));
            currentLevelTextView.setText(String.valueOf(currentStepId));
        }else{
            scoreTextView.setText(DigitsLocalization.convertEnglishDigitsToArabic(String.valueOf(totalMark)));
            currentLevelTextView.setText(DigitsLocalization.convertEnglishDigitsToArabic(String.valueOf(currentStepId)));
        }

        UserDataTableQueries userDataTableQueries = UserDataTableQueries.sharedInstance(YouLoseActivity.this);
        userNameTextView.setText(userDataTableQueries.getName());
        if (userDataTableQueries.getProfileImagePath() != null) {
            Picasso.with(YouLoseActivity.this)
                    .load(userDataTableQueries.getProfileImagePath())
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(profileImageView);
        }

        if (playerType == 1) {
            typeTextView.setText(R.string.beginner);
        } else if (playerType == 2) {
            typeTextView.setText(R.string.intermediate);
        } else if (playerType == 3) {
            typeTextView.setText(R.string.expert);
        }else{
            typeTextView.setText(R.string.quick_challenge);
            currentLevelTextView.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.continueButton) {
            PlaySound.buttonClickSound(this, R.raw.button_click_sound);
            List<CheckedResultsSqliteModel> checkedResultsSqliteModelList = checkedResultsTableQueries.getAllAnswerResults();
            ScoresSqliteModel scoresSqliteModels = scoresTableQueries.getScoresMain();
            submitResults(jsonForSubmitResult(scoresSqliteModels, checkedResultsSqliteModelList));
        }
        if (view.getId() == R.id.viewResultButton) {
            PlaySound.buttonClickSound(this, R.raw.button_click_sound);

            Intent quiz_start_intent = new Intent(YouLoseActivity.this, ViewResultActivity.class);
            quiz_start_intent.putExtra("currentLevelId", currentStepId);
            quiz_start_intent.putExtra("totalMark", totalMark);
            startActivity(quiz_start_intent);
        }
        if (view.getId() == R.id.retryButton) {
            PlaySound.buttonClickSound(this, R.raw.button_click_sound);

            Intent quiz_start_intent = new Intent(YouLoseActivity.this, QuizStartActivity.class);
            quiz_start_intent.putExtra("selectedCategoryId", selectedCategoryId);
            quiz_start_intent.putExtra("playerType", playerType);
            quiz_start_intent.putExtra("stepIdToPlay", currentStepId);
            quiz_start_intent.putExtra("gameType", NORMAL);
            startActivity(quiz_start_intent);
            finish();
        }


    }

    private void submitResults(SubmitResultModel submitResultModel) {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            System.out.println("networkCheck.isConnectingToInternet() = " + networkCheck.isConnectingToInternet());
            AlertDialogCustom.noInternetAlertDialog(this);
            return;
        }
        final CustomProgress progressDialog = new CustomProgress(YouLoseActivity.this);
        progressDialog.show();

        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();

        Call<SubmitResultsModel> categoriesCall = retrofitService.submitResults(appPreferences.getData(AppConfiguration.MY_TOKEN), submitResultModel);
        categoriesCall.enqueue(new Callback<SubmitResultsModel>() {
            @Override
            public void onResponse(Call<SubmitResultsModel> call, Response<SubmitResultsModel> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 6000) {
//                        for (SubmitResultsModel.DataBean.PlayedBean dataBean : response.body().getData().getPlayed()) {
//                            //complete status checking
//                            if (dataBean.getStep_complete() == 0) {
//                                stepIdToPlay = dataBean.getStep_id();
//                                break;
//                            }
//                        }
//
//                        UserDataTableQueries userDataTableQueries = UserDataTableQueries.sharedInstance(getApplicationContext());
//                        userDataTableQueries.deleteAllUserDetails();
//
//                        SubmitResultsModel loginModel = new SubmitResultsModel();
//                        loginModel.setData(response.body().getData());
//
//                        userDataTableQueries.insertUserDetailsOnQuizComplete(loginModel);
//
//                        userDataTableQueries.updateMark(response.body().getData());
//
//                        System.out.println("stepIdToPlay = " + stepIdToPlay);
//                        if (stepIdToPlay == -1) {
//                            Intent quiz_start_intent = new Intent(YouLoseActivity.this, QuizTypeSelectActivity.class);
//                            quiz_start_intent.putExtra("selectedCategoryId", selectedCategoryId);
//                            startActivity(quiz_start_intent);
//                        } else {
//                            Intent quiz_start_intent = new Intent(YouLoseActivity.this, QuizStartActivity.class);
//                            quiz_start_intent.putExtra("selectedCategoryId", selectedCategoryId);
//                            quiz_start_intent.putExtra("playerType", playerType);
//                            quiz_start_intent.putExtra("stepIdToPlay", stepIdToPlay);
//                            quiz_start_intent.putExtra("gameType", NORMAL);
//                            startActivity(quiz_start_intent);
//                        }
                        Intent quiz_start_intent = new Intent(YouLoseActivity.this, QuizTypeSelectActivity.class);
                        quiz_start_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        quiz_start_intent.putExtra("selectedCategoryId",selectedCategoryId);
                        startActivity(quiz_start_intent);
                        finish();
                    } else if (response.body().getStatus() == 6004) {
                        AlertDialogCustom.sessionExpiredDialog(YouLoseActivity.this);
                    } else {
                        Intent quiz_start_intent = new Intent(YouLoseActivity.this, QuizTypeSelectActivity.class);
                        quiz_start_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        quiz_start_intent.putExtra("selectedCategoryId",selectedCategoryId);
                        startActivity(quiz_start_intent);
                        finish();
                    }
                } else {
                    AlertDialogCustom.commonAlertDialog(YouLoseActivity.this, String.valueOf(response.code()));
                }

            }

            @Override
            public void onFailure(Call<SubmitResultsModel> call, Throwable t) {
                System.out.println("t = " + t.getMessage());
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(YouLoseActivity.this, getString(R.string.went_wrong_wait));
            }

        });
    }

    public SubmitResultModel jsonForSubmitResult(ScoresSqliteModel scoresSqliteModels, List<CheckedResultsSqliteModel> checkedResultsSqliteModelList) {

        try {
            SubmitResultModel submitResultModel = new SubmitResultModel();
            submitResultModel.setCategory_id(scoresSqliteModels.getCategory_id());
            submitResultModel.setFull_scored(scoresSqliteModels.getFull_scored());
            submitResultModel.setLevel_id(scoresSqliteModels.getLevel_id());
            submitResultModel.setScored_mark(scoresSqliteModels.getScored_mark());
            submitResultModel.setStep_id(scoresSqliteModels.getStep_id());
            submitResultModel.setUser_id(appPreferences.getData(AppConfiguration.USER_ID));
            if (scoresSqliteModels.getFull_scored() == 1) {
                submitResultModel.setTotal_mark(scoresSqliteModels.getTotal_mark() + 10);
            } else
                submitResultModel.setTotal_mark(scoresSqliteModels.getTotal_mark());
            submitResultModel.setResult(checkedResultsSqliteModelList);

            System.out.println("response.body() " + new Gson().toJson(submitResultModel).toString());

            return submitResultModel;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    //language wrapper
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

}