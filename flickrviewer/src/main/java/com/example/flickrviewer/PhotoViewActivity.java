package com.example.flickrviewer;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

/**
 * Created by amber on 6/7/2017.
 */

public class PhotoViewActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    // Hess doesn't have this one full functional so we'll need to implement this more
    // since it's kind of the bulk of our proposed app
    // should display the image along with the comments and allow for favoriting of the pic
    // Almost none of this code does anything - currently clicking a picture just starts a new intent
    // that has a nice purple screen

    private static final boolean AUTO_HIDE = true;

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    private static final int UI_ANIMATION_DELAY = 300;

    private final Handler mHideHand = new Handler();
    private View mContent;
    private ViewPager mPage;
    private static final String TAG = "PhotoView";


    private final Runnable mHidePart2Runnable = new Runnable(){
      @SuppressLint("InlinedAPI")
        @Override
        public void run(){
          mContent.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE |
          View.SYSTEM_UI_FLAG_FULLSCREEN
          | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
          | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
          | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
      }
    };

    private View mControlsV;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null){
                actionBar.show();
            }
           // mControlsV.setVisibility(View.VISIBLE);
        }

    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable(){
        @Override
        public void run(){
            hide();
        }
    };

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent){
            if(AUTO_HIDE){
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

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

        setContentView(R.layout.activity_photo_view);

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

       /* ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }*/

        mPage = (ViewPager)findViewById(R.id.pager);

        mVisible = true;
       // mControlsV = findViewById(R.id.fullscreen_content_controls); no idea what this is or where it goes
        mContent = findViewById(R.id.fullscreen_content);

        mContent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                toggle();
            }
        });

        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener); should prevent jarring UI changes but no button exists so shrug
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == android.R.id.home){ // home is the home button
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle(){
        if(mVisible){
            hide();
        }
        else{
            show();
        }
    }

    private void hide(){
        ActionBar action = getSupportActionBar();
        if(action != null){
            action.hide();
        }
        //mControlsV.setVisibility(View.GONE); place holder for if that view existed but it doesn't
        mVisible = false;

        mHideHand.removeCallbacks(mShowPart2Runnable);
        mHideHand.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);

    }

    @SuppressLint("InlinkedApi")
    private void show(){
        mContent.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        mHideHand.removeCallbacks(mHidePart2Runnable);
        mHideHand.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis){
        mHideHand.removeCallbacks(mHideRunnable);
        mHideHand.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
        Log.d(TAG, "Fav got pref changed");
        this.recreate();
    }
}

