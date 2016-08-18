package com.vladnamik.developer.movieinformationviewer.database.entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyAction;
import com.raizlabs.android.dbflow.annotation.NotNull;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.annotation.UniqueGroup;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.vladnamik.developer.movieinformationviewer.database.MovieInfoDB;

import java.util.List;

@SuppressWarnings("unused")
@Table(database = MovieInfoDB.class, uniqueColumnGroups = @UniqueGroup(groupNumber = 1))
public class SearchPage extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    long _id;

    @Column
    @NotNull
    @Unique(unique = false, uniqueGroups = 1)
    private Integer pageNumber;

    @Column
    @NotNull
    @ForeignKey(onDelete = ForeignKeyAction.CASCADE)
    @Unique(unique = false, uniqueGroups = 1)
    private SearchQuery searchQuery;

    private Integer totalResults;

    List<Movie> Search;

    public SearchPage() {

    }

    public SearchPage(int pageNumber, SearchQuery searchQuery) {
        this.pageNumber = pageNumber;
        this.searchQuery = searchQuery;
    }

    public long getId() {
        return _id;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public SearchQuery getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public List<Movie> getMovies() {
        return Search;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("pageNumber = ").append(getPageNumber()).append("; searchQuery = ")
                .append(getSearchQuery()).append("; id = ").append(getId()).append('\n');
        builder.append("totalResults = ").append(totalResults).append('\n');
        if (Search != null) {
            for (Movie movie : Search) {
                builder.append(movie.toString()).append('\n');
            }
        }
        return builder.toString();
    }


}
