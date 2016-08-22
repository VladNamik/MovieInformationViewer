package com.vladnamik.developer.movieinformationviewer.main;


import android.util.Log;

import com.vladnamik.developer.movieinformationviewer.components.Application;
import com.vladnamik.developer.movieinformationviewer.database.DBService;
import com.vladnamik.developer.movieinformationviewer.database.entities.Movie;
import com.vladnamik.developer.movieinformationviewer.database.entities.SearchPage;

import java.io.IOException;

public class DataLoader {
    private static final String DATA_LOADER_LOG_TAG = "DataLoader";

    private Application application;

    public DataLoader(Application application) {
        this.application = application;
    }

    public SearchPage loadPage(final String search, final int pageNumber) throws IOException {
        Log.d(DATA_LOADER_LOG_TAG, "start loading page from DB");
        DBService dbService = new DBService();
        SearchPage searchPage = dbService.getSearchPage(search, pageNumber);
        if (searchPage != null) {
            searchPage.setMovies(dbService.getMoviesFromPageAndUpdate(searchPage));
            Log.d(DATA_LOADER_LOG_TAG, "page uploaded successfully");
            return searchPage;
        } else {
            Log.d(DATA_LOADER_LOG_TAG, "Found nothing in DB; start loading page from API");
            searchPage = application.getApiHelper().getPage(search, pageNumber);
            Log.d(DATA_LOADER_LOG_TAG, "end getting page from API");
            return searchPage;
        }
    }

    public Movie loadMovieByImdbId(String imdbId) throws IOException {
        Log.d(DATA_LOADER_LOG_TAG, "start loading movie by imdb id from DB");
        DBService dbService = new DBService();
        Movie movie = dbService.getMovieByImdbId(imdbId);
        if (movie != null) {
            Log.d(DATA_LOADER_LOG_TAG, "movie uploaded successfully");
            return movie;
        } else {
            Log.d(DATA_LOADER_LOG_TAG, "Found nothing in DB; start loading page from API");
            movie = application.getApiHelper().getMovieByImdbId(imdbId);

            Log.d(DATA_LOADER_LOG_TAG, "end getting movie from API");
            return movie;
        }
    }

    public Movie loadMovieByTitle(String title) throws IOException {
        Log.d(DATA_LOADER_LOG_TAG, "start loading movie by imdb id from DB");
        DBService dbService = new DBService();
        Movie movie = dbService.getMovieByTitle(title);
        if (movie != null) {
            Log.d(DATA_LOADER_LOG_TAG, "movie uploaded successfully");
            return movie;
        } else {
            Log.d(DATA_LOADER_LOG_TAG, "Found nothing in DB; start loading page from API");
            movie = application.getApiHelper().getMovieByTitle(title);
            Log.d(DATA_LOADER_LOG_TAG, "end getting movie from API");
            return movie;
        }
    }
}
