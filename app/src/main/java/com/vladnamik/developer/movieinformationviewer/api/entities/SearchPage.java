package com.vladnamik.developer.movieinformationviewer.api.entities;

import java.util.List;

@SuppressWarnings("unused")
public class SearchPage {
    private List<Movie> Search;
    private Integer totalResults;

    public List<Movie> getMovies() {
        return Search;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("totalResults = ").append(totalResults).append('\n');
        for (Movie movie : Search) {
            builder.append(movie.toString()).append('\n');
        }
        return builder.toString();
    }
}
