package com.kawika.smart_survey.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.alerts.AlertDialogCustom;
import com.kawika.smart_survey.database.CheckedResultsTableQueries;
import com.kawika.smart_survey.database.CurrentlyPlayingAnswersTableQueries;
import com.kawika.smart_survey.database.CurrentlyPlayingMainTableQueries;
import com.kawika.smart_survey.models.CurrentlyPlayingAnswersSqliteModel;
import com.kawika.smart_survey.models.CurrentlyPlayingMainSqliteModel;
import com.kawika.smart_survey.models.GetQuestionsModel;
import com.kawika.smart_survey.utils.PlaySound;
import com.kawika.smart_survey.api_services.RetrofitService;
import com.kawika.smart_survey.application.SmartSurveyApplication;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.database.CategoriesTableQueries;
import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.kawika.smart_survey.models.PlayedLevelsModel;
import com.kawika.smart_survey.networkCheck.NetworkCheck;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.utils.CustomProgress;
import com.kawika.smart_survey.utils.LocaleManager;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kawika.smart_survey.config.AppConfiguration.BEGINNER_TYPE;
import static com.kawika.smart_survey.config.AppConfiguration.EXPERT_TYPE;
import static com.kawika.smart_survey.config.AppConfiguration.INTERMEDIATE_TYPE;
import static com.kawika.smart_survey.config.AppConfiguration.NORMAL;
import static com.kawika.smart_survey.database.CheckedResultsTableQueries.checkedResultsTableQueries;
import static com.kawika.smart_survey.database.CurrentlyPlayingAnswersTableQueries.currentlyPlayingAnswersTableQueries;
import static com.kawika.smart_survey.database.CurrentlyPlayingMainTableQueries.currentlyPlayingMainTableQueries;

/**
 * Created by senthiljs on 09/02/18.
 */

