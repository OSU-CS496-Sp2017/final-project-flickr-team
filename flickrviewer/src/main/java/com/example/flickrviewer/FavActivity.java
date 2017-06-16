package com.example.flickrviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.example.flickrviewer.data.FavContract;
import com.example.flickrviewer.data.FavDBHelper;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kbajn on 6/13/2017.
 */

public class FavActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "Fav";
    private SQLiteDatabase mDB;
    private ArrayList<String> urls;

    private RecyclerView mPhotoRV;

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


        setContentView(R.layout.activity_fav);

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


        //DB STUFF
        FavDBHelper dbHelper = new FavDBHelper(FavActivity.this);
        mDB = dbHelper.getReadableDatabase();


        ///get the url, add it to array to be displayed
        urls = new ArrayList<String>();
        Cursor cursor = mDB.query(FavContract.FavoritePhotos.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            Log.d(TAG, "IN DB: " + cursor.getString(cursor.getColumnIndex(FavContract.FavoritePhotos.COLUMN_URL)));
            urls.add(cursor.getString(cursor.getColumnIndex(FavContract.FavoritePhotos.COLUMN_URL)));
        }

        //Display last favorited image
        ImageView image = (ImageView)findViewById(R.id.iv_fav);
        Context context = FavActivity.this;
        Picasso.with(context).setLoggingEnabled(true);
        Picasso.with(context).load(urls.get(urls.size() - 1)).into(image);


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
