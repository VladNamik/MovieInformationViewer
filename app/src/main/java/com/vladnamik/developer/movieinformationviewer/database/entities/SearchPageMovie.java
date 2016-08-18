package com.vladnamik.developer.movieinformationviewer.database.entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.annotation.UniqueGroup;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.vladnamik.developer.movieinformationviewer.database.MovieInfoDB;

@SuppressWarnings("unused")
@Table(database = MovieInfoDB.class, uniqueColumnGroups = @UniqueGroup(groupNumber = 1))
public class SearchPageMovie extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    long _id;

    @Column
    @ForeignKey(onDelete = ForeignKeyAction.CASCADE)
    @Unique(unique = false, uniqueGroups = 1)
    private SearchPage searchPage;

    @Column
    @ForeignKey(onDelete = ForeignKeyAction.CASCADE)
    @Unique(unique = false, uniqueGroups = 1)
    private Movie movie;

    public SearchPageMovie() {
    }

    public SearchPageMovie(SearchPage searchPage, Movie movie) {
        this.searchPage = searchPage;
        this.movie = movie;
    }

    public long getId() {
        return _id;
    }


    public SearchPage getSearchPage() {
        return searchPage;
    }

    public void setSearchPage(SearchPage searchPage) {
        this.searchPage = searchPage;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
