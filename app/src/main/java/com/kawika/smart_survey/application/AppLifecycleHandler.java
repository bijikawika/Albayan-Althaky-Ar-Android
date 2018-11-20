package com.kawika.smart_survey.application;

        import android.app.Activity;
        import android.app.Application.ActivityLifecycleCallbacks;
        import android.content.ComponentCallbacks2;
        import android.content.res.Configuration;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.widget.Toast;

        import com.kawika.smart_survey.callbacks.LifeCycleDelegate;

/*
 * Created by akhil on 09/03/18.
 */
public class AppLifecycleHandler implements ActivityLifecycleCallbacks, ComponentCallbacks2 {

    AppLifecycleHandler(LifeCycleDelegate lifeCycleDelegate) {
        this.lifeCycleDelegate = lifeCycleDelegate;
    }

    private boolean appInForeground;
    private LifeCycleDelegate lifeCycleDelegate;

    public void onActivityPaused(@Nullable Activity p0) {
    }

    public void onActivityResumed(@Nullable Activity p0) {
        if(!this.appInForeground) {
            this.appInForeground = true;
            this.lifeCycleDelegate.onAppForegrounded();
        }

    }

    public void onActivityStarted(@Nullable Activity p0) {
    }

    public void onActivityDestroyed(@Nullable Activity p0) {

    }

    public void onActivitySaveInstanceState(@Nullable Activity p0, @Nullable Bundle p1) {
    }

    public void onActivityStopped(@Nullable Activity p0) {

    }

    public void onActivityCreated(@Nullable Activity p0, @Nullable Bundle p1) {
    }

    public void onLowMemory() {
    }

    public void onConfigurationChanged(@Nullable Configuration p0) {
    }

    public void onTrimMemory(int level) {
        if(level == 20) {
            this.appInForeground = false;
            this.lifeCycleDelegate.onAppBackgrounded();
        }

    }
}