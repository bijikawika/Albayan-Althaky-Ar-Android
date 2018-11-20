package com.kawika.smart_survey.views;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.adapters.CountryCodesAdapter;
import com.kawika.smart_survey.adapters.DepartmentAdapter;
import com.kawika.smart_survey.alerts.AlertDialogCustom;
import com.kawika.smart_survey.api_services.RetrofitService;
import com.kawika.smart_survey.application.SmartSurveyApplication;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.database.QuickChallengeTableQueries;
import com.kawika.smart_survey.database.UserDataTableQueries;
import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.kawika.smart_survey.models.CountryCodeModel;
import com.kawika.smart_survey.models.DepartmentModel;
import com.kawika.smart_survey.models.AuthenticationModel;
import com.kawika.smart_survey.models.RegistrationRawCreation;
import com.kawika.smart_survey.networkCheck.NetworkCheck;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.utils.ApplicationPermission;
import com.kawika.smart_survey.utils.CustomProgress;
import com.kawika.smart_survey.utils.ImageCompression;
import com.kawika.smart_survey.utils.LocaleManager;
import com.kawika.smart_survey.utils.TempFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.gofynd.gravityview.GravityView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kawika.smart_survey.alerts.AlertDialogCustom.alertDialog;
import static com.kawika.smart_survey.application.SmartSurveyApplication.smartSurveyApplication;
import static com.kawika.smart_survey.config.AppConfiguration.CAMERA_AND_WRITE_REQUEST_CODE;
import static com.kawika.smart_survey.config.AppConfiguration.PERMISSIONS_REQUEST_READ_PHONE;
import static com.kawika.smart_survey.config.AppConfiguration.READ_PHONE_ALERT;
import static com.kawika.smart_survey.database.CategoriesTableQueries.categoriesTableQueries;
import static com.kawika.smart_survey.database.QuickChallengeTableQueries.quickChallengeTableQueries;
import static com.kawika.smart_survey.database.TopPlayersTableQueries.topPlayersTableQueries;

