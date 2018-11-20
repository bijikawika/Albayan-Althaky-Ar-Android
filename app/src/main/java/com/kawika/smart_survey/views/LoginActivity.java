package com.kawika.smart_survey.views;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.alerts.AlertDialogCustom;
import com.kawika.smart_survey.api_services.RetrofitService;
import com.kawika.smart_survey.application.SmartSurveyApplication;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.database.CategoriesTableQueries;
import com.kawika.smart_survey.database.QuickChallengeTableQueries;
import com.kawika.smart_survey.database.UserDataTableQueries;
import com.kawika.smart_survey.models.AuthenticationModel;
import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.kawika.smart_survey.networkCheck.NetworkCheck;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.utils.CustomProgress;
import com.kawika.smart_survey.utils.LocaleManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kawika.smart_survey.database.CategoriesTableQueries.categoriesTableQueries;
import static com.kawika.smart_survey.database.QuickChallengeTableQueries.quickChallengeTableQueries;
import static com.kawika.smart_survey.database.TopPlayersTableQueries.topPlayersTableQueries;


public class LoginActivity extends BaseAppCompatActivity {

    EditText emailEditText, passwordEditText;
    private CardView emailButtonCardView, confirmButtonCardView;
    CardView passwordButtonCardView;
    TextView forgetPass;
    private ImageView profileImageView;
    private TextView welcomeTextView;
    private YoYo.YoYoString rope;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appPreferences = AppPreferences.getInstance(LoginActivity.this, AppConfiguration.SMART_SURVEY_PREFS);
        appPreferences.saveInt(AppConfiguration.LANGUAGE_ID, 2);

            if (appPreferences.getBoolean(AppConfiguration.IS_LOGGED)) {
                CategoriesTableQueries followedTopicsTableQueries = CategoriesTableQueries.sharedInstance(LoginActivity.this);
                List<CategoriesSqliteModel> categoriesSqliteModels = followedTopicsTableQueries.getFollowedTopics();
                if (categoriesSqliteModels.size() > 2) {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                } else {
                    startActivity(new Intent(LoginActivity.this, CatalogActivity.class));
                }
                finish();
            }

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailButtonCardView = findViewById(R.id.emailButtonCardView);
        passwordButtonCardView = findViewById(R.id.passwordButtonCardView);
        confirmButtonCardView = findViewById(R.id.confirmButtonCardView);
        Button loginButton = findViewById(R.id.loginButton);
        forgetPass = findViewById(R.id.forgetPass);
        profileImageView = findViewById(R.id.profileImageView);
        welcomeTextView = findViewById(R.id.welcomeTextView);

        profileImageView.setVisibility(View.INVISIBLE);
        welcomeTextView.setVisibility(View.INVISIBLE);
        emailButtonCardView.setVisibility(View.INVISIBLE);
        passwordButtonCardView.setVisibility(View.INVISIBLE);
        confirmButtonCardView.setVisibility(View.INVISIBLE);

        loginButton.setOnClickListener(view -> {
            if (validate()) {
                try {
                    loginServerCall();
                } catch (Exception e) {
                    Log.e("Login activity", "exe: ", e);
                }
            }
        });

