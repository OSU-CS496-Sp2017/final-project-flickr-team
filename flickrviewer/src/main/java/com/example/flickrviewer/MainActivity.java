package com.example.flickrviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.flickrviewer.utils.FlickrUtil;
import com.example.flickrviewer.utils.NetworkUtil;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
implements LoaderManager.LoaderCallbacks<String>, FlickrPhotoGridAdapater.OnPhotoItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FLICKR_SEARCH_LOADER_ID = 0;
    private static final int NUM_COLUMNS = 1;

    private RecyclerView mPhotoRV;
    private ProgressBar mLoadingBar;
    private TextView mLoadError;

    private FlickrPhotoGridAdapater mGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //myToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        if (theme.equals("light")){
            myToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else if (theme.equals("midnight")){
            myToolbar.setBackgroundColor(getResources().getColor(R.color.darkPrimary));
        }
        else
            myToolbar.setBackgroundColor(getResources().getColor(R.color.fiestaPrimary));

        //Get shared preferences
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        mLoadingBar = (ProgressBar)findViewById(R.id.pb_load_bar);
        mLoadError = (TextView)findViewById(R.id.tv_load_error);
        mPhotoRV = (RecyclerView)findViewById(R.id.rv_pics);

        mGrid = new FlickrPhotoGridAdapater(this);
        mPhotoRV.setLayoutManager(new StaggeredGridLayoutManager(NUM_COLUMNS, StaggeredGridLayoutManager.VERTICAL)); // change num_columns or vertical/horizontal for different aesthetic
        mPhotoRV.setHasFixedSize(true);
        mPhotoRV.setAdapter(mGrid);

        getSupportLoaderManager().initLoader(FLICKR_SEARCH_LOADER_ID, null, this);

        Log.d(TAG, "in main, finished onCreate");

    }

    /*
    Creates the loader that will load our image results and put them in the grid viewer
     */

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args){
        return new AsyncTaskLoader<String>(this) {

            String mJSONResults;

            @Override
            protected void onStartLoading(){
                if(mJSONResults != null){
                    deliverResult(mJSONResults);
                } else{
                  mLoadingBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                String flickrURL = FlickrUtil.buildFlickrSearchURL();
                String results = null;
                try{
                    results = NetworkUtil.doGet(flickrURL);
                } catch (IOException e){
                    Log.d(TAG, "Error connecting: ", e);
                }
                Log.d(TAG, "here are results: " + results);
                return results;
            }

            @Override
            public void deliverResult(String data){
                mJSONResults = data;
                super.deliverResult(data);
            }
        };
    }

    /*
    Places photos in RecyclerView unless the response is empty in which case we print an error
     */

    @Override
    public void onLoadFinished(Loader<String> loader, String data){
        mLoadingBar.setVisibility(View.INVISIBLE);
        if(data != null){
            mLoadError.setVisibility(View.INVISIBLE);
            mPhotoRV.setVisibility(View.VISIBLE);
            FlickrUtil.FlickrPhoto[] pics = FlickrUtil.parseFlickrSearchResultsJSON(data);
            mGrid.updatePhotos(pics);
        } else{
            mPhotoRV.setVisibility(View.INVISIBLE);
            mLoadError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader){
        // do nothing
    }

    /*
    If a photo is clicked, start the photoviewactivity
     */

    @Override
    public void onPhotoItemClick(int photoID){
        Intent intent = new Intent(this, PhotoViewActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
        Log.d(TAG, "Main got pref changed");
        this.recreate();
    }

}
