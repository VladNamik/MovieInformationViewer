package com.vladnamik.developer.movieinformationviewer.components.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;

import com.vladnamik.developer.movieinformationviewer.R;
import com.vladnamik.developer.movieinformationviewer.components.Application;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_search_movie)
public class MainActivity extends AppCompatActivity {
    private static final String MAIN_ACTIVITY_LOG_TAG = "MainActivity";
    public static final String EXTRA_SEARCH_QUERY = "query";

    @App
    Application app;

    @ViewById(R.id.search_movie_activity_search_view)
    SearchView searchView;

    @AfterViews
    void afterViews() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, MovieListActivity_.class);
                intent.putExtra(EXTRA_SEARCH_QUERY, query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
