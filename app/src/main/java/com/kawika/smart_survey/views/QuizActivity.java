package com.kawika.smart_survey.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.adapters.OptionsAdapter;
import com.kawika.smart_survey.adapters.QuestionsCountAdapter;
import com.kawika.smart_survey.alerts.AlertDialogCustom;
import com.kawika.smart_survey.utils.PlaySound;
import com.kawika.smart_survey.api_services.RetrofitService;
import com.kawika.smart_survey.application.SmartSurveyApplication;
import com.kawika.smart_survey.callbacks.OptionClickListener;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.database.CheckedResultsTableQueries;
import com.kawika.smart_survey.database.CurrentlyPlayingAnswersTableQueries;
import com.kawika.smart_survey.database.CurrentlyPlayingMainTableQueries;
import com.kawika.smart_survey.database.QuickChallengeTableQueries;
import com.kawika.smart_survey.database.ScoresTableQueries;
import com.kawika.smart_survey.database.UserDataTableQueries;
import com.kawika.smart_survey.models.CheckedResultsSqliteModel;
import com.kawika.smart_survey.models.CurrentlyPlayingAnswersSqliteModel;
import com.kawika.smart_survey.models.CurrentlyPlayingMainSqliteModel;
import com.kawika.smart_survey.models.QuestionCountModel;
import com.kawika.smart_survey.models.ScoresSqliteModel;
import com.kawika.smart_survey.models.SubmitResultModel;
import com.kawika.smart_survey.models.SubmitResultsModel;
import com.kawika.smart_survey.networkCheck.NetworkCheck;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.recycler_scroll_control.MyCustomLayoutManager;
import com.kawika.smart_survey.utils.CustomProgress;
import com.kawika.smart_survey.utils.DigitsLocalization;
import com.kawika.smart_survey.utils.LocaleManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kawika.smart_survey.config.AppConfiguration.QUICK_CHALLENGE;
import static com.kawika.smart_survey.database.CheckedResultsTableQueries.checkedResultsTableQueries;
import static com.kawika.smart_survey.database.CurrentlyPlayingAnswersTableQueries.currentlyPlayingAnswersTableQueries;
import static com.kawika.smart_survey.database.CurrentlyPlayingMainTableQueries.currentlyPlayingMainTableQueries;
import static com.kawika.smart_survey.database.QuickChallengeTableQueries.quickChallengeTableQueries;
import static com.kawika.smart_survey.database.ScoresTableQueries.scoresTableQueries;
import static com.kawika.smart_survey.utils.RevealEffect.getViewToViewScalingAnimator;

/*
 * Created by senthiljs on 09/02/18.
 */

