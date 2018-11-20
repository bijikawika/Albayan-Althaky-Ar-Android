package com.kawika.smart_survey.views;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.alerts.AlertDialogCustom;
import com.kawika.smart_survey.api_services.RetrofitService;
import com.kawika.smart_survey.application.SmartSurveyApplication;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.database.CategoriesTableQueries;
import com.kawika.smart_survey.database.QuickChallengeTableQueries;
import com.kawika.smart_survey.database.UserDataTableQueries;
import com.kawika.smart_survey.databinding.HomeActivityBinding;
import com.kawika.smart_survey.databinding.MyScoreActivityBinding;
import com.kawika.smart_survey.databinding.NotificationActivityBinding;
import com.kawika.smart_survey.models.BasicRetroCallModel;
import com.kawika.smart_survey.models.CategoriesSqliteModel;
import com.kawika.smart_survey.models.CommonModel;
import com.kawika.smart_survey.models.ProfileUpdateResponse;
import com.kawika.smart_survey.models.UserDetailsModel;
import com.kawika.smart_survey.networkCheck.NetworkCheck;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.services.BackgroundMusicService;
import com.kawika.smart_survey.utils.ApplicationPermission;
import com.kawika.smart_survey.utils.CustomProgress;
import com.kawika.smart_survey.utils.ImageCompression;
import com.kawika.smart_survey.utils.PlaySound;
import com.kawika.smart_survey.utils.TempFile;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kawika.smart_survey.config.AppConfiguration.CAMERA_AND_WRITE_REQUEST_CODE;
import static com.kawika.smart_survey.database.CategoriesTableQueries.categoriesTableQueries;
import static com.kawika.smart_survey.database.QuickChallengeTableQueries.quickChallengeTableQueries;
import static com.kawika.smart_survey.database.TopPlayersTableQueries.topPlayersTableQueries;

/*
 * Created by senthiljs on 09/02/18.
 */

