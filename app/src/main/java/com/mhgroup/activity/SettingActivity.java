package com.mhgroup.activity;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by DK Wang on 2015/4/1.
 */
public class SettingActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingFragment())
                .commit();
    }

    public static class SettingFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.fragment_setting);
        }
    }
}
