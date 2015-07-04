package com.mhgroup.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mhgroup.activity.R;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by DK Wang on 2015/4/1.
 */
public class MyApplication extends Application {

    // we need to get the update-to-date values.

    //public static String ORI_LANG;
    //public static String GOAL_LANG;

    public static String PRE_ORI;
    public static String PRE_GOAL;
    public static String DEFAULT_ORI;
    public static String DEFAULT_GOAL;

    private static Context CONTEXT;

    public static Map<String, Locale> LANG_LOCALE_MAP = new HashMap<String, Locale>();

    @Override
    public void onCreate() {
        super.onCreate();

        // init the map
        String[] keys = getResources().getStringArray(R.array.language_values);
        Locale[] locales = new Locale[]{Locale.ENGLISH, Locale.CHINA, new Locale("es", "ES"), Locale.FRANCE, Locale.GERMAN};
        for(int i=0; i<keys.length; i++)
        {
            LANG_LOCALE_MAP.put(keys[i], locales[i]);
        }

        // init the strings
        PRE_ORI = getResources().getString(R.string.setting_origin_key);
        PRE_GOAL = getResources().getString(R.string.setting_goal_key);

        DEFAULT_ORI = getResources().getString(R.string.setting_origin_default);
        DEFAULT_GOAL = getResources().getString(R.string.setting_goal_default);
        CONTEXT = getApplicationContext();
        // read the settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CONTEXT);
        if (sharedPreferences.contains(PRE_ORI) && sharedPreferences.contains(PRE_GOAL)) {
            //ORI_LANG = sharedPreferences.getString(PRE_ORI, DEFAULT_ORI);
            //GOAL_LANG = sharedPreferences.getString(PRE_GOAL, DEFAULT_GOAL);
            Log.d("wdk", sharedPreferences.getString(PRE_ORI, ""));

        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PRE_ORI, DEFAULT_ORI);
            editor.putString(PRE_GOAL, DEFAULT_GOAL);
            editor.commit();

            Log.d("wdk", sharedPreferences.getString(PRE_ORI, ""));
            //ORI_LANG = DEFAULT_ORI;
            //GOAL_LANG = DEFAULT_GOAL;
        }
    }

    public String getGoalLanguage()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CONTEXT);
        return sharedPreferences.getString(PRE_GOAL, "zh-CN");
    }


}
