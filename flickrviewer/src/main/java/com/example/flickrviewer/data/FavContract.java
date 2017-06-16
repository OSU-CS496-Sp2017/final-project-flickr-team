package com.example.flickrviewer.data;

import android.provider.BaseColumns;

/**
 * Created by kbajn on 6/16/2017.
 */

public class FavContract {

    private FavContract(){}

    public static class FavoritePhotos implements BaseColumns{
        public static final String TABLE_NAME = "favoritePhotos";
        public static final String COLUMN_URL = "url";
    }
}
