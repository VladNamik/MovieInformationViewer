package com.vladnamik.developer.movieinformationviewer.api;

import com.vladnamik.developer.movieinformationviewer.database.DBService;
import com.vladnamik.developer.movieinformationviewer.database.entities.Movie;
import com.vladnamik.developer.movieinformationviewer.database.entities.SearchPage;

import java.io.IOException;

public class APIHelper {
    private static final String API_HELPER_LOG_TAG = "APIHelper";
    private OmdbAPI api;

    public APIHelper(OmdbAPI api) {
        this.api = api;
    }

//
//    public List<Movie> getMoviesFromAllPages(String search) throws IOException {
//        SearchPage searchPage = api.search(search, 1).execute().body();
//        List<Movie> allMovies = new ArrayList<>(searchPage.getTotalResults());
//        int pageNumber = 2;
//        while (searchPage.getMovies() != null) {
//            allMovies.addAll(searchPage.getMovies());
//            searchPage = api.search(search, pageNumber).execute().body();
//            pageNumber++;
//        }
//        return allMovies;
//    }
//
//    public List<Movie> getMoviesFullInfoFromPage(SearchPage page) throws IOException {
//        List<Movie> movies = page.getMovies();
//        List<Movie> fullInfoMovies = new ArrayList<>();
//        for (Movie movie : movies) {
//            fullInfoMovies.add(api.getMovieByImdbId(movie.getImdbID()).execute().body());
//        }
//        return fullInfoMovies;
//    }

    public SearchPage getPage(final String search, final int pageNumber) throws IOException {
        SearchPage searchPage = api.search(search, pageNumber).execute().body();
        if (searchPage.getTotalResults() == null) {
            return null;
        }

        searchPage = new DBService().savePage(search, searchPage, pageNumber);

        return searchPage;
    }

    public Movie getMovieByImdbId(String id) throws IOException {
        Movie movie = api.getMovieByImdbId(id).execute().body();
        movie.setFull(true);
        if (movie.getImdbID() == null) {
            return null;
        }

        movie = new DBService().saveMovie(movie);

        return movie;
    }

    public Movie getMovieByTitle(String title) throws IOException {
        Movie movie = api.getMovieByTitle(title).execute().body();
        movie.setFull(true);
        if (movie.getImdbID() == null) {
            return null;
        }

        movie = new DBService().saveMovie(movie);

        return movie;
    }


}
