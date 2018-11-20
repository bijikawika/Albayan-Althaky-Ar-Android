package com.kawika.smart_survey.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kawika.smart_survey.R;
import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.preferences.AppPreferences;
import com.kawika.smart_survey.services.BackgroundMusicService;
import com.kawika.smart_survey.utils.LocaleManager;

/**
 * Created by senthiljs on 15/03/18.
 */

public class GameMusicPermissionActivity extends AppCompatActivity {

    private AppPreferences appPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_music_permission_dialog);

        appPreferences = AppPreferences.getInstance(GameMusicPermissionActivity.this, AppConfiguration.SMART_SURVEY_PREFS);

        Button enableButton = findViewById(R.id.enableButton);
        enableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appPreferences.saveBoolean(AppConfiguration.IS_GAME_MUSIC_SET, true);
                BackgroundMusicService.StartService(GameMusicPermissionActivity.this);
                startActivity(new Intent(GameMusicPermissionActivity.this, HomeActivity.class));
                finish();

            }
        });

        Button disableButton = findViewById(R.id.disableButton);
        disableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appPreferences.saveBoolean(AppConfiguration.IS_GAME_MUSIC_SET, false);
                BackgroundMusicService.StartService(GameMusicPermissionActivity.this);
                startActivity(new Intent(GameMusicPermissionActivity.this, HomeActivity.class));
                finish();

            }
        });

        TextView skipTextView = findViewById(R.id.skipTextView);
        skipTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appPreferences.saveBoolean(AppConfiguration.IS_GAME_MUSIC_SET, false);
                startActivity(new Intent(GameMusicPermissionActivity.this, HomeActivity.class));
                finish();
            }
        });

        appPreferences.saveBoolean(AppConfiguration.IS_PUSH_NOTIFICATION_SET, true);
        appPreferences.saveBoolean(AppConfiguration.IS_SOUND_EFFECTS_SET, true);
    }
    //language wrapper
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }
}