public class SettingsActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private AppPreferences appPreferences;
    private CircleImageView profileImageView;
    private TextView nameDisplay;
    private BottomSheetDialog mBottomSheetDialog;
    private ProgressBar imageProgressBar;
    private String profileImageBase64 = "noImage";
    private File file = null;
    private String filePath = null;
    private String firstName;
    private String lastName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileImageView = findViewById(R.id.profileImageView);
        nameDisplay = findViewById(R.id.nameDisplay_text);
        imageProgressBar = findViewById(R.id.imageProgressBar);
        initiateToolBar();

    }

    @Override
    int getContentViewId() {
        return R.layout.activity_settings;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.menu_settings;
    }

    @Override
    HomeActivityBinding getHomeBinding() {
        return null;
    }

    @Override
    MyScoreActivityBinding getMyScoreActivityBinding() {
        return null;
    }

    @Override
    NotificationActivityBinding getNotificationBinding() {
        return null;
    }

    private void initiateToolBar() {

        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        //back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        //widget declaration
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        Switch pushNotificationButton = findViewById(R.id.pushNotificationButton);
        Switch soundEffectButton = findViewById(R.id.soundEffectButton);
        Switch gameMusicButton = findViewById(R.id.gameMusicButton);
        //other declarations
        appPreferences = AppPreferences.getInstance(this, AppConfiguration.SMART_SURVEY_PREFS);
        toolbar_title.setText(getString(R.string.settings));
        pushNotificationButton.setChecked(appPreferences.getBoolean(AppConfiguration.IS_PUSH_NOTIFICATION_SET));
        soundEffectButton.setChecked(appPreferences.getBoolean(AppConfiguration.IS_SOUND_EFFECTS_SET));
        gameMusicButton.setChecked(appPreferences.getBoolean(AppConfiguration.IS_GAME_MUSIC_SET));
        //image display
        setImageData();
        //event
        pushNotificationButton.setOnCheckedChangeListener(this);
        soundEffectButton.setOnCheckedChangeListener(this);
        gameMusicButton.setOnCheckedChangeListener(this);
    }

    /**
     * image display
     */
    private void setImageData() {
        //user name
        UserDataTableQueries userDataTableQueries = UserDataTableQueries.sharedInstance(this);
        firstName = userDataTableQueries.getName();
        lastName = userDataTableQueries.getLastName();
        String userName = firstName + " " + lastName;
        nameDisplay.setText(userName);

        if (userDataTableQueries.getProfileImagePath() != null) {
            Picasso.with(SettingsActivity.this)
                    .load(userDataTableQueries.getProfileImagePath())
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(profileImageView);
        }
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
        if (view.getId() == R.id.aboutUsLayout) {
            //about us click event
            try {
                PlaySound.buttonClickSound(this, R.raw.button_click_sound);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(AppConfiguration.ABOUT_US_URL));
                startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (view.getId() == R.id.logoutLayout) {
            //logout
            PlaySound.buttonClickSound(this, R.raw.button_click_sound);
            logoutDialog();
        } else if (view.getId() == R.id.profileImageView) {
            //Profile image edit
            PlaySound.buttonClickSound(this, R.raw.button_click_sound);
            EditProfileImage();
        } else if (view.getId() == R.id.nameEdit) {
            //user image edit
            PlaySound.buttonClickSound(this, R.raw.button_click_sound);
            EditTextLayoutAlert();
        }
    }

    /**
     * profile image edit
     */
    private void EditProfileImage() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //permission checker
                checkPermission();
            } else {
                //image capture
                itemNewImageCapture();
            }
        } catch (Exception e) {
            Log.e("Welcome activity", "PermissionM: ", e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_AND_WRITE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                itemNewImageCapture();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                    AlertDialogCustom.permissionDialog(this, getString(R.string.storage_permission));
                }
            }
        }
    }

    /**
     * permission checker
     */
    private void checkPermission() {
        try {
            String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
            if (ApplicationPermission.hasPermissions(this, PERMISSIONS)) {
                itemNewImageCapture();
            } else {
                ApplicationPermission.setStoragePermission(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * onActivityResult for all data result
     *
     * @param requestCode code
     * @param resultCode  result code
     * @param data        data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == AppConfiguration.IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (file != null) {
                try {
                    filePath = ImageCompression.compressImage(String.valueOf(file), this);
                } catch (Exception e) {
                    Log.e("AddNewPro", "onActivityResult: ", e);
                }
            }
            //crop the method
            cropActionMethod(filePath);
        } else if (requestCode == AppConfiguration.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            try {
                filePath = ImageCompression.compressImage(String.valueOf(data.getData()), this);
            } catch (Exception e) {
                Log.e("AddNewPro", "onActivityResult: ", e);
            }
            //crop method
            cropActionMethod(filePath);
        } else if (requestCode == AppConfiguration.READ_AVATAR_RESULT && resultCode == RESULT_OK
                && data != null && data.getExtras() != null) {
            //download image from server
            DownloadImageFromServer(data.getStringExtra(AppConfiguration.DATA_URL));
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            //crop image out put
            if (data != null) {
                final Uri resultUri = UCrop.getOutput(data);
                imageDisplay(new File(resultUri.getPath()).toString());
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
//            System.out.println("requestCode = " + UCrop.getError(data));
        }


    }

    /**
     * crop methos
     *
     * @param filePath path
     */
    private void cropActionMethod(String filePath) {
        UCrop.of(Uri.fromFile(new File(filePath)), Uri.fromFile(new File(filePath)))
                .withAspectRatio(4, 4)
                .withMaxResultSize(400, 400)
                .start(this);
    }

    /**
     * image display
     *
     * @param filePath image path
     */
    private void imageDisplay(String filePath) {
        try {
            if (filePath != null) {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(
                        new File(filePath)));
                profileImageView.setImageBitmap(bitmap);
                profileImageBase64 = getEncoded64ImageStringFromBitmap(bitmap);
                postProfileDataUpdate(true);
            }
        } catch (Exception e) {
            Log.e("AddNewPro", "onActivityResult: ", e);
        }
    }

    /**
     * post profile Image and user name data
     */
    private void postProfileDataUpdate(boolean isImage) {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            AlertDialogCustom.noInternetAlertDialog(this);
            return;
        }
        //userId
        String userId = appPreferences.getData(AppConfiguration.USER_ID);

        final CustomProgress progressDialog = new CustomProgress(this);
        progressDialog.show();

        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();

        Call<ProfileUpdateResponse> categoriesCall;
        //checking profile image or user name
        if (isImage) {
            categoriesCall = retrofitService.userUpdateProfile(appPreferences.getData(AppConfiguration.MY_TOKEN),
                    userId,
                    null, null,
                    profileImageBase64);
        } else {
            categoriesCall = retrofitService.userUpdateProfile(appPreferences.getData(AppConfiguration.MY_TOKEN),
                    userId,
                    firstName,
                    lastName,
                    null);
        }

        categoriesCall.enqueue(new Callback<ProfileUpdateResponse>() {
            @Override
            public void onResponse(Call<ProfileUpdateResponse> call, Response<ProfileUpdateResponse> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 6000) {
                        //response update
                        UserDataTableQueries userDataTableQueries = UserDataTableQueries.sharedInstance(getApplicationContext());
                        userDataTableQueries.updateFirstnameAndLastName(userId, response.body(), lastName, firstName);
                    } else {
                        AlertDialogCustom.commonAlertDialog(SettingsActivity.this, response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileUpdateResponse> call, Throwable t) {
                System.out.println("t = " + t.getMessage());
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(SettingsActivity.this, getString(R.string.went_wrong_wait));
            }

        });
    }

    //bit map convert too base 64
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP);
    }

    /**
     * image capture
     */
    private void itemNewImageCapture() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        mBottomSheetDialog = new BottomSheetDialog(this);
        View view = View.inflate(this, R.layout.choose_avatar_dialog, null);

        mBottomSheetDialog.setContentView(view);
        //getWindow() null checking
        if (mBottomSheetDialog.getWindow() != null) {
            mBottomSheetDialog.getWindow().setLayout((int) (width / 1.40), (int) (height / 1.20));
        }
        mBottomSheetDialog.show();
        //bottom layout dismiss event
        mBottomSheetDialog.setOnDismissListener(dialog -> mBottomSheetDialog = null);

        LinearLayout layoutCamera = mBottomSheetDialog.findViewById(R.id.layoutCamera);
        LinearLayout layoutGallery = mBottomSheetDialog.findViewById(R.id.layoutGallery);
        LinearLayout layoutAvatar = mBottomSheetDialog.findViewById(R.id.layoutAvatar);

        //layout null checking
        if (layoutGallery != null) {
            //click event
            layoutGallery.setOnClickListener(v -> {
                PlaySound.buttonClickSound(this, R.raw.button_click_sound);
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, AppConfiguration.PICK_IMAGE_REQUEST);
                mBottomSheetDialog.dismiss();
            });
        }
        //layout null checking
        if (layoutCamera != null) {
            //click event
            layoutCamera.setOnClickListener(v -> {
                try {
                    PlaySound.buttonClickSound(this, R.raw.button_click_sound);
                    file = TempFile.getFilePath();
                    System.out.println("file = " + file);

                    Uri uriForFile = FileProvider.getUriForFile(this,
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
        //layout null checking
        if (layoutAvatar != null) {
            layoutAvatar.setOnClickListener(view1 -> {
                PlaySound.buttonClickSound(this, R.raw.button_click_sound);
                // avatar activity
                startActivityForResult(new Intent(this, AvatarListActivity.class),
                        AppConfiguration.READ_AVATAR_RESULT);
                mBottomSheetDialog.dismiss();
            });

        }
    }

    /**
     * download image from server
     *
     * @param url : url
     */
    private void DownloadImageFromServer(String url) {
        //progress visible
        imageProgressBar.setVisibility(View.VISIBLE);
        // #library : Retrofit
        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();

        Call<ResponseBody> call = retrofitService.fetchImageFromServer(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //progress visible
                    imageProgressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null && response.body().byteStream() != null) {
                        // display the image data in a ImageView or save it
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
                    postProfileDataUpdate(true);
                } catch (NullPointerException np) {
                    np.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //progress visible
                imageProgressBar.setVisibility(View.GONE);
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.no_resource);
                profileImageView.setImageBitmap(icon);
                profileImageBase64 = "noImage";
                postProfileDataUpdate(true);
            }
        });
    }

    /**
     * function of edit text
     */
    private void EditTextLayoutAlert() {

        final Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogbox_enter_name);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        final EditText editTextFistName = dialog.findViewById(R.id.editTextFistName);
        final EditText editTextLastName = dialog.findViewById(R.id.editTextLastName);
        final TextView okTextView = dialog.findViewById(R.id.okTextView);
        final TextView cancelTextView = dialog.findViewById(R.id.cancelTextView);

        editTextFistName.setText(firstName);
        editTextLastName.setText(lastName);
        editTextFistName.setSelection(editTextFistName.getText().length());

        okTextView.setOnClickListener(view -> {
            PlaySound.buttonClickSound(this, R.raw.button_click_sound);

            if (editTextFistName.getText().toString().replace(" ", "").length() != 0) {
                if (!editTextFistName.getText().toString().equals(firstName) ||
                        !editTextLastName.getText().toString().equals(lastName)) {
                    //updating data to ui
                    firstName = editTextFistName.getText().toString();
                    lastName = editTextLastName.getText().toString();
                    String userName = firstName + " " + lastName;
                    nameDisplay.setText(userName);

                    //posting user updates
                    postProfileDataUpdate(false);

                }
                dialog.dismiss();
            } else {
                editTextFistName.setError(getResources().getString(R.string.firstname_cannot_empty));
            }
        });
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    /**
     * logout conformation dialog
     */
    private void logoutDialog() {
        //dialog
        android.app.AlertDialog.Builder aDialog = new android.app.AlertDialog.Builder(this,
                android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        aDialog.setMessage(R.string.logout_account_dialog);
        aDialog.setPositiveButton(R.string.yes, (arg0, arg1) -> {
            //delete web call
            postLogoutToServer();
        });
        aDialog.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
        android.app.AlertDialog alertDialog = aDialog.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    /**
     * logout service
     */
    private void postLogoutToServer() {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            AlertDialogCustom.noInternetAlertDialog(SettingsActivity.this);
            return;
        }
        final CustomProgress progressDialog = new CustomProgress(this);
        progressDialog.show();

        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();

        Call<CommonModel> categoriesCall = retrofitService.logOut(appPreferences.getData(AppConfiguration.MY_TOKEN),
                appPreferences.getData(AppConfiguration.USER_ID));

        categoriesCall.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 6000) {
                        appPreferences.clearData();
                        //stop music
                        BackgroundMusicService.StopService(SettingsActivity.this);
                        //activity relaunch
                        Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    } else if (response.body().getStatus() == 6004) {
                        AlertDialogCustom.sessionExpiredDialog(SettingsActivity.this);
                    } else {
                        AlertDialogCustom.commonAlertDialog(SettingsActivity.this, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonModel> call, Throwable t) {
                System.out.println("t = " + t.getMessage());
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(SettingsActivity.this, getString(R.string.went_wrong_wait));
            }

        });
    }

    /**
     * switch click
     *
     * @param compoundButton button
     * @param isChecked      boolean value
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (compoundButton.getId() == R.id.pushNotificationButton) {
            //push enable /disable value
            int selectedValue = 0;
            if (isChecked) {
                selectedValue = 1;
            }

            quickChallengePermissionSet(selectedValue);
            //api post
//            PushEnableValuePostToServer(selectedValue);

        } else if (compoundButton.getId() == R.id.soundEffectButton) {
            //setting push notification state put in to shared preference
            appPreferences.saveBoolean(AppConfiguration.IS_SOUND_EFFECTS_SET, isChecked);
            if (isChecked)
                PlaySound.buttonClickSound(this, R.raw.button_click_sound);

        } else if (compoundButton.getId() == R.id.gameMusicButton) {
            //setting push notification state put in to shared preference
            appPreferences.saveBoolean(AppConfiguration.IS_GAME_MUSIC_SET, isChecked);
            //playing sound in background
            if (isChecked) {
                BackgroundMusicService.StartService(this);
            } else {
                BackgroundMusicService.StopService(this);
            }
        }
    }

    private void getUserDetails() {
        final CustomProgress progressDialog = new CustomProgress(this);
        progressDialog.show();
        categoriesTableQueries = CategoriesTableQueries.sharedInstance(SettingsActivity.this);
        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();

        Call<UserDetailsModel> userDetailsModelCall = retrofitService.getUserDetails(appPreferences.getData(AppConfiguration.MY_TOKEN),
                appPreferences.getData(AppConfiguration.USER_ID));
        userDetailsModelCall.enqueue(new Callback<UserDetailsModel>() {
            @Override
            public void onResponse(Call<UserDetailsModel> call, Response<UserDetailsModel> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 6000) {
                        onDataFetchSuccess(response.body().getData());
                        finish();
                        startActivity(getIntent());
                    } else if (response.body().getStatus() == 6004) {
                        AlertDialogCustom.sessionExpiredDialog(SettingsActivity.this);
                    } else {
                        AlertDialogCustom.commonAlertDialog(SettingsActivity.this, response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<UserDetailsModel> call, Throwable t) {
                System.out.println("t = " + t.getMessage());
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(SettingsActivity.this, getString(R.string.went_wrong_wait));

            }

        });
    }

    private void quickChallengePermissionSet(int permissionCode) {
        NetworkCheck networkCheck = new NetworkCheck(this);
        if (!networkCheck.isConnectingToInternet()) {
            AlertDialogCustom.noInternetAlertDialog(this);
            return;
        }
        final CustomProgress progressDialog = new CustomProgress(this);
        progressDialog.show();

        quickChallengeTableQueries = QuickChallengeTableQueries.sharedInstance(getApplicationContext());

        SmartSurveyApplication smartSurveyApplication = SmartSurveyApplication.get(this);
        RetrofitService retrofitService = smartSurveyApplication.getRetrofitService();
        System.out.println("FirebaseInstanceId.getInstance().getToken() = " + FirebaseInstanceId.getInstance().getToken());

        Call<BasicRetroCallModel> categoriesCall = retrofitService.setPushPermission(appPreferences.getData(AppConfiguration.MY_TOKEN),
                appPreferences.getData(AppConfiguration.USER_ID), permissionCode, FirebaseInstanceId.getInstance().getToken(), "quick");
        categoriesCall.enqueue(new Callback<BasicRetroCallModel>() {
            @Override
            public void onResponse(Call<BasicRetroCallModel> call, Response<BasicRetroCallModel> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 6000) {
                        if (permissionCode == 1) {
                            appPreferences.saveBoolean(AppConfiguration.IS_PUSH_NOTIFICATION_SET, true);
                        } else {
                            appPreferences.saveBoolean(AppConfiguration.IS_PUSH_NOTIFICATION_SET, false);
                            quickChallengeTableQueries.deleteAllQuickChallenge();
                        }
                    } else if (response.body().getStatus() == 6004) {
                        AlertDialogCustom.sessionExpiredDialog(SettingsActivity.this);
                    } else {
                        AlertDialogCustom.commonAlertDialog(SettingsActivity.this, response.body().getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<BasicRetroCallModel> call, Throwable t) {
                progressDialog.dismiss();
                AlertDialogCustom.commonAlertDialog(SettingsActivity.this, getString(R.string.went_wrong_wait));
            }

        });
    }

    public void onDataFetchSuccess(UserDetailsModel.DataBean data) {
        UserDataTableQueries userDataTableQueries = UserDataTableQueries.sharedInstance(getApplicationContext());
        userDataTableQueries.deleteAllUserDetails();

        userDataTableQueries.insertDetails(data.getDepartment_id(), data.getDepartment(), data.getDevice_id(),
                data.getEmail(), data.getFirstname(), data.getLanguage_id(), data.getLastname(), data.getPhone(),
                data.getUser_mark(), data.getId(), data.getImage_path());

        categoriesTableQueries = categoriesTableQueries.sharedInstance(SettingsActivity.this);
        topPlayersTableQueries = topPlayersTableQueries.sharedInstance(SettingsActivity.this);

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

            categoriesTableQueries.insertAllTopics(categoriesSqliteModel);

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

    }

}