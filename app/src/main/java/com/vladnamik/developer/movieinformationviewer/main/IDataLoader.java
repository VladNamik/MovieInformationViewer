package com.vladnamik.developer.movieinformationviewer.main;

import com.vladnamik.developer.movieinformationviewer.database.entities.Movie;
import com.vladnamik.developer.movieinformationviewer.database.entities.SearchPage;

import java.io.IOException;

public interface IDataLoader {
    public SearchPage loadPage(final String search, final int pageNumber) throws IOException;

    public Movie loadFullMovieByImdbId(String imdbId) throws IOException;

    public Movie loadFullMovieByTitle(String title) throws IOException;
}
