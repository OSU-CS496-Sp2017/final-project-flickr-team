package com.example.flickrviewer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

/**
 * Created by kbajn on 6/15/2017.
 */

public class PhotoActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "Photo";

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


        setContentView(R.layout.activity_photo);

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

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
        Log.d(TAG, "Photo got pref changed");
        this.recreate();
    }
}
