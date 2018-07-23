package com.vladnamik.developer.movieinformationviewer.components.activities;


import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.vladnamik.developer.movieinformationviewer.R;
import com.vladnamik.developer.movieinformationviewer.components.fragments.MovieListFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

import java.util.Locale;

@EActivity(R.layout.activity_only_movie_list)
public class MovieListActivity extends AppCompatActivity
        implements MovieListFragment.GetQueryParams, MovieListFragment.OnMovieSelected,
        SearchView.OnQueryTextListener {
    private static final String LOG_TAG = "MovieListActivity";
    public static final String EXTRA_MOVIE_IMDB_ID = "movieImdbId";

    @Extra(MainActivity.EXTRA_SEARCH_QUERY)
    @InstanceState
    String searchQuery;
    int moviesInQuery = 0;

    @ViewById(R.id.header_for_movie_listview_container)
    View headerView;
    @ViewById(R.id.header_for_movie_listview_search_query)
    TextView headerSearchQuery;
    @ViewById(R.id.header_for_movie_listview_search_results_number)
    TextView headerSearchResultsNumber;

    @Override
    public String getQuery() {
        return searchQuery;
    }

    @Override
    public void setQueryParams(String newQuery, int moviesInQuery) {
        this.searchQuery = newQuery;
        headerSearchQuery.setText(searchQuery);
        this.moviesInQuery = moviesInQuery;
        headerSearchResultsNumber.setText(String.format(Locale.getDefault(), "%d", moviesInQuery));
    }

    @Override
    public void onMovieSelected(String movieImdbId) {
        Intent intent = new Intent(this, FullMovieInfoActivity_.class);
        intent.putExtra(EXTRA_MOVIE_IMDB_ID, movieImdbId);
        startActivity(intent);
    }

    @AfterViews
    public void afterViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setAllowEnterTransitionOverlap(true);
        }

        headerView.setClickable(false);
        setQueryParams(searchQuery, moviesInQuery);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_with_searchview, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Log.d(LOG_TAG, "onQueryTextSubmit() " + s);
        searchQuery = s;
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
