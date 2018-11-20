package com.kawika.smart_survey.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kawika.smart_survey.R;
import com.kawika.smart_survey.alerts.AlertDialogCustom;
import com.kawika.smart_survey.api_services.RetrofitService;
import com.kawika.smart_survey.application.SmartSurveyApplication;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.models.CommonModel;
import com.kawika.smart_survey.networkCheck.NetworkCheck;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.utils.CustomProgress;
import com.kawika.smart_survey.utils.LocaleManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpValidationActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private CountDownTimer countDownTimer = null;
    private Button verifyButton;
    private TextView resendPinButton, labelTextView;
    private CardView verifyButtonCardView;
    private EditText verify_no1;
    private EditText verify_no2;
    private EditText verify_no3;
    private int pageIdentifier;
    private int userId;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_validation);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null) {
            finish();
            return;
        }
        pageIdentifier = extras.getInt(AppConfiguration.PAGE_IDENTIFIER);
        userId = extras.getInt(AppConfiguration.USER_ID);
        appPreferences = AppPreferences.getInstance(this, AppConfiguration.SMART_SURVEY_PREFS);

        verifyButton = findViewById(R.id.verifyButton);
        verifyButtonCardView = findViewById(R.id.verifyButtonCardView);
        resendPinButton = findViewById(R.id.resendPinButton);
        labelTextView = findViewById(R.id.labelTextView);
        verify_no1 = findViewById(R.id.verify_no1);
        verify_no2 = findViewById(R.id.verify_no2);
        verify_no3 = findViewById(R.id.verify_no3);

        verify_no1.addTextChangedListener(this);
        verify_no2.addTextChangedListener(this);
        verify_no3.addTextChangedListener(this);
        verifyButton.setOnClickListener(this);
        resendPinButton.setOnClickListener(this);

        verify_no1.setNextFocusDownId(verify_no2.getId());
        verify_no2.setNextFocusDownId(verify_no3.getId());
        verify_no3.setNextFocusDownId(verify_no1.getId());

        verifyButton.setVisibility(View.VISIBLE);
        verifyButtonCardView.setVisibility(View.VISIBLE);

        checkVerifyButtonDisplayStatus();
    }

    private void countDownTimerSetting() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                /*textViewTimer.setText("" + millisUntilFinished / 1000);*/
                if ((millisUntilFinished / 1000) < 30) {
                    verifyButton.setVisibility(View.VISIBLE);
                    verifyButtonCardView.setVisibility(View.VISIBLE);
                }
            }

            public void onFinish() {
                /*textViewTimer.setVisibility(View.GONE);*/
            }
        }.start();
    }

//    //language wrapper
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(LocaleManager.setLocale(base));
//    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        if (verify_no1.getText().hashCode() == editable.hashCode()) {
            if (verify_no1.getText().length() != 0) {
                verify_no2.requestFocus();
            }
        } else if (verify_no2.getText().hashCode() == editable.hashCode()) {
            if (verify_no2.getText().length() != 0) {
                verify_no3.requestFocus();
            } else {
                verify_no1.requestFocus();
            }
        } else if (verify_no3.getText().hashCode() == editable.hashCode()) {
            if (verify_no3.getText().length() == 0) {
                verify_no2.requestFocus();
            }
        }
        checkVerifyButtonDisplayStatus();
    }

    private void checkVerifyButtonDisplayStatus() {
        if (verify_no1.getText().length()
                + verify_no2.getText().length()
                + verify_no3.getText().length() == 3) {
            verifyButtonCardView.setAlpha(1f);
            verifyButtonCardView.setEnabled(true);
            verifyButton.setEnabled(true);
        } else {
            verifyButtonCardView.setAlpha(0.75f);
            verifyButtonCardView.setEnabled(false);
            verifyButton.setEnabled(false);
        }
    }

    @Override
    public final void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        try {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();
        try {
            this.finish();
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.verifyButton) {
            VerifyServerCall();
        } else if (view.getId() == R.id.resendPinButton) {
            resendPinServerCall();
        }
    }

    private void resendPinServerCall() {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            AlertDialogCustom.noInternetAlertDialog(this);
            return;
        }
        final CustomProgress progressDialog = new CustomProgress(this);
        progressDialog.show();

        resendPinButton.setEnabled(false);
        String type;
        if (pageIdentifier == 2) {
            type = AppConfiguration.FORGOT;
        } else {
            type = AppConfiguration.REGISTRATION;
        }
        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();
        Call<CommonModel> registerUser = retrofitService.resendPin(userId, type);
        registerUser.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 6000) {
                        resendPinButton.setEnabled(true);
                        verify_no1.getText().clear();
                        verify_no2.getText().clear();
                        verify_no3.getText().clear();
                    } else {
                        AlertDialogCustom.commonAlertDialog(OtpValidationActivity.this, response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<CommonModel> call, Throwable t) {
                System.out.println("t = " + t.getMessage());
                progressDialog.dismiss();
                resendPinButton.setEnabled(true);
                AlertDialogCustom.commonAlertDialog(OtpValidationActivity.this, getString(R.string.went_wrong_wait));
            }

        });
    }

    private void VerifyServerCall() {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            AlertDialogCustom.noInternetAlertDialog(this);
            return;
        }
        final CustomProgress progressDialog = new CustomProgress(this);
        progressDialog.show();

        String otp = verify_no1.getText().toString()
                + verify_no2.getText().toString()
                + verify_no3.getText().toString();
        String type;
        if (pageIdentifier == 2) {
            type = AppConfiguration.FORGOT;
        } else {
            type = AppConfiguration.REGISTRATION;
        }

        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();
        Call<CommonModel> registerUser = retrofitService.otpVerification(userId, otp, type);
        registerUser.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 6000) {
                        onLoginSuccess(response.body());
                    } else {
                        AlertDialogCustom.commonAlertDialog(OtpValidationActivity.this, response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<CommonModel> call, Throwable t) {
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(OtpValidationActivity.this, getString(R.string.went_wrong_wait));
            }

        });
    }

    public void onLoginSuccess(CommonModel body) {
        Intent intent;
        if (pageIdentifier == 2) {
            intent = new Intent(this, PasswordResetActivity.class);
            intent.putExtra(AppConfiguration.USER_ID, body.getUser_id());
        } else {
            appPreferences.saveBoolean(AppConfiguration.IS_LOGGED, true);
            intent = new Intent(this, CatalogActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
