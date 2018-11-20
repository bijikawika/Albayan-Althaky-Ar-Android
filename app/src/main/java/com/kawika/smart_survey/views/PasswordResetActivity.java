package com.kawika.smart_survey.views;
/*
 * Created by akhil on 28/02/18.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class PasswordResetActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private int userId;
    private EditText newPasswordEditText;
    private EditText conformPasswordEditText;
    private Button sendButton;
    private CardView sendButtonCardView;
    private CardView conformPasswordButtonCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_reset_activity);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null) {
            //without data activity is closing
            finish();
            return;
        }
        userId = extras.getInt(AppConfiguration.USER_ID);

        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        conformPasswordEditText = findViewById(R.id.conformPasswordEditText);
        sendButton = findViewById(R.id.sendButton);
        sendButtonCardView = findViewById(R.id.sendButtonCardView);
        conformPasswordButtonCardView = findViewById(R.id.conformPasswordButtonCardView);

        newPasswordEditText.addTextChangedListener(this);
        conformPasswordEditText.addTextChangedListener(this);
        sendButton.setOnClickListener(this);
        checkSendButtonDisplayStatus();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        if (newPasswordEditText.getText().hashCode() == editable.hashCode()) {
            if (conformPasswordEditText.getText().length() != 0) {
                conformPasswordEditText.requestFocus();
            }
        }
        checkSendButtonDisplayStatus();
    }

    /**
     * check all edit text are fill or not
     */
    private void checkSendButtonDisplayStatus() {
        if (validateEditText(conformPasswordEditText, false)
                && validateEditText(newPasswordEditText, false)) {
            sendButtonCardView.setAlpha(1f);
            sendButtonCardView.setEnabled(true);
            sendButton.setEnabled(true);
        } else {
            sendButtonCardView.setAlpha(0.75f);
            sendButtonCardView.setEnabled(false);
            sendButton.setEnabled(false);
        }
    }

    /**
     * editText checking
     *
     * @return boolean result
     */
    public boolean validateEditText(EditText editText, boolean isAnimated) {
        boolean valid = true;

        String editView = editText.getText().toString();

        if (editView.isEmpty() || editView.trim().length() == 1) {

            if (isAnimated) {
                viewAnimation(editText);
            }
            valid = false;
        }
        return valid;
    }

    /**
     * animation view
     *
     * @param view view
     */
    private void viewAnimation(View view) {
        YoYo.with(Techniques.Tada)
                .duration(700)
                .repeat(1)
                .playOn(view);
    }

    /**
     * screen tech event
     *
     * @param event : MotionEvent
     * @return : result
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sendButton) {
            //password set pin server call
            if (checkPasswordEqual()) {
                setPasswordServerCall();

            } else {
                viewAnimation(conformPasswordButtonCardView);
                Toast.makeText(this, getResources().getString(R.string.password_doesnot_mach), Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * web service
     */
    private void setPasswordServerCall() {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            AlertDialogCustom.noInternetAlertDialog(this);
            return;
        }
        //progress dialog custom method
        final CustomProgress progressDialog = new CustomProgress(this);
        progressDialog.show();

        String newPassword = conformPasswordEditText.getText().toString();

        // Web service contact
        // #library : Retrofit
        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();
        //call object
        Call<CommonModel> setPassword = retrofitService.setPassword(userId, newPassword);

        //method
        setPassword.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                //progress bar dismiss
                progressDialog.dismiss();
                //web call success ,200
                if (response.code() == 200) {
                    //Api response success ,6000
                    if (response.body().getStatus() == 6000) {
                        onLoginSuccess();
                    } else {
                        //fail alert
                        AlertDialogCustom.commonAlertDialog(PasswordResetActivity.this, response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<CommonModel> call, Throwable t) {
                System.out.println("t = " + t.getMessage());
                //progress bar dismiss
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(PasswordResetActivity.this, getString(R.string.went_wrong_wait));
            }

        });
    }

    /**
     * success method
     */
    private void onLoginSuccess() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /**
     * check new and conform password are equal
     *
     * @return boolean
     */

    private boolean checkPasswordEqual() {
        if ((newPasswordEditText.getText().toString()).compareTo(conformPasswordEditText.getText().toString()) != 0) {
            return false;

        } else {
            return true;
        }
    }

    //language wrapper
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }
}
