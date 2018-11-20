package com.kawika.smart_survey.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.alerts.AlertDialogCustom;
import com.kawika.smart_survey.utils.PlaySound;
import com.kawika.smart_survey.api_services.RetrofitService;
import com.kawika.smart_survey.application.SmartSurveyApplication;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.database.CategoriesTableQueries;
import com.kawika.smart_survey.database.CheckedResultsTableQueries;
import com.kawika.smart_survey.database.CurrentlyPlayingAnswersTableQueries;
import com.kawika.smart_survey.database.CurrentlyPlayingMainTableQueries;
import com.kawika.smart_survey.database.UserDataTableQueries;
import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.kawika.smart_survey.models.CurrentlyPlayingAnswersSqliteModel;
import com.kawika.smart_survey.models.CurrentlyPlayingMainSqliteModel;
import com.kawika.smart_survey.models.GetQuestionsModel;
import com.kawika.smart_survey.networkCheck.NetworkCheck;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.utils.CustomProgress;
import com.kawika.smart_survey.utils.DigitsLocalization;
import com.kawika.smart_survey.utils.LocaleManager;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kawika.smart_survey.config.AppConfiguration.NORMAL;
import static com.kawika.smart_survey.database.CheckedResultsTableQueries.checkedResultsTableQueries;
import static com.kawika.smart_survey.database.CurrentlyPlayingAnswersTableQueries.currentlyPlayingAnswersTableQueries;
import static com.kawika.smart_survey.database.CurrentlyPlayingMainTableQueries.currentlyPlayingMainTableQueries;

/**
 * Created by senthiljs on 09/02/18.
 */

public class QuizStartActivity extends BaseAppCompatActivity {

