package com.vladnamik.developer.movieinformationviewer.api;

import com.vladnamik.developer.movieinformationviewer.database.DBHelper;
import com.vladnamik.developer.movieinformationviewer.database.entities.Movie;
import com.vladnamik.developer.movieinformationviewer.database.entities.SearchPage;

import java.io.IOException;

public class APIHelper {
    private static final String LOG_TAG = "APIHelper";
    private OmdbAPI api;

    public APIHelper(OmdbAPI api) {
        this.api = api;
    }

    public SearchPage getPage(final String search, final int pageNumber) throws IOException {
        SearchPage searchPage = api.search(search, pageNumber).execute().body();
        if (searchPage.getTotalResults() == null) {
            return null;
        }

        searchPage = new DBHelper().savePage(search, searchPage, pageNumber);

        return searchPage;
    }

    public Movie getMovieByImdbId(String id) throws IOException {
        Movie movie = api.getMovieByImdbId(id).execute().body();
        movie.setFull(true);
        if (movie.getImdbID() == null) {
            return null;
        }

        movie = new DBHelper().saveMovie(movie);

        return movie;
    }

    public Movie getMovieByTitle(String title) throws IOException {
        Movie movie = api.getMovieByTitle(title).execute().body();
        movie.setFull(true);
        if (movie.getImdbID() == null) {
            return null;
        }

        movie = new DBHelper().saveMovie(movie);

        return movie;
    }


}