public class QuizTypeSelectActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private CardView beginnerCardView, intermediateCardView, expertCardView;
    private int delayDuration = 300;
    private int fadeInDuration = 700;
    private int selectedCategoryId;
    private AppPreferences appPreferences;
    private int stepIdToPlay = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_type_select_activity);

        appPreferences = AppPreferences.getInstance(this, AppConfiguration.SMART_SURVEY_PREFS);

        Intent myIntent = getIntent();
        selectedCategoryId = myIntent.getIntExtra("selectedCategoryId", 0);

        beginnerCardView = findViewById(R.id.beginnerCardView);
        intermediateCardView = findViewById(R.id.intermediateCardView);
        expertCardView = findViewById(R.id.expertCardView);

        beginnerCardView.setOnClickListener(this);
        intermediateCardView.setOnClickListener(this);
        expertCardView.setOnClickListener(this);

        showDropDown();

        setData();

    }

    private void setData() {
        CategoriesTableQueries followedTopicsTableQueries = CategoriesTableQueries.sharedInstance(QuizTypeSelectActivity.this);
        CategoriesSqliteModel courseDetailList = followedTopicsTableQueries.getSelectedCategoryBasedById(selectedCategoryId);
        if (courseDetailList != null) {
            ImageView selectedCategoryImageView = findViewById(R.id.selectedCategoryImageView);
            TextView categoryTextView = findViewById(R.id.categoryTextView);
            Picasso.with(QuizTypeSelectActivity.this)
                    .load(courseDetailList.getImage_path())
                    .placeholder(R.drawable.no_resource)
                    .error(R.drawable.no_resource)
                    .into(selectedCategoryImageView);

            categoryTextView.setText(courseDetailList.getCategory_name());

        }
    }

    private void showDropDown() {
        beginnerCardView.setVisibility(View.INVISIBLE);
        intermediateCardView.setVisibility(View.INVISIBLE);
        expertCardView.setVisibility(View.INVISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            beginnerCardView.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeInDown)
                    .duration(fadeInDuration)
                    .playOn(beginnerCardView);
            Handler handler12 = new Handler();
            handler12.postDelayed(() -> {
                intermediateCardView.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeInDown)
                        .duration(fadeInDuration)
                        .playOn(intermediateCardView);
                Handler handler1 = new Handler();
                handler1.postDelayed(() -> {
                    expertCardView.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeInDown)
                            .duration(fadeInDuration)
                            .playOn(expertCardView);
                }, delayDuration);
            }, delayDuration);
        }, 100);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.beginnerCardView) {
            PlaySound.buttonClickSound(this, R.raw.button_click_sound);
            getLevel(BEGINNER_TYPE);
        } else if (view.getId() == R.id.intermediateCardView) {
            PlaySound.buttonClickSound(this, R.raw.button_click_sound);
            getLevel(INTERMEDIATE_TYPE);
        } else if (view.getId() == R.id.expertCardView) {
            PlaySound.buttonClickSound(this, R.raw.button_click_sound);
            getLevel(EXPERT_TYPE);
        }
    }

    private void startActivityWithSelectedCategory(int player_type) {
        Intent quiz_start_intent = new Intent(QuizTypeSelectActivity.this, QuizStartActivity.class);
        quiz_start_intent.putExtra("selectedCategoryId", selectedCategoryId);
        quiz_start_intent.putExtra("playerType", player_type);
        quiz_start_intent.putExtra("stepIdToPlay", stepIdToPlay);
        quiz_start_intent.putExtra("gameType", NORMAL);
        startActivity(quiz_start_intent);
        finish();
    }

    private void getLevel(int playerType) {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            AlertDialogCustom.noInternetAlertDialog(this);
            return;
        }
        final CustomProgress progressDialog = new CustomProgress(this);
        progressDialog.show();

        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();

        System.out.println("appPreferences.getData(AppConfiguration.MY_TOKEN) +\"--\"+ = " + appPreferences.getData(AppConfiguration.MY_TOKEN) + "--" +appPreferences.getData(AppConfiguration.USER_ID)
        +"--"+selectedCategoryId+ "--"+ playerType);

        Call<PlayedLevelsModel> categoriesCall = retrofitService.getLevel(appPreferences.getData(AppConfiguration.MY_TOKEN), appPreferences.getData(AppConfiguration.USER_ID), selectedCategoryId,
                playerType);
        categoriesCall.enqueue(new Callback<PlayedLevelsModel>() {
            @Override
            public void onResponse(Call<PlayedLevelsModel> call, Response<PlayedLevelsModel> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    System.out.println("response =getStatus() " + response.body().getStatus()+"--"+response.body().getMessage());
                    if (response.body().getStatus() == 6000) {
//                        for (PlayedLevelsModel.DataBean dataBean : response.body().getData()) {
//                            if (dataBean.getStep_complete() == 0) {
//                                stepIdToPlay = dataBean.getStep_id();
//                                break;
//                            }
//                        }
//
//                        if (stepIdToPlay == -1) {
//                            AlertDialogCustom.commonAlertDialog(QuizTypeSelectActivity.this, getString(R.string.user_completed_all_levels));
//                        } else {
//                            if (playerType == BEGINNER_TYPE) {
//                                startActivityWithSelectedCategory(BEGINNER_TYPE);
//                            } else if (playerType == INTERMEDIATE_TYPE) {
//                                startActivityWithSelectedCategory(INTERMEDIATE_TYPE);
//                            } else {
//                                startActivityWithSelectedCategory(EXPERT_TYPE);
//                            }
//                        }

                        if (response.body().getData().get(0).getStep_complete() == 0){
//                            if (playerType == BEGINNER_TYPE) {
//                                startActivityWithSelectedCategory(BEGINNER_TYPE);
//                            } else if (playerType == INTERMEDIATE_TYPE) {
//                                startActivityWithSelectedCategory(INTERMEDIATE_TYPE);
//                            } else {
//                                startActivityWithSelectedCategory(EXPERT_TYPE);
//                            }
                             checkedResultsTableQueries = CheckedResultsTableQueries.sharedInstance(getApplicationContext());
                                checkedResultsTableQueries.deleteAllCheckedResults();
                                AppPreferences appPreferences = AppPreferences.getInstance(QuizTypeSelectActivity.this, AppConfiguration.SMART_SURVEY_PREFS);
                                getQuestions(appPreferences.getData(AppConfiguration.MY_TOKEN), appPreferences.getData(AppConfiguration.USER_ID),
                                        selectedCategoryId, playerType, response.body().getData().get(0).getStep_id());

                        }else{
                            AlertDialogCustom.commonAlertDialog(QuizTypeSelectActivity.this, getString(R.string.user_completed_all_levels));
                        }

                    } else if (response.body().getStatus() == 6004) {
                        AlertDialogCustom.sessionExpiredDialog(QuizTypeSelectActivity.this);
                    } else {
                        AlertDialogCustom.commonAlertDialog(QuizTypeSelectActivity.this, response.body().getMessage());

                    }
                }

            }

            @Override
            public void onFailure(Call<PlayedLevelsModel> call, Throwable t) {
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(QuizTypeSelectActivity.this, getString(R.string.went_wrong_wait));
            }

        });
    }

    private void getQuestions(String myToken, String userId, int selectedCategoryId, int playerType, int stepId) {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            AlertDialogCustom.noInternetAlertDialog(this);
            return;
        }
        final CustomProgress progressDialog = new CustomProgress(this);
        progressDialog.show();

        System.out.println("myToken = " + myToken+"--"+userId+"--"+selectedCategoryId+"--"+playerType+"--"+stepId);
        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();

        Call<GetQuestionsModel> categoriesCall = retrofitService.getQuestions(myToken, userId, selectedCategoryId,
                playerType, stepId);
        categoriesCall.enqueue(new Callback<GetQuestionsModel>() {
            @Override
            public void onResponse(Call<GetQuestionsModel> call, Response<GetQuestionsModel> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 6000) {

                        currentlyPlayingMainTableQueries = CurrentlyPlayingMainTableQueries.sharedInstance(QuizTypeSelectActivity.this);
                        currentlyPlayingMainTableQueries.deleteAllCurrentlyPlayingMain();

                        currentlyPlayingAnswersTableQueries = CurrentlyPlayingAnswersTableQueries.sharedInstance(QuizTypeSelectActivity.this);
                        currentlyPlayingAnswersTableQueries.deleteAllCurrentlyPlayingAnswers();

                        for (int i = 0; i < response.body().getData().size(); i++) {
                            final CurrentlyPlayingMainSqliteModel currentlyPlayingMainSqliteModel = new CurrentlyPlayingMainSqliteModel();
                            currentlyPlayingMainSqliteModel.setSteps(response.body().getData().get(i).getSteps());
                            currentlyPlayingMainSqliteModel.setLevel(response.body().getData().get(i).getLevel());
                            currentlyPlayingMainSqliteModel.setCategory_id(response.body().getData().get(i).getEn_category_id());
                            currentlyPlayingMainSqliteModel.setCategory(response.body().getData().get(i).getCategory());
                            currentlyPlayingMainSqliteModel.setQuestion(response.body().getData().get(i).getQuestion());
                            currentlyPlayingMainSqliteModel.setQuestion_id(response.body().getData().get(i).getQuestion_id());
                            currentlyPlayingMainSqliteModel.setExplanation(response.body().getData().get(i).getExplanation());

                            if (!currentlyPlayingMainTableQueries.insertCurrentlyPlayingMain(currentlyPlayingMainSqliteModel))
                                Toast.makeText(QuizTypeSelectActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();

                        }
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            for (int j = 0; j < response.body().getData().get(i).getAnswers().size(); j++) {
                                final CurrentlyPlayingAnswersSqliteModel currentlyPlayingAnswersSqliteModel = new CurrentlyPlayingAnswersSqliteModel();
                                currentlyPlayingAnswersSqliteModel.setSteps(response.body().getData().get(i).getSteps());
                                currentlyPlayingAnswersSqliteModel.setQuestion_id(response.body().getData().get(i).getQuestion_id());
                                currentlyPlayingAnswersSqliteModel.setAnswer_id(response.body().getData().get(i).getAnswers().get(j).getAnswer_id());
                                currentlyPlayingAnswersSqliteModel.setAnswer(response.body().getData().get(i).getAnswers().get(j).getAnswer());
                                System.out.println("response.body().getData().get(i).getAnswers().get(j).getAnswer() = " + response.body().getData().get(i).getAnswers().get(j).getAnswer());
                                currentlyPlayingAnswersSqliteModel.setCorrect_answer(response.body().getData().get(i).getAnswers().get(j).getCorrect_answer());

                                if (!currentlyPlayingAnswersTableQueries.insertCurrentlyPlayingAnswers(currentlyPlayingAnswersSqliteModel))
                                    Toast.makeText(QuizTypeSelectActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();

                            }
                        }
                        Intent quiz_intent = new Intent(QuizTypeSelectActivity.this, QuizActivity.class);
                        quiz_intent.putExtra("gameType", NORMAL);
                        startActivity(quiz_intent);
//                        finish();

                    } else if (response.body().getStatus() == 6004){
                        AlertDialogCustom.sessionExpiredDialog(QuizTypeSelectActivity.this);
                    } else {
                        AlertDialogCustom.commonAlertDialog(QuizTypeSelectActivity.this, response.body().getMessage());

                    }
                }

            }

            @Override
            public void onFailure(Call<GetQuestionsModel> call, Throwable t) {
                System.out.println("t = " + t.getMessage());
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(QuizTypeSelectActivity.this, getString(R.string.went_wrong_wait));
            }

        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(QuizTypeSelectActivity.this, HomeActivity.class));
        finish();
    }
}
