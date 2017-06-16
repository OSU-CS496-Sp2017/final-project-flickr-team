package com.example.flickrviewer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kbajn on 6/16/2017.
 */

public class FavDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favoritePhotos.db";
    private static final int DATABASE_VERSION = 1;

    public FavDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        final String SQL_CREATE_FAVORITE_PHOTOS_TABLE =
                "CREATE TABLE " + FavContract.FavoritePhotos.TABLE_NAME + " (" +
                        FavContract.FavoritePhotos._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavContract.FavoritePhotos.COLUMN_URL + " TEXT NOT NULL " +
                        ");";
        db.execSQL(SQL_CREATE_FAVORITE_PHOTOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + FavContract.FavoritePhotos.TABLE_NAME);
        onCreate(db);
    }

}
