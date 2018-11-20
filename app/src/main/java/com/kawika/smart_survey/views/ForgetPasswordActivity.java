package com.kawika.smart_survey.views;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.alerts.AlertDialogCustom;
import com.kawika.smart_survey.api_services.RetrofitService;
import com.kawika.smart_survey.application.SmartSurveyApplication;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.models.CommonModel;
import com.kawika.smart_survey.networkCheck.NetworkCheck;
import com.kawika.smart_survey.utils.CustomProgress;
import com.kawika.smart_survey.utils.LocaleManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgetPasswordActivity extends BaseAppCompatActivity {

    Button sendButton;
    EditText emailEditText, numberEditText;
    private static final String TAG = "ForgetPasswordActivity";
    CardView emailButtonCardView;
    CardView numberButtonCardView;
    private YoYo.YoYoString rope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pasword_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode().setDuration(600));
        }
        //widget declaration
        sendButton = findViewById(R.id.sendButton);
        emailEditText = findViewById(R.id.emailEditText);
        numberEditText = findViewById(R.id.numberEditText);
        emailButtonCardView = findViewById(R.id.emailButtonCardView);
        numberButtonCardView = findViewById(R.id.numberButtonCardView);

        //click listener
        sendButton.setOnClickListener(view -> {
            if (validateEmail(true) || validatePhoneNo(true)) {
                send();
            }
        });
    }

    public void send() {
        Log.d(TAG, "Login");
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            AlertDialogCustom.noInternetAlertDialog(this);
            return;
        }

        //progress dialog custom method
        final CustomProgress progressDialog = new CustomProgress(this);
        progressDialog.show();

        String email = emailEditText.getText().toString();
        String number = numberEditText.getText().toString();
        Call<CommonModel> registerUser;
        // Web service contact
        // #library : Retrofit
        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();
        //call object
        if (validateEmail(false) && validatePhoneNo(false)) {
            //both
            registerUser = retrofitService.forgetPassword(email, number);
        } else if (validateEmail(false)) {
            //email only
            registerUser = retrofitService.forgetPassword(email, null);
        } else {
            //phone
            registerUser = retrofitService.forgetPassword(null, number);
        }

        //method
        registerUser.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                //progress bar dismiss
                progressDialog.dismiss();
                //web call success ,200
                if (response.code() == 200) {
                    //Api response success ,6000
                    if (response.body().getStatus() == 6000) {
                        onLoginSuccess(response.body());
                    } else {
                        //fail alert
                        AlertDialogCustom.commonAlertDialog(ForgetPasswordActivity.this, response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<CommonModel> call, Throwable t) {
                System.out.println("t = " + t.getMessage());
                //progress bar dismiss
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(ForgetPasswordActivity.this, getString(R.string.went_wrong_wait));
            }

        });
    }

    /**
     * success method
     * @param response CommonModel
     */
    public void onLoginSuccess(CommonModel response) {

        Intent intent=new Intent(this, OtpValidationActivity.class);
        intent.putExtra(AppConfiguration.PAGE_IDENTIFIER, 2);
        intent.putExtra(AppConfiguration.USER_ID, response.getUser_id());
        startActivity(intent);
        finish();
    }

    /**
     * phone no checking
     *
     * @return boolean
     */
    public boolean validatePhoneNo(boolean isAnimated) {
        boolean valid = true;

        String number = numberEditText.getText().toString();
        if (number.isEmpty() || number.length() < 10) {
            //animation checking
            if (isAnimated) {
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .repeat(1)
                        .playOn(numberButtonCardView);
                Toast.makeText(getBaseContext(), R.string.error_phone, Toast.LENGTH_LONG).show();
            }
            valid = false;
        }

        return valid;
    }

    /**
     * email checking
     *
     * @return boolean result
     */
    public boolean validateEmail(boolean isAnimated) {
        boolean valid = true;

        String email = emailEditText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            if (isAnimated) {
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .repeat(1)
                        .playOn(emailButtonCardView);
                Toast.makeText(getBaseContext(), R.string.error_email, Toast.LENGTH_LONG).show();
            }
            valid = false;
        }
        return valid;
    }

    //language wrapper
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }
}
