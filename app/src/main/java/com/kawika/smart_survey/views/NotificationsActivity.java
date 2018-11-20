package com.kawika.smart_survey.views;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kawika.smart_survey.R;
import com.kawika.smart_survey.alerts.AlertDialogCustom;
import com.kawika.smart_survey.api_services.RetrofitService;
import com.kawika.smart_survey.application.SmartSurveyApplication;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.database.CurrentlyPlayingAnswersTableQueries;
import com.kawika.smart_survey.database.CurrentlyPlayingMainTableQueries;
import com.kawika.smart_survey.database.QuickChallengeTableQueries;
import com.kawika.smart_survey.database.UserDataTableQueries;
import com.kawika.smart_survey.databinding.HomeActivityBinding;
import com.kawika.smart_survey.databinding.MyScoreActivityBinding;
import com.kawika.smart_survey.databinding.NotificationActivityBinding;
import com.kawika.smart_survey.models.BasicRetroCallModel;
import com.kawika.smart_survey.models.CurrentlyPlayingAnswersSqliteModel;
import com.kawika.smart_survey.models.CurrentlyPlayingMainSqliteModel;
import com.kawika.smart_survey.models.GetQuestionsModel;
import com.kawika.smart_survey.models.QuickChallengeCheckModel;
import com.kawika.smart_survey.models.QuickChallengeSqliteModel;
import com.kawika.smart_survey.networkCheck.NetworkCheck;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.utils.CustomProgress;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kawika.smart_survey.config.AppConfiguration.QUICK_CHALLENGE;
import static com.kawika.smart_survey.database.CurrentlyPlayingAnswersTableQueries.currentlyPlayingAnswersTableQueries;
import static com.kawika.smart_survey.database.CurrentlyPlayingMainTableQueries.currentlyPlayingMainTableQueries;
import static com.kawika.smart_survey.database.QuickChallengeTableQueries.quickChallengeTableQueries;
import static com.kawika.smart_survey.utils.RevealEffect.BOTTOM_RIGHT;
import static com.kawika.smart_survey.utils.RevealEffect.CENTER;
import static com.kawika.smart_survey.utils.RevealEffect.TOP_LEFT;
import static com.kawika.smart_survey.utils.RevealEffect.showWithCircularRevealAnimation;

/**
 * Created by senthiljs on 09/02/18.
 */

public class NotificationsActivity extends BaseActivity implements View.OnClickListener {

    NotificationActivityBinding binding;
    private boolean notificationFoundLocal = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quickChallengeTableQueries = QuickChallengeTableQueries.sharedInstance(getApplicationContext());

        showQuickChallengeifAvailable();

        checkQuickChallengeBackground();

