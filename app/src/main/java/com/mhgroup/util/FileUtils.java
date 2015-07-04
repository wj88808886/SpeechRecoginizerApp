package com.mhgroup.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mhgroup.activity.R;

import java.util.Locale;

/**
 * Created by DK Wang on 2015/4/2.
 */
public class FileUtils {

    public static String getGoalLanguage(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getResources().getString(R.string.setting_goal_key);
        String defaultLang = context.getResources().getString(R.string.setting_goal_default);
        return sharedPreferences.getString(key, defaultLang);
    }

    public static String getOriginalLanguage(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getResources().getString(R.string.setting_origin_key);
        String defaultLang = context.getResources().getString(R.string.setting_origin_default);
        return sharedPreferences.getString(key, defaultLang);
    }

}
