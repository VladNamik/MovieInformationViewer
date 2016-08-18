package com.vladnamik.developer.movieinformationviewer.api;

import android.os.AsyncTask;
import android.util.Pair;

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

    public void getPageAsync(final String search, final int pageNumber, final Callback<SearchPage> callback) {
        new PageAsync(search, pageNumber, callback).execute();
    }

    private class PageAsync extends AsyncTask<Void, Void, Pair<SearchPage, Throwable>> {
        private final String search;
        private final int pageNumber;
        private final Callback<SearchPage> callback;

        public PageAsync(String search, int pageNumber, Callback<SearchPage> callback) {
            this.callback = callback;
            this.search = search;
            this.pageNumber = pageNumber;
        }

        @Override
        protected Pair<SearchPage, Throwable> doInBackground(Void... voids) {
            try {
                SearchPage searchPage = api.search(search, pageNumber).execute().body();
                if (searchPage.getTotalResults() == null) {
                    return new Pair<>(null, null);
                }

                searchPage = new DBService().savePage(search, searchPage, pageNumber);

                return new Pair<>(searchPage, null);
            } catch (IOException e) {
                return new Pair<>(null, (Throwable) e);
            }
        }

        @Override
        protected void onPostExecute(Pair<SearchPage, Throwable> searchPageThrowablePair) {
            if (searchPageThrowablePair.second != null) {
                callback.onFailure(searchPageThrowablePair.second);
            } else {
                callback.onResponse(searchPageThrowablePair.first);
            }
        }
    }

    public void getMovieByImdbIdAsync(String id, final Callback<Movie> callback) {
        new MovieByImdbIdAsync(id, callback).execute();
    }

    private class MovieByImdbIdAsync extends AsyncTask<Void, Void, Pair<Movie, Throwable>> {
        private String id;
        private Callback<Movie> callback;

        public MovieByImdbIdAsync(String id, Callback<Movie> callback) {
            this.id = id;
            this.callback = callback;
        }

        @Override
        protected Pair<Movie, Throwable> doInBackground(Void... voids) {
            try {
                Movie movie = api.getMovieByImdbId(id).execute().body();
                movie.setFull(true);
                if (movie.getImdbID() == null) {
                    return new Pair<>(null, null);
                }

                movie = new DBService().saveMovie(movie);

                return new Pair<>(movie, null);
            } catch (IOException e) {
                return new Pair<>(null, (Throwable) e);
            }
        }

        @Override
        protected void onPostExecute(Pair<Movie, Throwable> movieThrowablePair) {
            if (movieThrowablePair.second != null) {
                callback.onFailure(movieThrowablePair.second);
            } else {
                callback.onResponse(movieThrowablePair.first);
            }
        }
    }

    public void getMovieByTitleAsync(String title, final Callback<Movie> callback) {
        new MovieByTitleAsync(title, callback).execute();
    }

    private class MovieByTitleAsync extends AsyncTask<Void, Void, Pair<Movie, Throwable>> {
        private String title;
        private Callback<Movie> callback;

        public MovieByTitleAsync(String title, Callback<Movie> callback) {
            this.title = title;
            this.callback = callback;
        }

        @Override
        protected Pair<Movie, Throwable> doInBackground(Void... voids) {
            try {
                Movie movie = api.getMovieByTitle(title).execute().body();
                movie.setFull(true);
                if (movie.getImdbID() == null) {
                    return new Pair<>(null, null);
                }

                movie = new DBService().saveMovie(movie);

                return new Pair<>(movie, null);
            } catch (IOException e) {
                return new Pair<>(null, (Throwable) e);
            }
        }

        @Override
        protected void onPostExecute(Pair<Movie, Throwable> movieThrowablePair) {
            if (movieThrowablePair.second != null) {
                callback.onFailure(movieThrowablePair.second);
            } else {
                callback.onResponse(movieThrowablePair.first);
            }
        }
    }

    public interface Callback<T> {
        void onResponse(T data);

        void onFailure(Throwable t);
    }

}
