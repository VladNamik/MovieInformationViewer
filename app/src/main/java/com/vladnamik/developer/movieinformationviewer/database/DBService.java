package com.vladnamik.developer.movieinformationviewer.database;

import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.vladnamik.developer.movieinformationviewer.database.entities.Movie;
import com.vladnamik.developer.movieinformationviewer.database.entities.MoviePoster;
import com.vladnamik.developer.movieinformationviewer.database.entities.Movie_Table;
import com.vladnamik.developer.movieinformationviewer.database.entities.SearchPage;
import com.vladnamik.developer.movieinformationviewer.database.entities.SearchPageMovie;
import com.vladnamik.developer.movieinformationviewer.database.entities.SearchPageMovie_Table;
import com.vladnamik.developer.movieinformationviewer.database.entities.SearchPage_Table;
import com.vladnamik.developer.movieinformationviewer.database.entities.SearchQuery;
import com.vladnamik.developer.movieinformationviewer.database.entities.SearchQuery_Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBService {
    private static final String DB_SERVICE_LOG_TAG = "DBService";

    public String getAllDBInfo(boolean deleteAll) {

        Log.d(DB_SERVICE_LOG_TAG, "start getting movie posters info");
        List<MoviePoster> moviePosters = SQLite.select().from(MoviePoster.class).queryList();
        StringBuilder moviePostersString = new StringBuilder("Movie Posters:\n");
        for (MoviePoster moviePoster : moviePosters) {
            moviePostersString.append(moviePoster.toString()).append("\n");
            if (deleteAll) {
                moviePoster.delete();
            }
        }

        Log.d(DB_SERVICE_LOG_TAG, "start getting movies info");
        List<Movie> movies = SQLite.select().from(Movie.class).queryList();
        StringBuilder moviesString = new StringBuilder("Movies:\n");
        for (Movie movie : movies) {
            moviesString.append(movie.toString()).append("\n");
            if (deleteAll) {
                movie.delete();
            }
        }


        Log.d(DB_SERVICE_LOG_TAG, "start getting search page movies info");
        List<SearchPageMovie> searchPageMovies = SQLite.select().from(SearchPageMovie.class).queryList();
        StringBuilder searchPageMoviesString = new StringBuilder("Search Page Movies:\n");
        for (SearchPageMovie searchPageMovie : searchPageMovies) {
            searchPageMoviesString.append(searchPageMovie.toString()).append("\n");
            if (deleteAll) {
                searchPageMovie.delete();
            }
        }


        Log.d(DB_SERVICE_LOG_TAG, "start getting search pages info");
        List<SearchPage> searchPages = SQLite.select().from(SearchPage.class).queryList();
        StringBuilder searchPagesString = new StringBuilder("Search Pages:\n");
        for (SearchPage searchPage : searchPages) {
            searchPagesString.append(searchPage.toString()).append("\n");
            if (deleteAll) {
                searchPage.delete();
            }
        }

        Log.d(DB_SERVICE_LOG_TAG, "start getting search queries info");
        List<SearchQuery> searchQueries = SQLite.select().from(SearchQuery.class).queryList();
        StringBuilder searchQueriesString = new StringBuilder("Search Queries:\n");
        for (SearchQuery searchQuery : searchQueries) {
            searchQueriesString.append(searchQuery.toString()).append("\n");
            if (deleteAll) {
                searchQuery.delete();
            }
        }

        Log.d(DB_SERVICE_LOG_TAG, "end method .getAllDBInfo()");
        return searchQueriesString.append(searchPagesString.toString())
                .append(searchPageMoviesString.toString())
                .append(moviesString.toString())
                .append(moviePostersString.toString())
                .toString();
    }

    public SearchQuery saveSearchQuery(String search, Integer moviesCount) {
        SearchQuery searchQuery = SQLite.select().from(SearchQuery.class)
                .where(SearchQuery_Table.query.eq(search)).querySingle();
        if (searchQuery == null) {
            searchQuery = new SearchQuery(search, new Date());
        } else {
            searchQuery.setLastSearchDate(new Date());
        }
        searchQuery.setMoviesCount(moviesCount);
        searchQuery.save();
        return searchQuery;
    }

    public SearchPage savePage(String search, SearchPage searchPage, int pageNumber) {
        if (searchPage == null) {
            return null;
        }

        SearchQuery searchQuery = saveSearchQuery(search, searchPage.getTotalResults());

        SearchPage searchPageFromDB = SQLite.select().from(SearchPage.class)
                .where(SearchPage_Table.pageNumber.eq(pageNumber))
                .and(SearchPage_Table.searchQuery__id.eq(searchQuery.getId()))
                .querySingle();


        if (searchPageFromDB != null) {

            IProperty[] properties = Movie_Table.getAllColumnProperties();
            for (int i = 0; i < properties.length; i++) {
                properties[i] = properties[i].withTable();
            }

            List<Movie> moviesFromDB = SQLite.select(properties).from(Movie.class)
                    .innerJoin(SearchPageMovie.class).on(Movie_Table._id.withTable().eq(SearchPageMovie_Table.movie__id))
                    .innerJoin(SearchPage.class).on(SearchPageMovie_Table.searchPage__id.eq(SearchPage_Table._id.withTable()))
                    .where(SearchPage_Table._id.withTable().eq(searchPageFromDB.getId())).queryList();

            updateMoviesLastSearchDate(moviesFromDB);
            return searchPageFromDB;
        } else {

            searchPage.setSearchQuery(searchQuery);
            searchPage.setPageNumber(pageNumber);
            searchPage.save();

            if (searchPage.getMovies() != null) {
                List<SearchPageMovie> searchPageMovies = new ArrayList<>(searchPage.getMovies().size());
                for (Movie movie : searchPage.getMovies()) {
                    new SearchPageMovie(searchPage, saveMovie(movie)).save();
                }
            }

            return searchPage;
        }
    }

    public Movie saveMovie(Movie movie) {
        if (movie == null) {
            return null;
        }

        Movie movieFromDB = SQLite.select().from(Movie.class)
                .where(Movie_Table.imdbID.eq(movie.getImdbID())).querySingle();

        if (movieFromDB == null || (!movieFromDB.isFull() && movie.isFull())) {

            movie.setLastSearchDate(new Date());
            if (movieFromDB != null) {
                movie._id = movieFromDB._id;
            }
            movie = saveMoviePoster(movie);
            movie.save();
            return movie;
        } else {

            movieFromDB.setLastSearchDate(new Date());
            movieFromDB.update();
            return movieFromDB;
        }
    }

    public Movie saveMoviePoster(Movie movie) {
        if (movie != null && movie.isFull() && movie.getBlobPoster() == null) {
            MoviePoster moviePoster = new MoviePoster();
            try {
                moviePoster.downloadPoster(movie.getPoster());
                moviePoster.save();
            } catch (IOException e) {
                Log.w(DB_SERVICE_LOG_TAG, "Can't download poster from " + movie.getPoster());
                e.printStackTrace();
            }
            movie.setBlobPoster(moviePoster);
        }
        movie.save();
        return movie;
    }


    public void updateMoviesLastSearchDate(List<Movie> movies) {
        if (movies != null) {
            for (Movie movie : movies) {
                Log.d(DB_SERVICE_LOG_TAG, movie.toString());
                movie.setLastSearchDate(new Date());
                movie.save();
            }
        }
    }

}