public class QuizActivity extends AppCompatActivity implements OptionClickListener {
    private List<QuestionCountModel> questionCountModelList = new ArrayList<>();
    private RecyclerView questionCountRecyclerView, optionsMainRecyclerView;
    private QuestionsCountAdapter mAdapter;
    private int positionCount = -1, totalScore = 0;
    private TextView questionTextView, counterTextView,
            userNameTextView, myScoreTextView, currentScoreTextView;
    private RelativeLayout rootView;
    private int fadeInDuration = 700;
    private CountDownTimer count;
    private boolean isCountDownFinish = false;
    private ImageView profileImageView, shuttle;
    private List<CurrentlyPlayingAnswersSqliteModel> currentlyPlayingAnswersSqliteModelArrayList;
    private ArrayList<CurrentlyPlayingMainSqliteModel> currentlyPlayingMainSqliteModel;
    private boolean isClickEnabled = true;
    private int answerSelectId;
    private String passed_result = "";
    private int fullyScored = 1;
    private AppPreferences appPreferences;
    private View shuttleView;
    private OptionClickListener optionClickListener;
    private OptionsAdapter optionsAdapter;
    private RelativeLayout closeRelativeLayout;
    private int gameType;
    private ImageView quizCloseImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);

        appPreferences = AppPreferences.getInstance(QuizActivity.this, AppConfiguration.SMART_SURVEY_PREFS);

        Intent myIntent = getIntent();
        gameType = myIntent.getIntExtra("gameType", 0);

        optionClickListener = this;

        currentlyPlayingMainTableQueries = CurrentlyPlayingMainTableQueries.sharedInstance(QuizActivity.this);
        currentlyPlayingAnswersTableQueries = CurrentlyPlayingAnswersTableQueries.sharedInstance(QuizActivity.this);
        checkedResultsTableQueries = CheckedResultsTableQueries.sharedInstance(getApplicationContext());
        scoresTableQueries = ScoresTableQueries.sharedInstance(getApplicationContext());

        userNameTextView = findViewById(R.id.userNameTextView);
        profileImageView = findViewById(R.id.profileImageView);
        questionTextView = findViewById(R.id.questionTextView);
        myScoreTextView = findViewById(R.id.myScoreTextView);

        optionsMainRecyclerView = findViewById(R.id.optionsMainRecyclerView);
        closeRelativeLayout = findViewById(R.id.closeRelativeLayout);


        currentScoreTextView = findViewById(R.id.currentScoreTextView);
        shuttle = findViewById(R.id.shuttle);
        rootView = findViewById(R.id.rootView);
        shuttleView = findViewById(R.id.shuttle);

        UserDataTableQueries userDataTableQueries = UserDataTableQueries.sharedInstance(QuizActivity.this);
        userNameTextView.setText(userDataTableQueries.getName());
        if (appPreferences.getInt(AppConfiguration.LANGUAGE_ID) == 1){
            myScoreTextView.setText(userDataTableQueries.getMyScore());
        }else{
            myScoreTextView.setText(DigitsLocalization.convertEnglishDigitsToArabic(userDataTableQueries.getMyScore()));
        }
        if (userDataTableQueries.getProfileImagePath() != null) {
            Picasso.with(QuizActivity.this)
                    .load(userDataTableQueries.getProfileImagePath())
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(profileImageView);
        }
        currentlyPlayingMainSqliteModel = currentlyPlayingMainTableQueries.getAllMainQuestions();

        scrollCountRcycler();

        resetViews();

        quizCloseImageView = findViewById(R.id.quizCloseImageView);
        quizCloseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuizActivity.this.finish();
            }
        });


    }

    private void setData() {
        if (appPreferences.getInt(AppConfiguration.LANGUAGE_ID) == 1){
            currentScoreTextView.setText(String.valueOf(totalScore));
        }else{
            currentScoreTextView.setText(DigitsLocalization.convertEnglishDigitsToArabic(String.valueOf(totalScore)));
        }
        questionTextView.setVisibility(View.INVISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            questionTextView.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeIn)
                    .duration(fadeInDuration)
                    .playOn(questionTextView);
        }, 0);

        if (currentlyPlayingMainSqliteModel != null && currentlyPlayingMainSqliteModel.size() > 0) {
            questionTextView.setText(currentlyPlayingMainSqliteModel.get(positionCount).getQuestion());
        }
        currentlyPlayingAnswersSqliteModelArrayList = currentlyPlayingAnswersTableQueries.getSelectedAnswerBasedById
                (currentlyPlayingMainSqliteModel.get(positionCount).getQuestion_id());

        optionsAdapter = new OptionsAdapter(QuizActivity.this, optionClickListener, currentlyPlayingAnswersSqliteModelArrayList);
        optionsMainRecyclerView.setHasFixedSize(true);
        optionsMainRecyclerView.setLayoutManager(new MyCustomLayoutManager(getApplicationContext()));
        optionsMainRecyclerView.setItemAnimator(new DefaultItemAnimator());
        optionsMainRecyclerView.setAdapter(optionsAdapter);

    }

    //dialog for invalid selection
    private void showDialogForInvalidAnswer(String explanation) {
        final Dialog dialog = new Dialog(QuizActivity.this, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.invalid_selection_dialog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.setCanceledOnTouchOutside(true);

        TextView dialogAnswerExplanationTextView = dialog.findViewById(R.id.dialogAnswerExplanationTextView);
        dialogAnswerExplanationTextView.setText(explanation);

        dialog.setOnCancelListener(dialogInterface -> {
            passed_result = "false";
            System.out.println("passed_result = " + passed_result);
            validateResult();
        });
        ImageView cancelPopupImageView = dialog.findViewById(R.id.cancelPopupImageView);
        cancelPopupImageView.setOnClickListener(view -> {
            dialog.cancel();
        });

        dialog.show();
    }

    private void resetViews() {

        positionCount++;

        //set question count
        TextView questionNumberTextView = findViewById(R.id.questionNumberTextView);
        if (appPreferences.getInt(AppConfiguration.LANGUAGE_ID) == 1){
            questionNumberTextView.setText(String.valueOf(positionCount + 1));
        }else{
            questionNumberTextView.setText(DigitsLocalization.convertEnglishDigitsToArabic(String.valueOf(positionCount + 1)));
        }

        //set values
        setData();

        //scroll recycler to position
        questionCountRecyclerView.smoothScrollToPosition(positionCount);

        mAdapter.updateRecyclerView(positionCount, questionCountModelList);

        //runCountdown
        startCountdown();
    }


    private void clickedOption(View throwView, View optionView, RelativeLayout validRelativeLayout, RelativeLayout invalidRelativeLayout,
                               TextView optionTextView) {
        PlaySound.buttonClickSound(this, R.raw.option_click_sound);

        isClickEnabled = false;
        count.cancel();
        YoYo.with(Techniques.FadeIn)
                .duration(400)
                .repeat(5)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (currentlyPlayingAnswersSqliteModelArrayList.get(answerSelectId).getCorrect_answer() == 1) {
                            showSuccessLight(optionView, validRelativeLayout, throwView);
                        } else {
                            showFailureLight(optionView, invalidRelativeLayout, optionTextView);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .playOn(optionView);
    }

    private void scrollCountRcycler() {
        questionCountRecyclerView = findViewById(R.id.questionCountRecyclerView);
        prepareRecyclerCount();

        closeRelativeLayout.measure(closeRelativeLayout.getWidth(), closeRelativeLayout.getHeight());
        closeRelativeLayout.getMeasuredHeight();
        int layoutHeight = closeRelativeLayout.getMeasuredHeight();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int height = (displayMetrics.heightPixels) - layoutHeight * 4;
        questionCountRecyclerView.setPadding(0, 0, 0, height);
        questionCountRecyclerView.setClipToPadding(false);


        mAdapter = new QuestionsCountAdapter(questionCountModelList, QuizActivity.this, currentlyPlayingMainSqliteModel.size());
        questionCountRecyclerView.setHasFixedSize(true);
        questionCountRecyclerView.setLayoutManager(new MyCustomLayoutManager(getApplicationContext()));
        questionCountRecyclerView.setItemAnimator(new DefaultItemAnimator());
        questionCountRecyclerView.setAdapter(mAdapter);

        counterTextView = findViewById(R.id.counterTextView);
        counterTextView.setOnClickListener(view -> {
            startActivity(new Intent(QuizActivity.this, YouLoseActivity.class));
            finish();
        });
    }

    private void prepareRecyclerCount() {
        for (int i = 1 ; i < currentlyPlayingMainSqliteModel.size()+1; i++) {
            QuestionCountModel questionCountModel = new QuestionCountModel();
            questionCountModel.setQuesCount(String.valueOf(i));
            if (i == 1)
                questionCountModel.setHighlight(1);
            else
                questionCountModel.setHighlight(-1);
            questionCountModelList.add(questionCountModel);
        }
    }

    private void startCountdown() {
        isCountDownFinish = false;
        count = new CountDownTimer(61000, 1000) {
            public void onTick(long millisUntilFinished) {
                String milliSecond = String.valueOf((millisUntilFinished / 1000));
                if (appPreferences.getInt(AppConfiguration.LANGUAGE_ID) == 1){
                    counterTextView.setText(milliSecond +" "+getString(R.string.sec));
                }else{
                    counterTextView.setText(DigitsLocalization.convertEnglishDigitsToArabic(milliSecond +" "+getString(R.string.sec)));
                }
            }

            public void onFinish() {

                Handler refresh = new Handler(Looper.getMainLooper());
                refresh.post(() -> {
                    try {
                        totalScore = totalScore - 5;
                        fullyScored = 0;
                        passed_result = "none";
                        isClickEnabled = true;
                        throwScore(3, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                if (count != null) {
                    count.cancel();
                }
            }
        };

        count.start();
    }

    private void validateResult() {

        optionsAdapter.enableClick();
        updateResultInDb();
        quizCloseImageView.setEnabled(true);
        quizCloseImageView.setAlpha(1f);

        if (currentlyPlayingMainSqliteModel.size() == positionCount + 1) {
            if (fullyScored == 1)
                totalScore = totalScore + 10;
            ScoresSqliteModel scoresSqliteModels = scoresTableQueries.getScoresMain();
            if (totalScore > 30) {
                if (gameType == QUICK_CHALLENGE){
                    List<CheckedResultsSqliteModel> checkedResultsSqliteModelList = checkedResultsTableQueries.getAllAnswerResults();
                    submitResults(jsonForSubmitResult(scoresSqliteModels, checkedResultsSqliteModelList));
                }else {
                    Intent quiz_start_intent = new Intent(QuizActivity.this, YouWinActivity.class);
                    quiz_start_intent.putExtra("selectedCategoryId", scoresSqliteModels.getCategory_id());
                    quiz_start_intent.putExtra("currentStepId", scoresSqliteModels.getStep_id());
                    quiz_start_intent.putExtra("totalMark", totalScore);
                    quiz_start_intent.putExtra("playerType", scoresSqliteModels.getLevel_id());
                    startActivity(quiz_start_intent);
                    finish();
                }

            } else {
                if (gameType == QUICK_CHALLENGE){
                    List<CheckedResultsSqliteModel> checkedResultsSqliteModelList = checkedResultsTableQueries.getAllAnswerResults();
                    submitResults(jsonForSubmitResult(scoresSqliteModels, checkedResultsSqliteModelList));
                }else {
                    Intent quiz_start_intent = new Intent(QuizActivity.this, YouLoseActivity.class);
                    quiz_start_intent.putExtra("selectedCategoryId", scoresSqliteModels.getCategory_id());
                    quiz_start_intent.putExtra("currentStepId", scoresSqliteModels.getStep_id());
                    quiz_start_intent.putExtra("totalMark", totalScore);
                    quiz_start_intent.putExtra("playerType", scoresSqliteModels.getLevel_id());
                    startActivity(quiz_start_intent);
                    finish();
                }
            }


        } else {
            Handler refresh = new Handler(Looper.getMainLooper());
            refresh.post(new Runnable() {
                public void run() {
                    try {
                        isClickEnabled = true;
                        resetViews();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            if (count != null) {
                count.cancel();
            }
        }
    }

    private void showSuccessLight(View optionView, RelativeLayout validReltiveLayout, View throwView) {
        optionView.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.valid_answer_border));
        validReltiveLayout.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.ZoomIn)
                .duration(1000)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                        totalScore = totalScore + 10;
                        passed_result = "true";
                        throwScore(1, throwView);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .playOn(validReltiveLayout);
    }

    private void showFailureLight(View optionView, RelativeLayout invalidRelativeLayout, TextView optionTextView) {
        optionView.setBackground(ContextCompat.getDrawable(QuizActivity.this, R.drawable.invalid_answer_border));
        invalidRelativeLayout.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.ZoomIn)
                .duration(1000)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        fullyScored = 0;
                        totalScore = totalScore - 5;
                        throwScore(2, null);

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .playOn(invalidRelativeLayout);
    }

    private void updateResultInDb() {

        final CheckedResultsSqliteModel checkedResultsSqliteModel = new CheckedResultsSqliteModel();
        checkedResultsSqliteModel.setQuestion_id(currentlyPlayingAnswersSqliteModelArrayList.get(answerSelectId).getQuestion_id());
        if (!passed_result.equals("none")) {
            checkedResultsSqliteModel.setClicked_id(currentlyPlayingAnswersSqliteModelArrayList.get(answerSelectId).getAnswer_id());
        } else {
            checkedResultsSqliteModel.setClicked_id(-1);
        }
        checkedResultsSqliteModel.setPassed_result(passed_result);

        checkedResultsTableQueries.insertCheckedResults(checkedResultsSqliteModel);

        scoresTableQueries.deleteAllScores();
        final ScoresSqliteModel scoresSqliteModel = new ScoresSqliteModel();
        scoresSqliteModel.setCategory_name(currentlyPlayingMainSqliteModel.get(0).getCategory());
        scoresSqliteModel.setCategory_id(currentlyPlayingMainSqliteModel.get(0).getCategory_id());
        scoresSqliteModel.setStep_id(currentlyPlayingMainSqliteModel.get(0).getSteps());
        if (currentlyPlayingMainSqliteModel.get(0).getLevel().equals(getString(R.string.beginner))) {
            scoresSqliteModel.setLevel_id(1);
        } else if (currentlyPlayingMainSqliteModel.get(0).getLevel().equals(getString(R.string.intermediate))) {
            scoresSqliteModel.setLevel_id(2);
        } else if (currentlyPlayingMainSqliteModel.get(0).getLevel().equals(getString(R.string.expert))) {
            scoresSqliteModel.setLevel_id(3);
        } else {
            scoresSqliteModel.setLevel_id(-1);
        }
        scoresSqliteModel.setTotal_mark(250);
        scoresSqliteModel.setScored_mark(totalScore);
        scoresSqliteModel.setFull_scored(fullyScored);

        scoresTableQueries.insertScores(scoresSqliteModel);

    }

    private void throwScore(int key, View throwView) {
        if (key == 1) {
            PlaySound.buttonClickSound(this, R.raw.coin_sound);

            Rect fromRect = new Rect();
            Rect toRect = new Rect();

            if (throwView != null) {
                ImageView animEndImageView = findViewById(R.id.animEndImageView);
                throwView.getGlobalVisibleRect(fromRect);
                animEndImageView.getGlobalVisibleRect(toRect);

                AnimatorSet animatorSet = getViewToViewScalingAnimator(rootView, shuttleView, fromRect, toRect, 1000, 0);

                animatorSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        shuttleView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        shuttleView.setVisibility(View.GONE);

                        validateResult();

                       updateCurrentScore();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animatorSet.start();
            }
        } else if (key == 2) {
            updateCurrentScore();
            showDialogForInvalidAnswer(currentlyPlayingMainSqliteModel.get(positionCount).getExplanation());
        } else {
            updateCurrentScore();
            validateResult();
        }

    }

    private void updateCurrentScore(){
        if (appPreferences.getInt(AppConfiguration.LANGUAGE_ID) == 1){
            currentScoreTextView.setText(String.valueOf(String.valueOf(totalScore)));
        }else{
            currentScoreTextView.setText(DigitsLocalization.convertEnglishDigitsToArabic(String.valueOf(totalScore)));
        }
    }

    //language wrapper
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onOptionClick(int position, View throwView, View optionView, RelativeLayout validRelativeLayout, RelativeLayout invalidRelativeLayout, TextView optionTextView) {
        answerSelectId = position;
        quizCloseImageView.setEnabled(false);
        quizCloseImageView.setAlpha(0.5f);
        clickedOption(throwView, optionView, validRelativeLayout, invalidRelativeLayout, optionTextView);
    }

    private void submitResults(SubmitResultModel submitResultModel) {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            AlertDialogCustom.noInternetAlertDialog(this);
            return;
        }
        final CustomProgress progressDialog = new CustomProgress(this);
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
                        quickChallengeTableQueries = QuickChallengeTableQueries.sharedInstance(getApplicationContext());
                        quickChallengeTableQueries.deleteAllQuickChallenge();

                        Intent quiz_start_intent = new Intent(QuizActivity.this, ViewResultActivity.class);
                        quiz_start_intent.putExtra("currentLevelId",submitResultModel.getStep_id());
                        quiz_start_intent.putExtra("totalMark",totalScore);
                        startActivity(quiz_start_intent);
                        finish();
                    } else if (response.body().getStatus() == 6004) {
                        AlertDialogCustom.sessionExpiredDialog(QuizActivity.this);
                    } else {
                        AlertDialogCustom.commonAlertDialog(QuizActivity.this, response.body().getMessage());
                    }
                } else {
                    AlertDialogCustom.commonAlertDialog(QuizActivity.this, String.valueOf(response.code()));
                }

            }

            @Override
            public void onFailure(Call<SubmitResultsModel> call, Throwable t) {
                System.out.println("t = " + t.getMessage());
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(QuizActivity.this, getString(R.string.went_wrong_wait));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (count != null) {
            count.cancel();
        }
    }
}