        forgetPass.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            transitionTo(i);
            if (rope != null) {
                rope.stop(true);
            }
        });

        LinearLayout registerRelativeLayout = findViewById(R.id.registerRelativeLayout);
        registerRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                if (rope != null) {
                    rope.stop(true);
                }

            }
        });

        playAnim();


        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        loginServerCall();
                    } catch (Exception e) {
                        Log.e("Welcome activity", "PermissionM: ", e);
                    }
                    handled = true;
                }
                return handled;
            }
        });

    }

    private void playAnim() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                profileImageView.setVisibility(View.VISIBLE);
                rope = YoYo.with(Techniques.ZoomIn)
                        .duration(500)
                        .playOn(profileImageView);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        welcomeTextView.setVisibility(View.VISIBLE);
                        rope = YoYo.with(Techniques.FadeIn)
                                .duration(700)
                                .playOn(welcomeTextView);
                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            emailButtonCardView.setVisibility(View.VISIBLE);
                            rope = YoYo.with(Techniques.BounceIn)
                                    .duration(400)
                                    .playOn(emailButtonCardView);
                            Handler handler12 = new Handler();
                            handler12.postDelayed(() -> {
                                passwordButtonCardView.setVisibility(View.VISIBLE);
                                rope = YoYo.with(Techniques.BounceIn)
                                        .duration(400)
                                        .playOn(passwordButtonCardView);
                                Handler handler1 = new Handler();
                                handler1.postDelayed(() -> {
                                    confirmButtonCardView.setVisibility(View.VISIBLE);
                                    rope = YoYo.with(Techniques.BounceIn)
                                            .duration(400)
                                            .playOn(confirmButtonCardView);
                                }, 100);
                            }, 100);
                        }, 200);
                    }
                }, 100);
            }
        }, 50);
    }

    /**
     * server communication of login
     */
    public void loginServerCall() {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            AlertDialogCustom.noInternetAlertDialog(this);
            return;
        }
        CustomProgress progressDialog = new CustomProgress(LoginActivity.this);
        progressDialog.show();
        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();
        Call<AuthenticationModel> loginCall = retrofitService.loginUser(emailEditText.getText().toString(),
                passwordEditText.getText().toString(), generateToken(), appPreferences.getInt(AppConfiguration.LANGUAGE_ID));
        loginCall.enqueue(new Callback<AuthenticationModel>() {
            @Override
            public void onResponse(Call<AuthenticationModel> call, Response<AuthenticationModel> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 6000) {
                        onLoginSuccess(response.body().getData());
                    } else {
                        AlertDialogCustom.commonAlertDialog(LoginActivity.this, response.body().getMessage());
                    }
                } else {
                    AlertDialogCustom.commonAlertDialog(LoginActivity.this, getString(R.string.went_wrong_wait));

                }
            }

            @Override
            public void onFailure(Call<AuthenticationModel> call, Throwable t) {
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(LoginActivity.this, getString(R.string.went_wrong_wait));
            }

        });
    }

    /**
     * success message of login
     *
     * @param data
     */
    public void onLoginSuccess(AuthenticationModel.DataBean data) {
        UserDataTableQueries userDataTableQueries = UserDataTableQueries.sharedInstance(getApplicationContext());
        userDataTableQueries.deleteAllUserDetails();

        AuthenticationModel loginModel = new AuthenticationModel();
        loginModel.setData(data);

        userDataTableQueries.insertUserDetails(loginModel);

        categoriesTableQueries = categoriesTableQueries.sharedInstance(LoginActivity.this);
        topPlayersTableQueries = topPlayersTableQueries.sharedInstance(LoginActivity.this);
        categoriesTableQueries.deleteAllCategories();
        topPlayersTableQueries.deleteAllTopPlayers();

        for (int i = 0; i < data.getUser_categories().size(); i++) {
            final CategoriesSqliteModel categoriesSqliteModel = new CategoriesSqliteModel();
            categoriesSqliteModel.setEn_category_id(data.getUser_categories().get(i).getEn_category_id());
            categoriesSqliteModel.setCategory_color(data.getUser_categories().get(i).getCategory_color());
            categoriesSqliteModel.setCategory_name(data.getUser_categories().get(i).getCategory_name());
            categoriesSqliteModel.setCrown(data.getUser_categories().get(i).getCrown());
            categoriesSqliteModel.setCrown_image(data.getUser_categories().get(i).getCrown_image());
            categoriesSqliteModel.setLevel_count(data.getUser_categories().get(i).getLevel_count());
            categoriesSqliteModel.setIs_followed(data.getUser_categories().get(i).getIs_followed());
            categoriesSqliteModel.setImage_path(data.getUser_categories().get(i).getImage_path());

            if (!categoriesTableQueries.insertAllTopics(categoriesSqliteModel))
                Toast.makeText(LoginActivity.this, "Something went wrong. Please restart .", Toast.LENGTH_LONG).show();

            if (data.getUser_categories().get(i).getTop_players().size() > 0) {
                for (int j = 0; j < data.getUser_categories().get(i).getTop_players().size(); j++) {
                    int categorId = data.getUser_categories().get(i).getEn_category_id();
                    String firstName = data.getUser_categories().get(i).getTop_players().get(j).getFirstname();
                    String lastName = data.getUser_categories().get(i).getTop_players().get(j).getLastname();
                    int userId = data.getUser_categories().get(i).getTop_players().get(j).getUser_id();
                    int totalMark = data.getUser_categories().get(i).getTop_players().get(j).getTotal_mark();
                    int rank = data.getUser_categories().get(i).getTop_players().get(j).getRank();
                    String imagePath = data.getUser_categories().get(i).getTop_players().get(j).getImage_path();
                    String categoryName = data.getUser_categories().get(i).getTop_players().get(j).getCategory_name();
                    topPlayersTableQueries.insertTopPlayers(categorId, firstName, lastName, userId, totalMark, rank, imagePath, categoryName);
                }
            }

        }

        if (data.getChallengeNotification() != null) {
            quickChallengeTableQueries = QuickChallengeTableQueries.sharedInstance(getApplicationContext());
            quickChallengeTableQueries.deleteAllQuickChallenge();
            quickChallengeTableQueries.insertQuickChallenge(data.getChallengeNotification().getIs_available(), data.getChallengeNotification().getMessage());
        }

        if (data.getChallengeNotification().getPush_enable() == 1)
            appPreferences.saveBoolean(AppConfiguration.IS_PUSH_NOTIFICATION_SET, true);
        else
            appPreferences.saveBoolean(AppConfiguration.IS_PUSH_NOTIFICATION_SET, false);


        appPreferences.saveData(AppConfiguration.MY_TOKEN, data.getDevice_id());
        appPreferences.saveData(AppConfiguration.USER_ID, String.valueOf(data.getId()));
        appPreferences.saveBoolean(AppConfiguration.IS_LOGGED, true);

        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        Toast.makeText(getBaseContext(), R.string.login_success, Toast.LENGTH_LONG).show();
        finish();

    }


    public boolean validate() {
        boolean valid = true;

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            //emailEditText.setError("enter a valid email address");
            rope = YoYo.with(Techniques.Shake)
                    .duration(700)
                    .playOn(emailButtonCardView);
            valid = false;
            Toast.makeText(getBaseContext(), "Enter a valid email address", Toast.LENGTH_SHORT).show();
        }
        if (password.isEmpty()) {
            //  passwordEditText.setError("between 4 and 10 alphanumeric characters");
            rope = YoYo.with(Techniques.Shake)
                    .duration(700)
                    .playOn(passwordButtonCardView);
            valid = false;
            Toast.makeText(getBaseContext(), "Enter a valid password", Toast.LENGTH_SHORT).show();
        }

        return valid;
    }


    private long generateToken() {
        return System.currentTimeMillis();
    }


    //language wrapper
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }
}
