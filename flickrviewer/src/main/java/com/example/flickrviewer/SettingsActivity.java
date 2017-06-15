package com.example.flickrviewer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by kbajn on 6/12/2017.
 */

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "Settings";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        String theme = sharedPreferences.getString(getString(R.string.pref_color_key), getString(R.string.pref_color_default));

        if (theme.equals("light")){
            setTheme(R.style.AppTheme);
        }
        else if (theme.equals("midnight")){
            setTheme(R.style.AppThemeDark);
        }
        else
            setTheme(R.style.AppThemeFiesta);
        //setTheme(R.style.AppTheme);

        setContentView(R.layout.activity_settings);

       Toolbar myToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (theme.equals("light")){
            myToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else if (theme.equals("midnight")){
            myToolbar.setBackgroundColor(getResources().getColor(R.color.darkPrimary));
        }
        else
            myToolbar.setBackgroundColor(getResources().getColor(R.color.fiestaPrimary));

        //Get shared preferences
        /*SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        String theme = sharedPreferences.getString(getString(R.string.pref_color_key), getString(R.string.pref_color_default));
        Log.d(TAG, theme);*/

        getFragmentManager().beginTransaction().replace(R.id.blankFragment, new SettingsFragment()).commit();

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
        Log.d(TAG, "Settings got pref changed");
        this.recreate();
    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);

        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
            //reset theme
            Log.d(TAG, "It knows it changed... we need to do something.");

            /*String theme = sharedPreferences.getString(getString(R.string.pref_color_key), getString(R.string.pref_color_default));
            Log.d(TAG, theme); */

        }

        @Override
        public void onResume(){
            super.onResume();
            Log.d(TAG, "Settings resumed");
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause(){
            super.onPause();
            Log.d(TAG, "Settings paused");

            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }
    }

}
