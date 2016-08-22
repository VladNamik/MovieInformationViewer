package com.vladnamik.developer.movieinformationviewer.components.activities;


import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.vladnamik.developer.movieinformationviewer.R;
import com.vladnamik.developer.movieinformationviewer.components.fragments.MovieListFragment;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

@EActivity(R.layout.activity_only_movie_list)
public class MovieListActivity extends AppCompatActivity
        implements MovieListFragment.GetQueryParams, MovieListFragment.OnMovieSelected,
        SearchView.OnQueryTextListener {
    private static final String MOVIE_LIST_ACTIVITY_LOG_TAG = "MovieListActivity";
    public static final String EXTRA_MOVIE_IMDB_ID = "movieImdbId";

    @Extra(MainActivity.EXTRA_SEARCH_QUERY)
    String searchQuery;

    @Override
    public String getQuery() {
        return searchQuery;
    }

    @Override
    public void onMovieSelected(String movieImdbId) {
        Intent intent = new Intent(this, FullMovieInfoActivity_.class);
        intent.putExtra(EXTRA_MOVIE_IMDB_ID, movieImdbId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_with_searchview, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Enter movie name");
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Log.d(MOVIE_LIST_ACTIVITY_LOG_TAG, "onQueryTextSubmit() " + s);
        MovieListFragment movieListFragment = (MovieListFragment) getFragmentManager()
                .findFragmentById(R.id.only_movie_list_activity_list_fragment);
        movieListFragment.refreshQuery(s);
        return false;
    }


    @Override
    public boolean onQueryTextChange(String s) {
//        Log.d(MOVIE_LIST_ACTIVITY_LOG_TAG, "onQueryTextChange() " + s);
        return false;
    }


}