public class RegistrationActivity extends AppCompatActivity implements TextWatcher {
    private CardView firstNameCardView,
            lastNameCardView,
            emailIdCardView,
            phoneNumberCardView,
            createPasswordCardView,
            confirmPasswordCardView,
            departmentCardView,
            confirmButtonCardView;
    private EditText firstNameEditText, lastNameEditText, emailIdEditText, phoneNumberEditText,
            createPasswordEditText, confirmPasswordEditText;
    private TextView departmentTextView;
    private ProgressBar imageProgressBar;
    private int delayDuration = 65;
    private int fadeInDuration = 400;
    private BottomSheetDialog mBottomSheetDialog;
    private int height, width;
    private File file = null;
    private String filePath = null;
    private ImageView profileImageView, mainBackgroundImageView;
    private GravityView gravityView;
    private String profileImageBase64 = "noImage";
    private RelativeLayout choosePictureRelativeLayout;
    private Button registerButton;
    private TextView countryCodeTextView;
    private List<CountryCodeModel.DataBean> countryCodeDataList;
    private List<DepartmentModel.DataBean> departmentDataList;
    private CustomProgress progressDialog;
    private int departmentId;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firstNameCardView = findViewById(R.id.firstNameCardView);
        lastNameCardView = findViewById(R.id.lastNameCardView);
        emailIdCardView = findViewById(R.id.emailIdCardView);
        phoneNumberCardView = findViewById(R.id.phoneNumberCardView);
        createPasswordCardView = findViewById(R.id.createPasswordCardView);
        confirmPasswordCardView = findViewById(R.id.confirmPasswordCardView);
        departmentCardView = findViewById(R.id.departmentCardView);
        confirmButtonCardView = findViewById(R.id.confirmButtonCardView);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailIdEditText = findViewById(R.id.emailIdEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        createPasswordEditText = findViewById(R.id.createPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        profileImageView = findViewById(R.id.profileImageView);
        mainBackgroundImageView = findViewById(R.id.mainBackgroundImageView);
        choosePictureRelativeLayout = findViewById(R.id.choosePictureRelativeLayout);
        registerButton = findViewById(R.id.registerButton);
        countryCodeTextView = findViewById(R.id.countryCodeTextView);
        departmentTextView = findViewById(R.id.departmentTextView);
        imageProgressBar = findViewById(R.id.imageProgressBar);

        firstNameCardView.setVisibility(View.INVISIBLE);
        lastNameCardView.setVisibility(View.INVISIBLE);
        emailIdCardView.setVisibility(View.INVISIBLE);
        phoneNumberCardView.setVisibility(View.INVISIBLE);
        createPasswordCardView.setVisibility(View.INVISIBLE);
        confirmPasswordCardView.setVisibility(View.INVISIBLE);
        departmentCardView.setVisibility(View.INVISIBLE);
        confirmButtonCardView.setVisibility(View.INVISIBLE);

        openDropdown();

        progressDialog = new CustomProgress(RegistrationActivity.this);

        getCountryCodes();

        gravityView = GravityView.getInstance(RegistrationActivity.this)
                .setImage(mainBackgroundImageView, R.drawable.gravity_background)
                .center();

        appPreferences = AppPreferences.getInstance(RegistrationActivity.this, AppConfiguration.SMART_SURVEY_PREFS);

    }

    @Override
    protected void onStart() {
        super.onStart();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        RegistrationActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        choosePictureRelativeLayout.setOnClickListener(view -> {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermission();
                } else {
                    itemNewImageCapture();
                }
            } catch (Exception e) {
                Log.e("Welcome activity", "PermissionM: ", e);
            }
        });
        registerButton.setOnClickListener(view -> {
            if (validatePersonalDetails()) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_READ_PHONE);
                        } else {
                            registerUser();
                        }
                    } else {
                        registerUser();
                    }
                } catch (Exception e) {
                    Log.e("Welcome activity", "PermissionM: ", e);
                }
            }
        });
        profileImageView.setOnClickListener(view -> {
            BitmapDrawable drawable = (BitmapDrawable) profileImageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            smartSurveyApplication.setBitmap(bitmap);
            Intent intent = new Intent(RegistrationActivity.this, AvatarZoomActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    RegistrationActivity.this,
                    profileImageView,
                    ViewCompat.getTransitionName(profileImageView));
            startActivity(intent, options.toBundle());
        });


        countryCodeTextView.setOnClickListener(view -> {
            if (countryCodeDataList != null) {
                if (countryCodeDataList.size() > 0)
                    showCountryCodeDialog(countryCodeDataList);
            }
        });

        departmentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countryCodeDataList != null) {
                    if (countryCodeDataList.size() > 0)
                        showDepartmentDialog(departmentDataList);
                }
            }
        });

        firstNameEditText.addTextChangedListener(this);
        lastNameEditText.addTextChangedListener(this);
        emailIdEditText.addTextChangedListener(this);
        phoneNumberEditText.addTextChangedListener(this);
        createPasswordEditText.addTextChangedListener(this);
        confirmPasswordEditText.addTextChangedListener(this);

    }

    private void openDropdown() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            firstNameCardView.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeIn)
                    .duration(fadeInDuration)
                    .playOn(firstNameCardView);
            Handler handler1 = new Handler();
            handler1.postDelayed(() -> {
                lastNameCardView.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn)
                        .duration(fadeInDuration)
                        .playOn(lastNameCardView);
                Handler handler11 = new Handler();
                handler11.postDelayed(() -> {
                    emailIdCardView.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeIn)
                            .duration(fadeInDuration)
                            .playOn(emailIdCardView);
                    Handler handler111 = new Handler();
                    handler111.postDelayed(() -> {
                        phoneNumberCardView.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.FadeIn)
                                .duration(fadeInDuration)
                                .playOn(phoneNumberCardView);
                        Handler handler11114 = new Handler();
                        handler11114.postDelayed(() -> {
                            createPasswordCardView.setVisibility(View.VISIBLE);
                            YoYo.with(Techniques.FadeIn)
                                    .duration(fadeInDuration)
                                    .playOn(createPasswordCardView);
                            Handler handler11113 = new Handler();
                            handler11113.postDelayed(() -> {
                                confirmPasswordCardView.setVisibility(View.VISIBLE);
                                YoYo.with(Techniques.FadeIn)
                                        .duration(fadeInDuration)
                                        .playOn(confirmPasswordCardView);
                                Handler handler11112 = new Handler();
                                handler11112.postDelayed(() -> {
                                    departmentCardView.setVisibility(View.VISIBLE);
                                    YoYo.with(Techniques.FadeIn)
                                            .duration(fadeInDuration)
                                            .playOn(departmentCardView);
                                    Handler handler1111 = new Handler();
                                    handler1111.postDelayed(() -> {
                                        confirmButtonCardView.setVisibility(View.VISIBLE);
                                        YoYo.with(Techniques.FadeIn)
                                                .duration(fadeInDuration)
                                                .playOn(confirmButtonCardView);

                                    }, delayDuration);
                                }, delayDuration);
                            }, delayDuration);
                        }, delayDuration);
                    }, delayDuration);
                }, delayDuration);
            }, delayDuration);
        }, 200);
    }

    private void checkPermission() {
        try {
            String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
            if (ApplicationPermission.hasPermissions(RegistrationActivity.this, PERMISSIONS)) {
                itemNewImageCapture();
            } else {
                ApplicationPermission.setStoragePermission(RegistrationActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void itemNewImageCapture() {

        mBottomSheetDialog = new BottomSheetDialog(RegistrationActivity.this);
        View view = View.inflate(this, R.layout.choose_avatar_dialog, null);

        mBottomSheetDialog.setContentView(view);
        if (mBottomSheetDialog.getWindow() != null) {
            mBottomSheetDialog.getWindow().setLayout((int) (width / 1.40), (int) (height / 1.20));
        }
        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(dialog -> mBottomSheetDialog = null);

        LinearLayout layoutCamera = mBottomSheetDialog.findViewById(R.id.layoutCamera);
        LinearLayout layoutGallery = mBottomSheetDialog.findViewById(R.id.layoutGallery);
        LinearLayout layoutAvatar = mBottomSheetDialog.findViewById(R.id.layoutAvatar);

        if (layoutGallery != null) {
            layoutGallery.setOnClickListener(v -> {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, AppConfiguration.PICK_IMAGE_REQUEST);
                mBottomSheetDialog.dismiss();
            });
        }
        if (layoutCamera != null) {
            layoutCamera.setOnClickListener(v -> {
                try {
                    file = TempFile.getFilePath();
                    System.out.println("file = " + file);

                    Uri uriForFile = FileProvider.getUriForFile(RegistrationActivity.this,
                            AppConfiguration.APPLICATION_ID + ".provider",
                            file);

                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
                    intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
                    startActivityForResult(intent, AppConfiguration.IMAGE_CAPTURE);
                    mBottomSheetDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        if (layoutAvatar != null) {
            layoutAvatar.setOnClickListener(view1 -> {
                startActivityForResult(new Intent(this, AvatarListActivity.class),
                        AppConfiguration.READ_AVATAR_RESULT);
                mBottomSheetDialog.dismiss();
            });

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == AppConfiguration.IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (file != null) {
                try {
                    filePath = ImageCompression.compressImage(String.valueOf(file), RegistrationActivity.this);
                } catch (Exception e) {
                    Log.e("AddNewPro", "onActivityResult: ", e);
                }
            }
            imageDisplay(filePath);
        } else if (requestCode == AppConfiguration.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            try {
                filePath = ImageCompression.compressImage(String.valueOf(data.getData()), RegistrationActivity.this);
            } catch (Exception e) {
                Log.e("AddNewPro", "onActivityResult: ", e);
            }
            imageDisplay(filePath);
        } else if (requestCode == AppConfiguration.READ_AVATAR_RESULT && resultCode == RESULT_OK
                && data != null && data.getExtras() != null) {

            DownloadImageFromServer(data.getStringExtra(AppConfiguration.DATA_URL));
        }


    }

    private void imageDisplay(String filePath) {
        try {
            if (filePath != null) {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(RegistrationActivity.this.getContentResolver(), Uri.fromFile(
                        new File(filePath)));
                profileImageView.setImageBitmap(bitmap);
                profileImageBase64 = getEncoded64ImageStringFromBitmap(bitmap);
            }
        } catch (Exception e) {
            Log.e("AddNewPro", "onActivityResult: ", e);
        }
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP);
    }

    private boolean validatePersonalDetails() {
        boolean is_validate = true;
        if ((firstNameEditText.getText().toString().length() == 0) ||
                (firstNameEditText.getText().toString().replaceAll("\\s", "").length() == 0)) {
            showErrorShake(firstNameCardView);
            is_validate = false;
        }
        if ((lastNameEditText.getText().toString().length() == 0) ||
                (lastNameEditText.getText().toString().replaceAll("\\s", "").length() == 0)) {
            showErrorShake(lastNameCardView);
            is_validate = false;
        }
        if (emailIdEditText.getText().toString().isEmpty() || !isPatternMathes()) {
            showErrorShake(emailIdCardView);
            is_validate = false;
        }
        if (phoneNumberEditText.getText().toString().length() == 0 ||
                phoneNumberEditText.getText().toString().length() < 9) {
            showErrorShake(phoneNumberCardView);
            is_validate = false;
        }
        if ((createPasswordEditText.getText().toString().length() == 0) ||
                (createPasswordEditText.getText().toString().replaceAll("\\s", "").length() == 0)) {
            showErrorShake(createPasswordCardView);
            is_validate = false;
        }

        if (!confirmPasswordEditText.getText().toString().equals(createPasswordEditText.getText().toString())) {
            showErrorShake(confirmPasswordCardView);
            is_validate = false;
        }
        if (departmentId == 0) {
            showErrorShake(departmentCardView);
            is_validate = false;
        }
        return is_validate;

    }

    private void registerUser() {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            AlertDialogCustom.noInternetAlertDialog(this);
            return;
        }
        final CustomProgress progressDialog = new CustomProgress(RegistrationActivity.this);
        progressDialog.show();
        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(RegistrationActivity.this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();
        Call<AuthenticationModel> registerUser = retrofitService.registerUser(
                jsonForRegistration(firstNameEditText.getText().toString(),
                        lastNameEditText.getText().toString(),
                        profileImageBase64,
                        emailIdEditText.getText().toString(),
                        phoneNumberEditText.getText().toString(),
                        createPasswordEditText.getText().toString()));
        registerUser.enqueue(new Callback<AuthenticationModel>() {
            @Override
            public void onResponse(Call<AuthenticationModel> call, Response<AuthenticationModel> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 6000) {

                        UserDataTableQueries userDataTableQueries = UserDataTableQueries.sharedInstance(getApplicationContext());
                        userDataTableQueries.deleteAllUserDetails();

                        AuthenticationModel registration = new AuthenticationModel();
                        registration.setData(response.body().getData());

                        userDataTableQueries.insertUserDetails(registration);

                        categoriesTableQueries = categoriesTableQueries.sharedInstance(RegistrationActivity.this);
                        topPlayersTableQueries = topPlayersTableQueries.sharedInstance(RegistrationActivity.this);
                        categoriesTableQueries.deleteAllCategories();
                        topPlayersTableQueries.deleteAllTopPlayers();

                        for (int i = 0; i < response.body().getData().getUser_categories().size(); i++) {
                            final CategoriesSqliteModel categoriesSqliteModel = new CategoriesSqliteModel();
                            categoriesSqliteModel.setEn_category_id(response.body().getData().getUser_categories().get(i).getEn_category_id());
                            categoriesSqliteModel.setCategory_color(response.body().getData().getUser_categories().get(i).getCategory_color());
                            categoriesSqliteModel.setCategory_name(response.body().getData().getUser_categories().get(i).getCategory_name());
                            categoriesSqliteModel.setCrown(response.body().getData().getUser_categories().get(i).getCrown());
                            categoriesSqliteModel.setCrown_image(response.body().getData().getUser_categories().get(i).getCrown_image());
                            categoriesSqliteModel.setLevel_count(response.body().getData().getUser_categories().get(i).getLevel_count());
                            categoriesSqliteModel.setIs_followed(response.body().getData().getUser_categories().get(i).getIs_followed());
                            categoriesSqliteModel.setImage_path(response.body().getData().getUser_categories().get(i).getImage_path());

                            if (!categoriesTableQueries.insertAllTopics(categoriesSqliteModel))
                                Toast.makeText(RegistrationActivity.this, "Something went wrong. Please restart .", Toast.LENGTH_LONG).show();
                            for (int j = 0; j < response.body().getData().getUser_categories().get(i).getTop_players().size(); j++) {
                                int categorId = response.body().getData().getUser_categories().get(i).getEn_category_id();
                                String firstName = response.body().getData().getUser_categories().get(i).getTop_players().get(j).getFirstname();
                                String lastName = response.body().getData().getUser_categories().get(i).getTop_players().get(j).getLastname();
                                int userId = response.body().getData().getUser_categories().get(i).getTop_players().get(j).getUser_id();
                                int totalMark = response.body().getData().getUser_categories().get(i).getTop_players().get(j).getTotal_mark();
                                int rank;
                                if (j == 0)
                                    rank = 1;
                                else
                                    rank = 2;
                                String imagePath = "http://e-learning.kawikatech.com/data/profile/" + response.body().getData().getUser_categories().get(i).getTop_players().get(j).getImage();
                                String categoryName = response.body().getData().getUser_categories().get(i).getTop_players().get(j).getCategory_name();
                                topPlayersTableQueries.insertTopPlayers(categorId, firstName, lastName, userId, totalMark, rank, imagePath, categoryName);
                            }

                        }

                        if (response.body().getData().getChallengeNotification() != null) {
                            quickChallengeTableQueries = QuickChallengeTableQueries.sharedInstance(getApplicationContext());
                            quickChallengeTableQueries.deleteAllQuickChallenge();
                            quickChallengeTableQueries.insertQuickChallenge(response.body().getData().getChallengeNotification().getIs_available(), response.body().getData().getChallengeNotification().getMessage());
                        }

                        appPreferences.saveData(AppConfiguration.MY_TOKEN, response.body().getData().getDevice_id());
                        appPreferences.saveData(AppConfiguration.USER_ID, String.valueOf(response.body().getData().getId()));

                        //Intent call
                        Intent intent = new Intent(RegistrationActivity.this, OtpValidationActivity.class);
                        intent.putExtra(AppConfiguration.PAGE_IDENTIFIER, 1);
                        intent.putExtra(AppConfiguration.USER_ID, response.body().getData().getId());
                        startActivity(intent);
                        finish();

                    } else {
                        AlertDialogCustom.commonAlertDialog(RegistrationActivity.this, response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<AuthenticationModel> call, Throwable t) {
                System.out.println("t = " + t.getMessage());
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(RegistrationActivity.this, getString(R.string.went_wrong_wait));
            }

        });
    }

    public RegistrationRawCreation jsonForRegistration(String firstName, String lastName, String profileImage,
                                                       String email, String phone, String password) {
        try {
            RegistrationRawCreation registrationModel = new RegistrationRawCreation();
            registrationModel.setFirstname(firstName);
            registrationModel.setLastname(lastName);
            registrationModel.setProfile_image(profileImage);
            registrationModel.setEmail(email);
            registrationModel.setPhone(countryCodeTextView.getText().toString() + phone);
            registrationModel.setPassword(password);
            registrationModel.setLanguages_language_id(appPreferences.getInt(AppConfiguration.LANGUAGE_ID));
            registrationModel.setDepartments_department_id(departmentId);
            registrationModel.setDevice_id(generateToken());
            System.out.println("response.body() " + new Gson().toJson(registrationModel).toString());

            return registrationModel;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void showErrorShake(CardView cardView) {
        YoYo.with(Techniques.Shake)
                .duration(800)
                .playOn(cardView);
        cardView.setBackground(ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.edit_text_error_border));

    }

    @Override
    public void onResume() {
        super.onResume();
        gravityView.registerListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        gravityView.unRegisterListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_AND_WRITE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                itemNewImageCapture();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                    AlertDialogCustom.permissionDialog(RegistrationActivity.this, getString(R.string.storage_permission));
                }
            }
        }
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registerUser();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                    AlertDialogCustom.permissionDialog(RegistrationActivity.this, "Turn on READ PHONE Permission from SETTINGS --> PERMISSIONS");
                } else {
                    alertDialog(RegistrationActivity.this, "You must accept PHONE permission to continue", READ_PHONE_ALERT);

                }
            }
        }
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        if (firstNameEditText.getText().hashCode() == editable.hashCode()) {
            if ((firstNameEditText.getText().toString().length() == 0) || (firstNameEditText.getText().toString().replaceAll("\\s", "").length() == 0)) {
                firstNameEditText.setBackground(ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.edit_text_error_border));
            } else {
                firstNameEditText.setBackground(ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.edit_text_valid_border));
            }
        } else if (lastNameEditText.getText().hashCode() == editable.hashCode()) {
            if ((lastNameEditText.getText().toString().length() == 0) || (lastNameEditText.getText().toString().replaceAll("\\s", "").length() == 0)) {
                lastNameEditText.setBackground(ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.edit_text_error_border));
            } else {
                lastNameEditText.setBackground(ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.edit_text_valid_border));
            }
        } else if (emailIdEditText.getText().hashCode() == editable.hashCode()) {
            if (!isPatternMathes()) {
                emailIdEditText.setBackground(ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.edit_text_error_border));

            } else {
                emailIdEditText.setBackground(ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.edit_text_valid_border));
            }

        } else if (phoneNumberEditText.getText().hashCode() == editable.hashCode()) {
            if (phoneNumberEditText.getText().toString().length() == 0 || phoneNumberEditText.getText().toString().length() < 9) {
                phoneNumberCardView.setBackground(ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.edit_text_error_border));
            } else {
                phoneNumberCardView.setBackground(ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.edit_text_valid_border));
            }
        } else if (createPasswordEditText.getText().hashCode() == editable.hashCode()) {
            if ((createPasswordEditText.getText().toString().length() == 0) || (createPasswordEditText.getText().toString().replaceAll("\\s", "").length() == 0)) {
                createPasswordEditText.setBackground(ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.edit_text_error_border));
            } else {
                createPasswordEditText.setBackground(ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.edit_text_valid_border));
            }
        } else if (confirmPasswordEditText.getText().hashCode() == editable.hashCode()) {
            if (!createPasswordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
                confirmPasswordEditText.setBackground(ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.edit_text_error_border));

            } else {
                confirmPasswordEditText.setBackground(ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.edit_text_valid_border));
            }
        }
    }

    private boolean isPatternMathes() {
        boolean valid = false;
        String emailPattern1 = "[a-zA-Z0-9._-]+@dubaided+\\.gov+\\.+ae+";
        String emailPattern2 = "[a-zA-Z0-9._-]+@sme+\\.+ae+";
        String emailPattern3 = "[a-zA-Z0-9._-]+@dedc+\\.gov+\\.+ae+";
        String emailPattern4 = "[a-zA-Z0-9._-]+@fdi+\\.gov+\\.+ae+";
        String emailPattern5 = "[a-zA-Z0-9._-]+@thefund+\\.+ae+";
        String gmail = "[a-zA-Z0-9._-]+@gmail+\\.+com+";

        Pattern patternOne = Pattern.compile(emailPattern1 + ".*", Pattern.CASE_INSENSITIVE);
        Matcher m1 = patternOne.matcher(emailIdEditText.getText().toString());

        Pattern patternTwo = Pattern.compile(emailPattern2 + ".*", Pattern.CASE_INSENSITIVE);
        Matcher m2 = patternTwo.matcher(emailIdEditText.getText().toString());

        Pattern patternThree = Pattern.compile(emailPattern3 + ".*", Pattern.CASE_INSENSITIVE);
        Matcher m3 = patternThree.matcher(emailIdEditText.getText().toString());

        Pattern patternFour = Pattern.compile(emailPattern4 + ".*", Pattern.CASE_INSENSITIVE);
        Matcher m4 = patternFour.matcher(emailIdEditText.getText().toString());

        Pattern patternFive = Pattern.compile(emailPattern5 + ".*", Pattern.CASE_INSENSITIVE);
        Matcher m5 = patternFive.matcher(emailIdEditText.getText().toString());

        Pattern patternSix = Pattern.compile(gmail + ".*", Pattern.CASE_INSENSITIVE);
        Matcher m6 = patternSix.matcher(emailIdEditText.getText().toString());

        if (m1.find()) {
            valid = true;
        }
        if (m2.find()) {
            valid = true;
        }
        if (m3.find()) {
            valid = true;
        }
        if (m4.find()) {
            valid = true;
        }
        if (m5.find()) {
            valid = true;
        }
        if (m6.find()) {
            valid = true;
        }

        return valid;
    }

    private void getCountryCodes() {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            AlertDialogCustom.noInternetAlertDialog(this);
            return;
        }
        progressDialog.show();

        final SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(RegistrationActivity.this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();
        Call<CountryCodeModel> countryCodeModelCall = retrofitService.getCountryCodes();

        countryCodeModelCall.enqueue(new Callback<CountryCodeModel>() {
            @Override
            public void onResponse(Call<CountryCodeModel> call, Response<CountryCodeModel> response) {

                if (response.code() == 200) {
                    if (response.body().getStatus() == 6000) {
                        countryCodeDataList = response.body().getData();
                    } else {
                        AlertDialogCustom.commonDialogWithFinish(RegistrationActivity.this, response.body().getMessage());
                    }
                }
                getDepartment();
            }

            @Override
            public void onFailure(Call<CountryCodeModel> call, Throwable t) {
                System.out.println("t = " + t.getMessage());
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(RegistrationActivity.this, "Please restart the app or retry");
            }

        });

    }

    public void showCountryCodeDialog(List<CountryCodeModel.DataBean> countryCodeDataList) {

        final Dialog dialog = new Dialog(RegistrationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.country_codes_dialog);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        TextView dropdownDialogTextView = dialog.findViewById(R.id.dropdownDialogTextView);
        RecyclerView countryCodeRecyclerView = dialog.findViewById(R.id.countryCodeRecyclerView);
        countryCodeRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(RegistrationActivity.this);
        countryCodeRecyclerView.setLayoutManager(mLayoutManager);
        dropdownDialogTextView.setText(getString(R.string.country_codes));

        RecyclerView.Adapter countryCodeAdapter = new CountryCodesAdapter(countryCodeDataList, RegistrationActivity.this, dialog);
        countryCodeRecyclerView.setAdapter(countryCodeAdapter);
        dialog.show();
    }

    public void showDepartmentDialog(List<DepartmentModel.DataBean> departmentDataList) {

        final Dialog dialog = new Dialog(RegistrationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.country_codes_dialog);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        TextView dropdownDialogTextView = dialog.findViewById(R.id.dropdownDialogTextView);
        RecyclerView countryCodeRecyclerView = dialog.findViewById(R.id.countryCodeRecyclerView);
        countryCodeRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(RegistrationActivity.this);
        countryCodeRecyclerView.setLayoutManager(mLayoutManager);

        dropdownDialogTextView.setText(getString(R.string.select_department));
        RecyclerView.Adapter departMentAdapter = new DepartmentAdapter(departmentDataList, RegistrationActivity.this, dialog);
        countryCodeRecyclerView.setAdapter(departMentAdapter);
        dialog.show();
    }

    private void getDepartment() {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            AlertDialogCustom.noInternetAlertDialog(this);
            return;
        }
        final AppPreferences appPreferences = AppPreferences.getInstance(RegistrationActivity.this, AppConfiguration.SMART_SURVEY_PREFS);

        final SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(RegistrationActivity.this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();
        Call<DepartmentModel> departmentModelCall = retrofitService.getDepartment(appPreferences.getInt(AppConfiguration.LANGUAGE_ID));

        departmentModelCall.enqueue(new Callback<DepartmentModel>() {
            @Override
            public void onResponse(Call<DepartmentModel> call, Response<DepartmentModel> response) {
                progressDialog.dismiss();

                if (response.code() == 200) {
                    //Api response success ,6000
                    if (response.body().getStatus() == 6000) {
                        departmentDataList = response.body().getData();

                    } else if (response.body().getStatus() == 6004) {
                        AlertDialogCustom.sessionExpiredDialog(RegistrationActivity.this);
                    } else {
                        //fail alert
                        AlertDialogCustom.commonDialogWithFinish(RegistrationActivity.this, response.body().getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<DepartmentModel> call, Throwable t) {
                System.out.println("t = " + t.getMessage());
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(RegistrationActivity.this, "Please restart the app or retry");
            }

        });

    }


    private void DownloadImageFromServer(String url) {
        imageProgressBar.setVisibility(View.VISIBLE);
        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();

        Call<ResponseBody> call = retrofitService.fetchImageFromServer(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    imageProgressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null && response.body().byteStream() != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
                        profileImageView.setImageBitmap(bitmap);
                        profileImageBase64 = getEncoded64ImageStringFromBitmap(bitmap);
                    } else {
                        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                                R.drawable.no_resource);
                        profileImageView.setImageBitmap(icon);
                        profileImageBase64 = "noImage";
                    }
                } catch (NullPointerException np) {
                    np.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                imageProgressBar.setVisibility(View.GONE);
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.no_resource);
                profileImageView.setImageBitmap(icon);
                profileImageBase64 = "noImage";
            }
        });
    }

    private long generateToken() {
        return System.currentTimeMillis();
    }


    public void updateSelectedCountryCode(String selectedCountryCode) {
        countryCodeTextView.setText(selectedCountryCode);
    }

    public void updateSelectedDepartment(int departmentId, String selectedDepartment) {
        departmentTextView.setText(selectedDepartment);
        this.departmentId = departmentId;
        departmentCardView.setBackground(ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.edit_text_valid_border));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        smartSurveyApplication.setBitmap(null);
    }
}
