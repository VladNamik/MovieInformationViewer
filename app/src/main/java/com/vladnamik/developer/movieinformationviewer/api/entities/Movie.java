package com.vladnamik.developer.movieinformationviewer.api.entities;

import java.util.Date;

@SuppressWarnings("unused")
public class Movie {
    public enum MovieType {
        series, movie, episode
    }

    private String Title;
    private String Year;
    private String imdbID;
    private MovieType Type;
    private String Poster;
    private String Country;
    private Date Released;
    private String Genre;
    private String Plot;
    private Float imdbRating;
    private String Rated;

    public String getTitle() {
        return Title;
    }

    public String getYear() {
        return Year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public MovieType getType() {
        return Type;
    }

    public String getPoster() {
        return Poster;
    }

    public String getCountry() {
        return Country;
    }

    public Date getReleased() {
        return Released;
    }

    public String getGenre() {
        return Genre;
    }

    public String getPlot() {
        return Plot;
    }

    public Float getImdbRating() {
        return imdbRating;
    }

    public String getRated() {
        return Rated;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("title: ").append(Title)
                .append("; year: ").append(Year)
                .append("; imdb id: ").append(imdbID)
                .append("; type: ").append(Type)
                .append("; release date: ").append(Released)
                .append("; plot: ").append(Plot);
        return builder.toString();
    }
}
