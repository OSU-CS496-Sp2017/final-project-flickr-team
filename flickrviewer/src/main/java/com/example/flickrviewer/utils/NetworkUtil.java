package com.example.flickrviewer.utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by amber on 6/7/2017.
 */
/*
 Literally just a URL caller
 */

public class NetworkUtil {

    private static final OkHttpClient mHTTP = new OkHttpClient();

    public static String doGet(String URL) throws IOException {
        Request r = new Request.Builder()
                .url(URL)
                .build();
        Response response = mHTTP.newCall(r).execute();

        try {
            return response.body().string();
        } finally {
            response.close();
        }
    }
}
