package com.vladnamik.developer.movieinformationviewer.database.entities;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.vladnamik.developer.movieinformationviewer.database.MovieInfoDB;

import java.util.Date;

@SuppressWarnings("unused")
@Table(database = MovieInfoDB.class)
public class Movie extends BaseModel {
    public enum MovieType {
        series, movie, episode, game
    }

    @Column
    @PrimaryKey(autoincrement = true)
    public int _id;

    @Column
    private Boolean full = false;

    @Column
    @Unique(onUniqueConflict = ConflictAction.REPLACE)
    private String imdbID;

    @Column
    private String Title;

    @Column
    private String Year;

    @Column
    private MovieType Type;

    @Column
    private String Poster;

    @Column
    private String Country;

    @Column
    private Date Released;

    @Column
    private String Genre;

    @Column
    private String Plot;

    @Column
    private Float imdbRating;

    @Column
    private String Rated;

    @Column
    @ForeignKey(onDelete = ForeignKeyAction.CASCADE)
    private MoviePoster moviePoster;

    @Column
    private Date lastSearchDate = new Date();


    public Movie() {
    }

    public Boolean isFull() {
        return full;
    }

    public void setFull(Boolean full) {
        this.full = full;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public MovieType getType() {
        return Type;
    }

    public void setType(MovieType type) {
        Type = type;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public Date getReleased() {
        return Released;
    }

    public void setReleased(Date released) {
        Released = released;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public Float getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Float imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getRated() {
        return Rated;
    }

    public void setRated(String rated) {
        Rated = rated;
    }

    public MoviePoster getBlobPoster() {
        return moviePoster;
    }

    public void setBlobPoster(MoviePoster moviePoster) {
        this.moviePoster = moviePoster;
    }

    public long getId() {
        return _id;
    }

    public void setMoviePoster(MoviePoster moviePoster) {
        this.moviePoster = moviePoster;
    }

    public void setLastSearchDate(Date lastSearchDate) {
        this.lastSearchDate = lastSearchDate;
    }

    public MoviePoster getMoviePoster() {
        return moviePoster;
    }

    public Date getLastSearchDate() {
        return lastSearchDate;
    }

//    @Override
//    public void save() {
//        saveMoviePoster();
//        try {
//            super.save();
//        } catch (SQLiteConstraintException e) {
//            ifSavingProblems(e);
//        }
//    }
//
//    private void ifSavingProblems(SQLiteConstraintException e) {
//        Movie movieToReplace = SQLite.select().from(Movie.class)
//                .where(Movie_Table.imdbID.eq(imdbID)).querySingle();
//        if (movieToReplace == null) {
//            throw e;
//        }
//        if (full && !movieToReplace.isFull()) {
//            _id = movieToReplace._id;
//            movieToReplace.delete();
//            super.save();
//        }
//    }
//
//    private void ifSavingProblems(SQLiteConstraintException e, DatabaseWrapper databaseWrapper) {
//        Movie movieToReplace = SQLite.select().from(Movie.class)
//                .where(Movie_Table.imdbID.eq(imdbID)).querySingle(databaseWrapper);
//        if (movieToReplace == null) {
//            throw e;
//        }
//        if (full && !movieToReplace.isFull()) {
//            _id = movieToReplace._id;
//            movieToReplace.delete(databaseWrapper);
//            super.save(databaseWrapper);
//        }
//    }
//
//    @Override
//    public void save(DatabaseWrapper databaseWrapper) {
//        saveMoviePoster(databaseWrapper);
//        try {
//            super.save(databaseWrapper);
//        } catch (SQLiteConstraintException e) {
//            ifSavingProblems(e);
//        }
//    }
//
//    @Override
//    public void update() {
//        saveMoviePoster();
//        try {
//            super.update();
//        } catch (SQLiteConstraintException e) {
//            ifSavingProblems(e);
//        }
//    }
//
//    @Override
//    public void update(DatabaseWrapper databaseWrapper) {
//        saveMoviePoster(databaseWrapper);
//        try {
//            super.update(databaseWrapper);
//        } catch (SQLiteConstraintException e) {
//            ifSavingProblems(e);
//        }
//    }
//
//    @Override
//    public void insert() {
//        saveMoviePoster();
//        try {
//            super.insert();
//        } catch (SQLiteConstraintException e) {
//            ifSavingProblems(e);
//        }
//    }
//
//    @Override
//    public void insert(DatabaseWrapper databaseWrapper) {
//        saveMoviePoster(databaseWrapper);
//        try {
//            super.insert(databaseWrapper);
//        } catch (SQLiteConstraintException e) {
//            ifSavingProblems(e);
//        }
//    }
//
//    private void saveMoviePoster() {
//        getMoviePoster();
//        if (moviePoster == null && full) {
//            moviePoster = new MoviePoster();
//            if (getPoster().equals("N/A")){
//                moviePoster.save();
//            } else {
//                throw new NoPosterException();
//            }
//        }
//    }
//
//    private void saveMoviePoster(DatabaseWrapper databaseWrapper) {
//        getMoviePoster();
//        if (moviePoster == null && full) {
//            moviePoster = new MoviePoster();
//            if (getPoster().equals("N/A")){
//                moviePoster.save(databaseWrapper);
//            } else {
//                throw new NoPosterException();
//            }
//        }
//    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("title: ").append(Title)
                .append("; year: ").append(Year)
                .append("; imdb id: ").append(imdbID)
                .append("; type: ").append(Type)
                .append("; release date: ").append(Released)
                .append("; plot: ").append(Plot)
                .append("; poster: ").append(getMoviePoster() == null ? "null" : getMoviePoster()._id)
                .append("; date: ").append(lastSearchDate)
                .append("; id: ").append(_id);
        return builder.toString();
    }
}
