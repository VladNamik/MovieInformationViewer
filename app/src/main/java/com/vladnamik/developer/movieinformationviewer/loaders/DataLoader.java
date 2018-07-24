package com.vladnamik.developer.movieinformationviewer.loaders;


import android.util.Log;

import com.vladnamik.developer.movieinformationviewer.components.Application;
import com.vladnamik.developer.movieinformationviewer.database.DBHelper;
import com.vladnamik.developer.movieinformationviewer.database.entities.Movie;
import com.vladnamik.developer.movieinformationviewer.database.entities.SearchPage;

import java.io.IOException;

public class DataLoader implements IDataLoader {
    private static final String LOG_TAG = "DataLoader";

    private Application application;

    public DataLoader(Application application) {
        this.application = application;
    }

    public SearchPage loadPage(final String search, final int pageNumber) throws IOException {
        Log.d(LOG_TAG, "start loading page from DB");
        DBHelper dbHelper = new DBHelper();
        SearchPage searchPage = dbHelper.getSearchPage(search, pageNumber);
        if (searchPage != null) {
            searchPage.setMovies(dbHelper.getMoviesFromPageAndUpdate(searchPage));
            Log.d(LOG_TAG, "page uploaded successfully");
            return searchPage;
        } else {
            Log.d(LOG_TAG, "Found nothing in DB; start loading page from API");
            searchPage = application.getApiHelper().getPage(search, pageNumber);
//            for (Movie movie: searchPage.getMovies())
//            {
//                loadFullMovieByImdbId(movie.getImdbID());
//            }
            Log.d(LOG_TAG, "end getting page from API");
            return searchPage;
        }
    }

    public Movie loadFullMovieByImdbId(String imdbId) throws IOException {
        Log.d(LOG_TAG, "start loading movie by imdb id from DB");
        DBHelper dbHelper = new DBHelper();
        Movie movie = dbHelper.getMovieByImdbId(imdbId);
        if (movie != null && movie.isFull()) {
            Log.d(LOG_TAG, "movie uploaded successfully");
            return movie;
        } else {
            Log.d(LOG_TAG, "Found nothing in DB; start loading page from API");
            movie = application.getApiHelper().getMovieByImdbId(imdbId);

            Log.d(LOG_TAG, "end getting movie from API");
            return movie;
        }
    }

    public Movie loadFullMovieByTitle(String title) throws IOException {
        Log.d(LOG_TAG, "start loading movie by imdb id from DB");
        DBHelper dbHelper = new DBHelper();
        Movie movie = dbHelper.getMovieByTitle(title);
        if (movie != null && movie.isFull()) {
            Log.d(LOG_TAG, "movie uploaded successfully");
            return movie;
        } else {
            Log.d(LOG_TAG, "Found nothing in DB; start loading page from API");
            movie = application.getApiHelper().getMovieByTitle(title);
            Log.d(LOG_TAG, "end getting movie from API");
            return movie;
        }
    }
}
