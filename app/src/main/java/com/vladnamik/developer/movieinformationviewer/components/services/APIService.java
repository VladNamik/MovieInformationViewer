package com.vladnamik.developer.movieinformationviewer.components.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.vladnamik.developer.movieinformationviewer.components.Application;

import java.io.IOException;

public class APIService extends IntentService {
    private static final String LOG_TAG = "APIService";
    private static final String ACTION_LOAD_PAGE = "com.vladnamik.developer.movieinformationviewer.components.services.action.LOAD_PAGE";
    private static final String ACTION_FULL_MOVIE_INFO = "com.vladnamik.developer.movieinformationviewer.components.services.action.FULL_MOVIE_INFO";
    public static final String ACTION_BROADCAST_FULL_MOVIE_INFO = "com.vladnamik.developer.movieinformationviewer.components.services.action.BROADCAST_FULL_MOVIE_INFO";
    public static final String ACTION_BROADCAST_LOAD_PAGE = "com.vladnamik.developer.movieinformationviewer.components.services.action.BROADCAST_LOAD_PAGE";
    public static final String EXTENDED_DATA_STATUS = "com.vladnamik.developer.movieinformationviewer.components.services.STATUS";
    public static final String STATUS_OK = "OK";

    public static final String EXTRA_QUERY = "com.vladnamik.developer.movieinformationviewer.components.services.extra.QUERY";
    public static final String EXTRA_PAGE_NUMBER = "com.vladnamik.developer.movieinformationviewer.components.services.extra.PAGE_NUMBER";

    public static final String EXTRA_MOVIE_IMDB_ID = "com.vladnamik.developer.movieinformationviewer.components.services.extra.MOVIE_IMDB_ID";

    public APIService() {
        super("APIService");
    }

    public static void startActionFullMovieInfo(Context context, String imdbId) {
        Intent intent = new Intent(context, APIService.class);
        intent.setAction(ACTION_FULL_MOVIE_INFO);
        intent.putExtra(EXTRA_MOVIE_IMDB_ID, imdbId);
        context.startService(intent);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(LOG_TAG, "onStart");
    }

    public static void startActionLoadPage(Context context, String query, int pageNumber) {
        Intent intent = new Intent(context, APIService.class);
        intent.setAction(ACTION_LOAD_PAGE);
        intent.putExtra(EXTRA_QUERY, query);
        intent.putExtra(EXTRA_PAGE_NUMBER, pageNumber);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();
            Log.d(LOG_TAG, "action = " + action);
            if (ACTION_LOAD_PAGE.equals(action)) {
                final String query = intent.getStringExtra(EXTRA_QUERY);
                final int pageNumber = intent.getIntExtra(EXTRA_PAGE_NUMBER, 0);
                handleActionLoadPage(query, pageNumber);
            } else if (ACTION_FULL_MOVIE_INFO.equals(action)) {
                final String imdbId = intent.getStringExtra(EXTRA_MOVIE_IMDB_ID);
                handleActionFullMovieInfo(imdbId);
            }
        }
    }

    private void handleActionLoadPage(String query, int pageNumber) {
        Log.d(LOG_TAG, "start handleActionLoadPage()");
        try {
            ((Application) getApplication()).getApiHelper().getPage(query, pageNumber);
            Intent localIntent = new Intent(ACTION_BROADCAST_LOAD_PAGE)
                    .putExtra(EXTENDED_DATA_STATUS, STATUS_OK)
                    .putExtra(EXTRA_QUERY, query)
                    .putExtra(EXTRA_PAGE_NUMBER, pageNumber);
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        } catch (IOException e) {
            Intent localIntent = new Intent(ACTION_BROADCAST_LOAD_PAGE)
                    .putExtra(EXTENDED_DATA_STATUS, e.getMessage())
                    .putExtra(EXTRA_QUERY, query)
                    .putExtra(EXTRA_PAGE_NUMBER, pageNumber);
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        } finally {
            Log.d(LOG_TAG, "end handleActionLoadPage()");
        }
    }

    private void handleActionFullMovieInfo(String imdbId) {
        try {
            ((Application) getApplication()).getApiHelper().getMovieByImdbId(imdbId);
            Intent localIntent = new Intent(ACTION_BROADCAST_FULL_MOVIE_INFO)
                    .putExtra(EXTENDED_DATA_STATUS, STATUS_OK)
                    .putExtra(EXTRA_MOVIE_IMDB_ID, imdbId);
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        } catch (IOException e) {
            Intent localIntent = new Intent(ACTION_BROADCAST_FULL_MOVIE_INFO)
                    .putExtra(EXTENDED_DATA_STATUS, e.getMessage())
                    .putExtra(EXTRA_MOVIE_IMDB_ID, imdbId);
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
    }
}
