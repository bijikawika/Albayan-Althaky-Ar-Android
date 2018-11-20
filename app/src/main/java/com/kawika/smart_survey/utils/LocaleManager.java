package com.kawika.smart_survey.utils;
/*
 * Created by akhil on 15/02/18.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import com.kawika.smart_survey.config.AppConfiguration;
import com.kawika.smart_survey.preferences.AppPreferences;

import java.util.Locale;

public class LocaleManager {

    public static Context setLocale(Context c) {
        return updateResources(c);
    }

    private static Context updateResources(Context context) {
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

}