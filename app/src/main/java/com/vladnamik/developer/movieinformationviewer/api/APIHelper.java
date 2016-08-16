package com.vladnamik.developer.movieinformationviewer.api;


import com.vladnamik.developer.movieinformationviewer.api.entities.Movie;
import com.vladnamik.developer.movieinformationviewer.api.entities.SearchPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class APIHelper {
    private OmdbAPI api;

    public APIHelper(OmdbAPI api) {
        this.api = api;
    }

    public List<Movie> getMoviesFromAllPages(String search) throws IOException {
        SearchPage searchPage = api.search(search, 1).execute().body();
        List<Movie> allMovies = new ArrayList<>(searchPage.getTotalResults());
        int pageNumber = 2;
        while (searchPage.getMovies() != null) {
            allMovies.addAll(searchPage.getMovies());
            searchPage = api.search(search, pageNumber).execute().body();
            pageNumber++;
        }
        return allMovies;
    }

    public List<Movie> getMoviesFullInfoFromPage(SearchPage page) throws IOException {
        List<Movie> movies = page.getMovies();
        List<Movie> fullInfoMovies = new ArrayList<>();
        for (Movie movie : movies) {
            fullInfoMovies.add(api.getMovieByImdbId(movie.getImdbID()).execute().body());
        }
        return fullInfoMovies;
    }

    public OmdbAPI getApi() {
        return api;
    }
}
