package com.vladnamik.developer.movieinformationviewer.api;


import com.vladnamik.developer.movieinformationviewer.database.entities.Movie;
import com.vladnamik.developer.movieinformationviewer.database.entities.SearchPage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface OmdbAPI {
    String BASE_URL = "http://www.omdbapi.com/";
    String DATE_FORMAT_PATTERN = "dd MMM yyyy";
    String API_KEY = "6d2e3336";

    @GET("/?apikey=" + API_KEY)
    Call<SearchPage> search(@Query("s") String name, @Query("page") int pageNumber);

    @GET("/?apikey=" + API_KEY)
    Call<Movie> getMovieByImdbId(@Query("i") String id);

    @GET("/?apikey=" + API_KEY)
    Call<Movie> getMovieByTitle(@Query("t") String title);


}
