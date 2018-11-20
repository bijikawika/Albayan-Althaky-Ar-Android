package com.kawika.smart_survey.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import static com.kawika.smart_survey.config.AppConfiguration.CAMERA_AND_WRITE_REQUEST_CODE;

/*
 * Created by pc on 5/3/2017.
 */
public class ApplicationPermission {
    private static final String TAG = "ApplicationPermission";

    /**
     * permiss
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermissions(Context context, String... permissions) throws Exception {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * storage permission definition
     *
     * @param activity
     */
    public static void setStoragePermission(Activity activity) {
        try {
            // The request code used in ActivityCompat.requestPermissions()
            // and returned in the Activity'provider_path onRequestPermissionsResult()
            String[] PERMISSIONS = { Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};

            if (!hasPermissions(activity, PERMISSIONS)) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS, CAMERA_AND_WRITE_REQUEST_CODE);
            }
        } catch (Exception e) {
            Log.e(TAG, "PermissionM: ", e);
        }
    }


}
