package com.vladnamik.developer.movieinformationviewer.database.entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.NotNull;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.vladnamik.developer.movieinformationviewer.database.MovieInfoDB;

import java.util.Date;

@SuppressWarnings("unused")
@Table(database = MovieInfoDB.class)
public class SearchQuery extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    long _id;

    @Column
    @Unique
    private String query;

    @Column
    private Date lastSearchDate;

    @Column
    @NotNull
    private Integer moviesCount;

    public SearchQuery() {
    }

    public SearchQuery(String query, Date lastSearchDate) {
        this.query = query;
        this.lastSearchDate = lastSearchDate;
    }

    public long getId() {
        return _id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Date getLastSearchDate() {
        return lastSearchDate;
    }

    public void setLastSearchDate(Date lastSearchDate) {
        this.lastSearchDate = lastSearchDate;
    }

    public Integer getMoviesCount() {
        return moviesCount;
    }

    public void setMoviesCount(Integer moviesCount) {
        this.moviesCount = moviesCount;
    }

    @Override
    public String toString() {
        return "id = " + _id + "; query = " + query + ";date = " + getLastSearchDate() + "; movies quantity = " + moviesCount;
    }
}
