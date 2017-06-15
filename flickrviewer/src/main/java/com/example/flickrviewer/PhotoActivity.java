package com.example.flickrviewer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.flickrviewer.utils.FlickrUtil;

/**
 * Created by kbajn on 6/15/2017.
 */

public class PhotoActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "Photo";
    public static final String EXTRA_PHOTOS = "photos";
    public static final String EXTRA_PHOTO_IDX = "photoIdx";

    private ViewPager mPager;
    private FlickrPhotoPagerAdapter mAdapter;
    private View mContentView;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        String theme = sharedPreferences.getString(getString(R.string.pref_color_key), getString(R.string.pref_color_default));

        //checks for preference and changes theme
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
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }else{
            Log.d("photoActivity", "actionBar is null for some reason");
        }


        mAdapter = new FlickrPhotoPagerAdapter(getSupportFragmentManager());
        if(mAdapter == null){
            Log.d("photoActivity", "why are you null");
        }
        mPager = (ViewPager)findViewById(R.id.pager);
        if(mPager == null){
            Log.d("photoActivity", "hey.... why is mpage null");
        }
        mPager.setAdapter(mAdapter);
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(EXTRA_PHOTOS)){
            FlickrUtil.FlickrPhoto[] photos = (FlickrUtil.FlickrPhoto[])intent.getSerializableExtra(EXTRA_PHOTOS);
            mAdapter.updatePics(photos);
            mPager.setCurrentItem(intent.getIntExtra(EXTRA_PHOTO_IDX, 0));
        }

        mContentView = findViewById(R.id.fullscreen_content);



        if (theme.equals("light")){
            myToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else if (theme.equals("midnight")){
            myToolbar.setBackgroundColor(getResources().getColor(R.color.darkPrimary));
        }
        else {
            myToolbar.setBackgroundColor(getResources().getColor(R.color.fiestaPrimary));
        }
    }



    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_photoview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.action_favorite:
                Intent favIntent = new Intent(this, FavActivity.class);
                startActivity(favIntent);
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
        Log.d(TAG, "Photo got pref changed");
        this.recreate();
    }
}
