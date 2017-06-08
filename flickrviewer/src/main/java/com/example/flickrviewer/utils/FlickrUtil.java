package com.example.flickrviewer.utils;

import android.net.Uri;
import android.text.TextUtils;


import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by amber on 6/7/2017.
 */

/*
 Classic API call utility class - builds a URL, and parses results
 */

public class FlickrUtil {
    private static final String FLICKR_API_BASE_URL = "https://api.flickr.com/services/rest"; // sets up the request format
    private static final String FLICKR_API_METHOD_PARAM = "method"; // specifies which method to use
    private static final String FLICKR_API_METHOD_ARG = "flickr.photos.getRecent"; // as defined in our proposal
    private static final String FLICKR_API_KEY_PARAM = "api_key"; // parameter for API key
    private static final String FLICK_API_KEY = "bc79f6caa2ca37317f8f6df134fb713f";  // our API key, SHH
    private static final String FLICKR_API_FORMAT_PARAM = "format"; // specifies response format
    private static final String FLICKR_API_FORMAT = "json"; // our response format - JSON rocks
    private static final String FLICKR_API_NOJSONPLS_PARAM = "nojsoncallback"; // tells the API we don't want to do a json function callback
    private static final String FLICKR_API_NOJSONPLS = "1"; // no json
    private static final String FLICKR_API_EXTRAS_PARAM = "extras";
    private static final String[] FLICKR_API_EXTAS = {"url_l", "url_m", "owner_name"};

    private static final Gson gson = new Gson();

    private static class FlickrSearchResult {
        FlickrPhotos photos;
        String stat;
    }

    public static class FlickrPhotos {
        FlickrPhoto[] photo;
    }

    public static class FlickrPhoto implements Serializable{
        public String title;
        public String ownername;
        public String url_l;
        public String url_m;
        public int width_l;
        public int height_l;
        public int width_m;
        public int height_m;
    }

    public static String buildFlickrSearchURL(){
        return Uri.parse(FLICKR_API_BASE_URL).buildUpon()
                .appendQueryParameter(FLICKR_API_METHOD_PARAM, FLICKR_API_METHOD_ARG)
                .appendQueryParameter(FLICKR_API_KEY_PARAM, FLICK_API_KEY)
                .appendQueryParameter(FLICKR_API_FORMAT_PARAM, FLICKR_API_FORMAT)
                .appendQueryParameter(FLICKR_API_NOJSONPLS_PARAM, FLICKR_API_NOJSONPLS)
                .appendQueryParameter(FLICKR_API_EXTRAS_PARAM, TextUtils.join(",", FLICKR_API_EXTAS))
                .build()
                .toString();
    }

    public static FlickrPhoto[] parseFlickrSearchResultsJSON(String exploreSearchJSON){
        FlickrSearchResult searchResults = gson.fromJson(exploreSearchJSON, FlickrSearchResult.class);
        FlickrPhoto[] photos = null;
        if(searchResults != null && searchResults.photos != null){
            photos = searchResults.photos.photo;
        }

        return photos;
    }
}
