package com.kawika.smart_survey.alerts;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.kawika.smart_survey.R;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.views.HomeActivity;
import com.kawika.smart_survey.views.LoginActivity;
import com.kawika.smart_survey.views.QuizTypeSelectActivity;
import com.kawika.smart_survey.views.TopicsActivity;

import static com.kawika.smart_survey.config.AppConfiguration.PERMISSIONS_REQUEST_READ_PHONE;
import static com.kawika.smart_survey.config.AppConfiguration.READ_PHONE_ALERT;


/*
 * Created by senthil on 31/01/2018.
 */
public class AlertDialogCustom {

    public static void commonDialogWithFinish(final Activity activity, String message) {
        try {
            new android.support.v7.app.AlertDialog.Builder(activity)
                    .setTitle(message)
                    .setCancelable(false)
                    .setPositiveButton(activity.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            activity.finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
    }

    public static void commonAlertDialog(Context context, String message) {

        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
        builder1.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert1 = builder1.create();
        alert1.show();
    }

    public static void completedAllLevelDialog(Context context, String message, int selectedCategoryId) {

        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
        builder1.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        Intent quiz_start_intent = new Intent(context, QuizTypeSelectActivity.class);
                        quiz_start_intent.putExtra("selectedCategoryId",selectedCategoryId);
                        context.startActivity(quiz_start_intent);
                    }
                });
        android.app.AlertDialog alert1 = builder1.create();
        alert1.show();
    }

    public static void sessionExpiredDialog(Context context) {
        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
        builder1.setTitle(context.getResources().getString(R.string.warning));
        builder1.setMessage(context.getResources().getString(R.string.session_expired))
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        AppPreferences appPreferences = AppPreferences.getInstance(context, AppConfiguration.SMART_SURVEY_PREFS);
                        appPreferences.clearData();
                        Intent intent = new Intent(context,
                                LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }
                });
        android.app.AlertDialog alert1 = builder1.create();
        alert1.show();
    }

    public static void permissionDialog(final Context context, String message) {

        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
        builder1.setTitle(context.getResources().getString(R.string.permission));
        builder1.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.settings_caps), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", context.getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            dialog.cancel();
                    }
                });
        android.app.AlertDialog alert1 = builder1.create();
        alert1.show();
    }

    public static void alertDialog(final Context context, String message, final int permission_id) {

        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
        builder1.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.accept_caps), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        if (permission_id == READ_PHONE_ALERT) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_READ_PHONE);
                        }
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert1 = builder1.create();
        alert1.show();
    }

    public static void noInternetAlertDialog(final Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.no_internet_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        Button closeButton = dialog.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

}
