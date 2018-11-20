package com.kawika.smart_survey.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatDelegate;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.kawika.smart_survey.api_services.RetrofitService;
import com.kawika.smart_survey.callbacks.LifeCycleDelegate;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.services.BackgroundMusicService;
import com.kawika.smart_survey.utils.LocaleManager;

import io.fabric.sdk.android.Fabric;
import java.util.Locale;

public class SmartSurveyApplication extends Application implements LifeCycleDelegate {
    private RetrofitService mRetrofitService;
    private TelephonyManager telephonyManager;
    public static SmartSurveyApplication smartSurveyApplication;
    private Bitmap bitmap=null;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        AppLifecycleHandler lifeCycleHandler = new AppLifecycleHandler(this);
        registerActivityLifecycleCallbacks(lifeCycleHandler);
        registerComponentCallbacks(lifeCycleHandler);
        smartSurveyApplication = this;
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

    }


    private void registerCallReceiver() {
        if(telephonyManager != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    private void unregisterCallReceiver() {
        if(telephonyManager != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
    }

    PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                BackgroundMusicService.StopService(SmartSurveyApplication.this);
            } else if(state == TelephonyManager.CALL_STATE_IDLE) {
                BackgroundMusicService.StartService(SmartSurveyApplication.this);
            } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
                BackgroundMusicService.StopService(SmartSurveyApplication.this);
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    };

    public static SmartSurveyApplication get(Context context) {
        return (SmartSurveyApplication) context.getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocale(this);
    }

    public RetrofitService getRetrofitService() {
        if (mRetrofitService == null) {
            mRetrofitService = RetrofitService.Factory.create();
        }
        return mRetrofitService;
    }

    public void setRetrofitService(RetrofitService mRetrofitService) {
        this.mRetrofitService = mRetrofitService;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void onAppBackgrounded() {
        BackgroundMusicService.StopService(this);
        unregisterCallReceiver();
    }


    public void onAppForegrounded() {
        BackgroundMusicService.StartService(this);
        registerCallReceiver();
    }

}