        binding.acceptTextView.setOnClickListener(this);
        binding.rejectTextView.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        showQuickChallengeifAvailable();
        checkQuickChallengeBackground();
    }

    @Override
    int getContentViewId() {
        return R.layout.notification_activity;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.menu_notifications;
    }

    @Override
    NotificationActivityBinding getNotificationBinding() {
        return binding = DataBindingUtil.setContentView(this, R.layout.notification_activity);
    }

    @Override
    MyScoreActivityBinding getMyScoreActivityBinding() {
        return null;

    }

    @Override
    HomeActivityBinding getHomeBinding() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            System.runFinalization();
            Runtime.getRuntime().gc();
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.acceptTextView) {
            AppPreferences appPreferences = AppPreferences.getInstance(NotificationsActivity.this, AppConfiguration.SMART_SURVEY_PREFS);
            getQuickChallengeQuestions(appPreferences.getData(AppConfiguration.MY_TOKEN), appPreferences.getData(AppConfiguration.USER_ID));
        } else if (view.getId() == R.id.rejectTextView) {
            denyQuickChallenge();
        }
    }

    private void checkQuickChallengeBackground() {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            binding.noNotificationTextView.setText(R.string.no_internet);
            return;
        }

        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();

        AppPreferences appPreferences = AppPreferences.getInstance(NotificationsActivity.this, AppConfiguration.SMART_SURVEY_PREFS);
        Call<QuickChallengeCheckModel> quickChallengeCheckModelCall = retrofitService.checkQuickChallenge(appPreferences.getData(AppConfiguration.MY_TOKEN), appPreferences.getData(AppConfiguration.USER_ID));
        quickChallengeCheckModelCall.enqueue(new Callback<QuickChallengeCheckModel>() {
            @Override
            public void onResponse(Call<QuickChallengeCheckModel> call, Response<QuickChallengeCheckModel> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus() == 6000) {

                        quickChallengeTableQueries.deleteAllQuickChallenge();

                        quickChallengeTableQueries.insertQuickChallenge(response.body().getIs_available(), response.body().getMessage());

                        QuickChallengeSqliteModel quickChallengeSqliteModel = quickChallengeTableQueries.getQuickChallenge();

                        if (quickChallengeSqliteModel != null) {
                            if (quickChallengeSqliteModel.getIs_available() == 1) {

                                binding.noNotificationTextView.setVisibility(View.INVISIBLE);
                                binding.vRevealEffect.setVisibility(View.VISIBLE);

                                if (!notificationFoundLocal) {
                                    try {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            binding.notificationCardView.post(() -> showWithCircularRevealAnimation(binding.notificationCardView,
                                                    CENTER, BOTTOM_RIGHT, NotificationsActivity.this.getResources().getInteger(R.integer.anim_duration_medium)));
                                        } else
                                            binding.vRevealEffect.setVisibility(View.VISIBLE);
                                    } catch (Exception e) {
                                    }
                                }

                                notificationFoundLocal = true;

                            } else {
                                binding.vRevealEffect.setVisibility(View.INVISIBLE);
                                binding.noNotificationTextView.setVisibility(View.VISIBLE);
                            }
                        }

                    } else if (response.body().getStatus() == 6004) {
                        AlertDialogCustom.sessionExpiredDialog(NotificationsActivity.this);
                    } else {
                        binding.vRevealEffect.setVisibility(View.INVISIBLE);
                        binding.noNotificationTextView.setVisibility(View.VISIBLE);
                        binding.noNotificationTextView.setText(response.body().getMessage());

                    }
                }

            }

            @Override
            public void onFailure(Call<QuickChallengeCheckModel> call, Throwable t) {
                AlertDialogCustom.commonAlertDialog(NotificationsActivity.this, getString(R.string.went_wrong_wait));
            }

        });
    }

    private void showQuickChallengeifAvailable() {
        QuickChallengeSqliteModel quickChallengeSqliteModel = quickChallengeTableQueries.getQuickChallenge();
        if (quickChallengeSqliteModel != null) {
            if (quickChallengeSqliteModel.getIs_available() == 1) {

                binding.noNotificationTextView.setVisibility(View.INVISIBLE);
                binding.notificationCardView.setVisibility(View.VISIBLE);

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.vRevealEffect.post(() -> showWithCircularRevealAnimation(binding.vRevealEffect,
                                CENTER, BOTTOM_RIGHT, NotificationsActivity.this.getResources().getInteger(R.integer.anim_duration_medium)));
                    } else
                        binding.vRevealEffect.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                }

                notificationFoundLocal = true;
            } else {
                binding.vRevealEffect.setVisibility(View.INVISIBLE);
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.noNotificationTextView.setText(R.string.loading);
                        binding.noNotificationTextView.post(() -> showWithCircularRevealAnimation(binding.noNotificationTextView,
                                CENTER, BOTTOM_RIGHT, NotificationsActivity.this.getResources().getInteger(R.integer.anim_duration_medium)));
                    } else
                        binding.noNotificationTextView.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                }
                notificationFoundLocal = false;
            }
        }
    }

    private void getQuickChallengeQuestions(String myToken, String userId) {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            AlertDialogCustom.noInternetAlertDialog(this);
            return;
        }
        final CustomProgress progressDialog = new CustomProgress(this);
        progressDialog.show();
        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();

        Call<GetQuestionsModel> categoriesCall = retrofitService.getQuickChallenge(myToken, userId);
        categoriesCall.enqueue(new Callback<GetQuestionsModel>() {
            @Override
            public void onResponse(Call<GetQuestionsModel> call, Response<GetQuestionsModel> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 6000) {

                        currentlyPlayingMainTableQueries = CurrentlyPlayingMainTableQueries.sharedInstance(NotificationsActivity.this);
                        currentlyPlayingMainTableQueries.deleteAllCurrentlyPlayingMain();

                        currentlyPlayingAnswersTableQueries = CurrentlyPlayingAnswersTableQueries.sharedInstance(NotificationsActivity.this);
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
                                Toast.makeText(NotificationsActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();

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
                                    Toast.makeText(NotificationsActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();

                            }
                        }

                        if (response.body().getData().size() > 0) {
                            Intent quiz_start_intent = new Intent(NotificationsActivity.this, QuizStartActivity.class);
                            quiz_start_intent.putExtra("gameType", QUICK_CHALLENGE);
                            quiz_start_intent.putExtra("selectedCategoryId", response.body().getData().get(0).getEn_category_id());
                            startActivity(quiz_start_intent);
                        } else {
                            AlertDialogCustom.commonAlertDialog(NotificationsActivity.this, response.body().getMessage());
                        }

                    } else if (response.body().getStatus() == 6004) {
                        AlertDialogCustom.sessionExpiredDialog(NotificationsActivity.this);
                    } else {
                        AlertDialogCustom.commonAlertDialog(NotificationsActivity.this, response.body().getMessage());

                    }
                }

            }

            @Override
            public void onFailure(Call<GetQuestionsModel> call, Throwable t) {
                System.out.println("t = " + t.getMessage());
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(NotificationsActivity.this, getString(R.string.went_wrong_wait));
            }

        });
    }


    private void denyQuickChallenge() {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            AlertDialogCustom.noInternetAlertDialog(this);
            return;
        }

        binding.noNotificationTextView.setVisibility(View.VISIBLE);
        binding.noNotificationTextView.setText(R.string.loading);

        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();

        AppPreferences appPreferences = AppPreferences.getInstance(NotificationsActivity.this, AppConfiguration.SMART_SURVEY_PREFS);
        Call<BasicRetroCallModel> quickChallengeCheckModelCall = retrofitService.denyQuickChallenge(appPreferences.getData(AppConfiguration.MY_TOKEN), appPreferences.getData(AppConfiguration.USER_ID));
        quickChallengeCheckModelCall.enqueue(new Callback<BasicRetroCallModel>() {
            @Override
            public void onResponse(Call<BasicRetroCallModel> call, Response<BasicRetroCallModel> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus() == 6000) {
                        quickChallengeTableQueries.deleteAllQuickChallenge();
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                binding.noNotificationTextView.post(() -> showWithCircularRevealAnimation(binding.noNotificationTextView,
                                        CENTER, TOP_LEFT, NotificationsActivity.this.getResources().getInteger(R.integer.anim_duration_long)));
                                binding.noNotificationTextView.setText(R.string.challenge_denied);
                            } else {
                                binding.noNotificationTextView.setVisibility(View.VISIBLE);
                                binding.noNotificationTextView.setText(R.string.challenge_denied);
                            }
                        } catch (Exception e) {
                            Log.e("Welcome activity", "PermissionM: ", e);
                        }
                    } else if (response.body().getStatus() == 6004) {
                        AlertDialogCustom.sessionExpiredDialog(NotificationsActivity.this);
                    } else {
                        AlertDialogCustom.commonAlertDialog(NotificationsActivity.this, response.body().getMessage());

                    }
                }

            }

            @Override
            public void onFailure(Call<BasicRetroCallModel> call, Throwable t) {
                AlertDialogCustom.commonAlertDialog(NotificationsActivity.this, getString(R.string.went_wrong_wait));
            }

        });
    }


}