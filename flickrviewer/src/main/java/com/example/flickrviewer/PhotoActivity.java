package com.example.flickrviewer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import com.example.flickrviewer.data.FavContract;
import com.example.flickrviewer.data.FavDBHelper;
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
    private String picURL;
    private SQLiteDatabase mDB;


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
        }

        mAdapter = new FlickrPhotoPagerAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);

        mPager.setAdapter(mAdapter);
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(EXTRA_PHOTOS)){
            FlickrUtil.FlickrPhoto[] photos = (FlickrUtil.FlickrPhoto[])intent.getSerializableExtra(EXTRA_PHOTOS);
            mAdapter.updatePics(photos);
            mPager.setCurrentItem(intent.getIntExtra(EXTRA_PHOTO_IDX, 0));

            //set url to add to database
            picURL = photos[mPager.getCurrentItem()].url_m.toString();
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

        //DB STUFF
        FavDBHelper dbHelper = new FavDBHelper(PhotoActivity.this);
        mDB = dbHelper.getWritableDatabase();
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
            case R.id.action_fav:
                Toast.makeText(PhotoActivity.this, "Added to favorites.", Toast.LENGTH_LONG).show();
                Log.d(TAG, picURL);
                addPhotoToDB();
                return true;

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

    private long addPhotoToDB(){
        if (picURL != null){
            ContentValues values = new ContentValues();
            values.put(FavContract.FavoritePhotos.COLUMN_URL, picURL);
            return mDB.insert(FavContract.FavoritePhotos.TABLE_NAME, null, values);
        }
        else
            return -1;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
        Log.d(TAG, "Photo got pref changed");
        this.recreate();
    }

    @Override
    protected void onDestroy(){
        mDB.close();
        super.onDestroy();
    }
}
