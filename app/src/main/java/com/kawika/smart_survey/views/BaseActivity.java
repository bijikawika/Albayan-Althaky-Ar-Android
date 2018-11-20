package com.kawika.smart_survey.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.kawika.smart_survey.R;
import com.kawika.smart_survey.utils.PlaySound;
import com.kawika.smart_survey.databinding.HomeActivityBinding;
import com.kawika.smart_survey.databinding.MyScoreActivityBinding;
import com.kawika.smart_survey.databinding.NotificationActivityBinding;
import com.kawika.smart_survey.helper.BottomNavigationViewHelper;
import com.kawika.smart_survey.utils.LocaleManager;

public abstract class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView navigationView;
    HomeActivityBinding homeActivityBinding;
    MyScoreActivityBinding myScoreActivityBinding;
    NotificationActivityBinding notificationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        homeActivityBinding = getHomeBinding();
        myScoreActivityBinding = getMyScoreActivityBinding();
        notificationBinding = getNotificationBinding();

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(navigationView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_score) {
                PlaySound.buttonClickSound(this, R.raw.button_click_sound);
                startActivity(new Intent(this, MyScoreActivity.class));
            } else if (itemId == R.id.menu_home) {
                PlaySound.buttonClickSound(this, R.raw.button_click_sound);
                startActivity(new Intent(this, HomeActivity.class));
            } else if (itemId == R.id.menu_settings) {
                PlaySound.buttonClickSound(this, R.raw.button_click_sound);
                startActivity(new Intent(this, SettingsActivity.class));
            } else if (itemId == R.id.menu_topics) {
                PlaySound.buttonClickSound(this, R.raw.button_click_sound);
                startActivity(new Intent(this, TopicsActivity.class));
            } else if (itemId == R.id.menu_notifications) {
                PlaySound.buttonClickSound(this, R.raw.button_click_sound);
                startActivity(new Intent(this, NotificationsActivity.class));
            }
            finish();
//        }, 0);
        return true;
    }

    private void updateNavigationBarState() {
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        Menu menu = navigationView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);
                break;
            }
        }
    }

    abstract int getContentViewId();

    abstract int getNavigationMenuItemId();

    abstract HomeActivityBinding getHomeBinding();

    abstract MyScoreActivityBinding getMyScoreActivityBinding();

    abstract NotificationActivityBinding getNotificationBinding();


    //language wrapper
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }
}
