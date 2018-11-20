package com.kawika.smart_survey.services;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.kawika.smart_survey.R;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.views.MyScoreActivity;


public class BackgroundMusicService extends Service {

    MediaPlayer player;
    public static boolean musicIsRunning = false;

    public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            player = MediaPlayer.create(this, R.raw.martin_garrix_animals);
            player.setLooping(true); // Set looping
            player.setVolume(100, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        musicIsRunning = true;
        return START_STICKY;
    }

    public void onDestroy() {
        //null checking
        if (player != null) {
            player.stop();
            player.release();
            musicIsRunning = false;
        }

    }

    public static void StartService(Context context) {
        //shared preference
        AppPreferences appPreferences = AppPreferences.getInstance(context, AppConfiguration.SMART_SURVEY_PREFS);
        //checking button enable
        if (appPreferences.getBoolean(AppConfiguration.IS_GAME_MUSIC_SET) && !musicIsRunning) {
            context.startService(new Intent(context, BackgroundMusicService.class));
        }
    }

    public static void StopService(Context context) {
        context.stopService(new Intent(context, BackgroundMusicService.class));
        musicIsRunning = false;

    }
}