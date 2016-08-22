package com.vladnamik.developer.movieinformationviewer.main;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.vladnamik.developer.movieinformationviewer.components.Application;
import com.vladnamik.developer.movieinformationviewer.components.services.APIService;
import com.vladnamik.developer.movieinformationviewer.database.DBHelper;
import com.vladnamik.developer.movieinformationviewer.database.entities.Movie;
import com.vladnamik.developer.movieinformationviewer.database.entities.SearchPage;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DataLoaderUsedService {
    private static final String DATA_LOADER_LOG_TAG = "DataLoader";

    private Application application;

    public DataLoaderUsedService(Application application) {
        this.application = application;
    }

    public SearchPage loadPage(Context context, final String search, final int pageNumber) throws IOException {
        Log.d(DATA_LOADER_LOG_TAG, "start loading page from DB");
        DBHelper dbHelper = new DBHelper();
        SearchPage searchPage = dbHelper.getSearchPage(search, pageNumber);
        if (searchPage != null) {
            searchPage.setMovies(dbHelper.getMoviesFromPageAndUpdate(searchPage));
            Log.d(DATA_LOADER_LOG_TAG, "page uploaded successfully");
            return searchPage;
        } else {
            Log.d(DATA_LOADER_LOG_TAG, "Found nothing in DB; start loading page from API");

            final ReentrantLock broadcastLock = new ReentrantLock();
            final Condition broadcastEndCondition = broadcastLock.newCondition();
            final StringBuilder status = new StringBuilder();
            BroadcastReceiver loadPageMessageReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.d(DATA_LOADER_LOG_TAG, "start search page onReceive()");
                    String extraQuery = intent.getStringExtra(APIService.EXTRA_QUERY);
                    int extraPageNumber = intent.getIntExtra(APIService.EXTRA_PAGE_NUMBER, 0);
                    Log.d(DATA_LOADER_LOG_TAG, "extraQuery = " + extraQuery + "; extraPageNumber = "
                            + extraPageNumber + "\nquery = " + search + "; pageNumber = " + pageNumber);

                    if (extraQuery.equals(search) && extraPageNumber == pageNumber) {
                        status.append(intent.getStringExtra(APIService.EXTENDED_DATA_STATUS));
                        Log.d(DATA_LOADER_LOG_TAG, "end search page onReceive()");
                        broadcastLock.lock();
                        broadcastEndCondition.signal();
                        broadcastLock.unlock();
                    }
                }
            };

            try {
                broadcastLock.lock();
                APIService.startActionLoadPage(context, search, pageNumber);
                LocalBroadcastManager.getInstance(context).registerReceiver(
                        loadPageMessageReceiver, new IntentFilter(APIService.ACTION_BROADCAST_LOAD_PAGE));
                Log.d(DATA_LOADER_LOG_TAG, "start waiting for broadcast end (search page)");
                broadcastEndCondition.await(10, TimeUnit.SECONDS);
                Log.d(DATA_LOADER_LOG_TAG, "end waiting for broadcast end (search page)");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(loadPageMessageReceiver);
                broadcastLock.unlock();
            }
            Log.d(DATA_LOADER_LOG_TAG, "end getting page from API\n status = " + status.toString());
            if (status.toString().equals(APIService.STATUS_OK)) {
                searchPage = dbHelper.getSearchPage(search, pageNumber);
                searchPage.setMovies(dbHelper.getMoviesFromPageAndUpdate(searchPage));
                return searchPage;
            } else {
                throw new IOException(status.toString());
            }
        }
    }

    public Movie loadFullMovieByImdbId(Context context, final String imdbId) throws IOException {
        Log.d(DATA_LOADER_LOG_TAG, "start loading movie by imdb id from DB");
        DBHelper dbHelper = new DBHelper();
        Movie movie = dbHelper.getMovieByImdbId(imdbId);
        if (movie != null && movie.isFull()) {
            Log.d(DATA_LOADER_LOG_TAG, "movie uploaded successfully");
            return movie;
        } else {
            Log.d(DATA_LOADER_LOG_TAG, "Found nothing in DB; start loading page from API");
            final ReentrantLock broadcastLock = new ReentrantLock();
            final Condition broadcastEndCondition = broadcastLock.newCondition();
            final StringBuilder status = new StringBuilder();
            BroadcastReceiver loadFullMovieInfoMessageReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.d(DATA_LOADER_LOG_TAG, "start movie full info onReceive()");
                    String extraMovieImdbId = intent.getStringExtra(APIService.EXTRA_MOVIE_IMDB_ID);
                    Log.d(DATA_LOADER_LOG_TAG, "extraMovieImdbId = " + extraMovieImdbId + "; MovieImdbId = " + imdbId);

                    if (extraMovieImdbId.equals(imdbId)) {
                        status.append(intent.getStringExtra(APIService.EXTENDED_DATA_STATUS));
                        Log.d(DATA_LOADER_LOG_TAG, "end movie full info onReceive()");
                        broadcastLock.lock();
                        broadcastEndCondition.signal();
                        broadcastLock.unlock();
                    }
                }
            };

            try {
                broadcastLock.lock();
                APIService.startActionFullMovieInfo(context, imdbId);
                LocalBroadcastManager.getInstance(context).registerReceiver(
                        loadFullMovieInfoMessageReceiver, new IntentFilter(APIService.ACTION_BROADCAST_FULL_MOVIE_INFO));
                Log.d(DATA_LOADER_LOG_TAG, "start waiting for broadcast end (movie full info)");
                broadcastEndCondition.await(10, TimeUnit.SECONDS);
                Log.d(DATA_LOADER_LOG_TAG, "end waiting for broadcast end (movie full info)");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(loadFullMovieInfoMessageReceiver);
                broadcastLock.unlock();
            }
            Log.d(DATA_LOADER_LOG_TAG, "end getting movie from API\n status = " + status.toString());
            if (status.toString().equals(APIService.STATUS_OK)) {
                movie = dbHelper.getMovieByImdbId(imdbId);
                return movie;
            } else {
                throw new IOException(status.toString());
            }
        }
    }

//    public Movie loadFullMovieByTitle(Context context, String title) throws IOException {
//        Log.d(DATA_LOADER_LOG_TAG, "start loading movie by imdb id from DB");
//        DBHelper dbHelper = new DBHelper();
//        Movie movie = dbHelper.getMovieByTitle(title);
//        if (movie != null && movie.isFull()) {
//            Log.d(DATA_LOADER_LOG_TAG, "movie uploaded successfully");
//            return movie;
//        } else {
//            Log.d(DATA_LOADER_LOG_TAG, "Found nothing in DB; start loading page from API");
//            movie = application.getApiHelper().getMovieByTitle(title);
//            Log.d(DATA_LOADER_LOG_TAG, "end getting movie from API");
//            return movie;
//        }
//    }
}
