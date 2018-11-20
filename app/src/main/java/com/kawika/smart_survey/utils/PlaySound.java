package com.kawika.smart_survey.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.kawika.smart_survey.R;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.preferences.AppPreferences;

/*
 * Created by senthiljs on 08/03/18.
 */

public class PlaySound {

   static MediaPlayer mp = null;
    public static void buttonClickSound(Context context, int button_click_sound) {



        AppPreferences appPreferences = AppPreferences.getInstance(context, AppConfiguration.SMART_SURVEY_PREFS);
        if (appPreferences.getBoolean(AppConfiguration.IS_SOUND_EFFECTS_SET)) {
            try {
                if (mp != null) {
                    if (mp.isPlaying()) {
                        mp.reset();
                    }
                }
                mp = new MediaPlayer();
                mp = MediaPlayer.create(context, button_click_sound);
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.reset();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("e = afewfwfw" + e);
            }

        }
    }

}