    private LinearLayout upLinearLayout, downLinearLayout;
    private TextView userNameTextView, levelTitleTextView, myScoreTextView;
    private ImageView profileImageView;
    private int selectedCategoryId;
    private int playerType, stepIdToPlay, gameType;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_start_activity);

        appPreferences = AppPreferences.getInstance(QuizStartActivity.this, AppConfiguration.SMART_SURVEY_PREFS);

        Intent myIntent = getIntent();
        selectedCategoryId = myIntent.getIntExtra("selectedCategoryId", 0);
        playerType = myIntent.getIntExtra("playerType", 0);
        stepIdToPlay = myIntent.getIntExtra("stepIdToPlay", 0);
        gameType = myIntent.getIntExtra("gameType", 0);

        upLinearLayout = findViewById(R.id.upLinearLayout);
        downLinearLayout = findViewById(R.id.downLinearLayout);
        userNameTextView = findViewById(R.id.userNameTextView);
        profileImageView = findViewById(R.id.profileImageView);
        levelTitleTextView = findViewById(R.id.levelTitleTextView);
        myScoreTextView = findViewById(R.id.myScoreTextView);

        upLinearLayout.setVisibility(View.INVISIBLE);
        downLinearLayout.setVisibility(View.INVISIBLE);

        playAnim();

        setData();

        TextView startGameTextView = findViewById(R.id.startGameTextView);
        startGameTextView.setOnClickListener(view -> {
            PlaySound.buttonClickSound(this, R.raw.button_click_sound);
            if (gameType == NORMAL) {
                checkedResultsTableQueries = CheckedResultsTableQueries.sharedInstance(getApplicationContext());
                checkedResultsTableQueries.deleteAllCheckedResults();
                AppPreferences appPreferences = AppPreferences.getInstance(QuizStartActivity.this, AppConfiguration.SMART_SURVEY_PREFS);
                getQuestions(appPreferences.getData(AppConfiguration.MY_TOKEN), appPreferences.getData(AppConfiguration.USER_ID),
                        selectedCategoryId, playerType, stepIdToPlay);
            }else{
                Intent quiz_intent = new Intent(QuizStartActivity.this, QuizActivity.class);
                quiz_intent.putExtra("gameType", gameType);
                startActivity(quiz_intent);
                finish();
            }
        });

    }

    private void playAnim() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                upLinearLayout.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.BounceInDown)
                        .duration(1000)
                        .playOn(upLinearLayout);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        downLinearLayout.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.BounceInUp)
                                .duration(1000)
                                .playOn(downLinearLayout);
                    }
                }, 100);
            }
        }, 100);
    }

    private void setData() {
        CardView quickChallengeCardView = findViewById(R.id.quickChallengeCardView);
        LinearLayout normalLinearLayout = findViewById(R.id.normalLinearLayout);
        if (gameType == NORMAL){
            normalLinearLayout.setVisibility(View.VISIBLE);
            quickChallengeCardView.setVisibility(View.GONE);
            if (appPreferences.getInt(AppConfiguration.LANGUAGE_ID) == 1){
                levelTitleTextView.setText(String.valueOf(stepIdToPlay));
            }else{
                levelTitleTextView.setText(DigitsLocalization.convertEnglishDigitsToArabic(String.valueOf(stepIdToPlay)));
            }
        }else{
            normalLinearLayout.setVisibility(View.GONE);
            quickChallengeCardView.setVisibility(View.VISIBLE);
        }
        ImageView selectedCategoryImageView = findViewById(R.id.selectedCategoryImageView);
        TextView selectedCategoryTextView = findViewById(R.id.selectedCategoryTextView);

        UserDataTableQueries userDataTableQueries = UserDataTableQueries.sharedInstance(QuizStartActivity.this);
        userNameTextView.setText(userDataTableQueries.getName());

        if (appPreferences.getInt(AppConfiguration.LANGUAGE_ID) == 1){
            myScoreTextView.setText(userDataTableQueries.getMyScore());
        }else{
            myScoreTextView.setText(DigitsLocalization.convertEnglishDigitsToArabic(userDataTableQueries.getMyScore()));
        }

        if (userDataTableQueries.getProfileImagePath() != null) {
            Picasso.with(QuizStartActivity.this)
                    .load(userDataTableQueries.getProfileImagePath())
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(profileImageView);

                CategoriesTableQueries followedTopicsTableQueries = CategoriesTableQueries.sharedInstance(QuizStartActivity.this);
                CategoriesSqliteModel courseDetailList = followedTopicsTableQueries.getSelectedCategoryBasedById(selectedCategoryId);
                if (courseDetailList != null) {
                    Picasso.with(QuizStartActivity.this)
                            .load(courseDetailList.getImage_path())
                            .placeholder(R.drawable.no_resource)
                            .error(R.drawable.no_resource)
                            .into(selectedCategoryImageView);

                    selectedCategoryTextView.setText(courseDetailList.getCategory_name());
                }
        }
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

                        currentlyPlayingMainTableQueries = CurrentlyPlayingMainTableQueries.sharedInstance(QuizStartActivity.this);
                        currentlyPlayingMainTableQueries.deleteAllCurrentlyPlayingMain();

                        currentlyPlayingAnswersTableQueries = CurrentlyPlayingAnswersTableQueries.sharedInstance(QuizStartActivity.this);
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
                                Toast.makeText(QuizStartActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();

                        }
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            for (int j = 0; j < response.body().getData().get(i).getAnswers().size(); j++) {
                                final CurrentlyPlayingAnswersSqliteModel currentlyPlayingAnswersSqliteModel = new CurrentlyPlayingAnswersSqliteModel();
                                currentlyPlayingAnswersSqliteModel.setSteps(response.body().getData().get(i).getSteps());
                                currentlyPlayingAnswersSqliteModel.setQuestion_id(response.body().getData().get(i).getQuestion_id());
                                currentlyPlayingAnswersSqliteModel.setAnswer_id(response.body().getData().get(i).getAnswers().get(j).getAnswer_id());
                                currentlyPlayingAnswersSqliteModel.setAnswer(response.body().getData().get(i).getAnswers().get(j).getAnswer());
                                currentlyPlayingAnswersSqliteModel.setCorrect_answer(response.body().getData().get(i).getAnswers().get(j).getCorrect_answer());

                                if (!currentlyPlayingAnswersTableQueries.insertCurrentlyPlayingAnswers(currentlyPlayingAnswersSqliteModel))
                                    Toast.makeText(QuizStartActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();

                            }
                        }
                        Intent quiz_intent = new Intent(QuizStartActivity.this, QuizActivity.class);
                        quiz_intent.putExtra("gameType", gameType);
                        startActivity(quiz_intent);
                        finish();

                    } else if (response.body().getStatus() == 6004){
                        AlertDialogCustom.sessionExpiredDialog(QuizStartActivity.this);
                    } else {
                        AlertDialogCustom.commonAlertDialog(QuizStartActivity.this, response.body().getMessage());

                    }
                }

            }

            @Override
            public void onFailure(Call<GetQuestionsModel> call, Throwable t) {
                System.out.println("t = " + t.getMessage());
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(QuizStartActivity.this, getString(R.string.went_wrong_wait));
            }

        });
    }

    //language wrapper
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }
}
