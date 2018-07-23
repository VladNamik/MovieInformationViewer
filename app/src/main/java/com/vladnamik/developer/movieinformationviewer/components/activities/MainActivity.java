package com.vladnamik.developer.movieinformationviewer.components.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.vladnamik.developer.movieinformationviewer.R;
import com.vladnamik.developer.movieinformationviewer.components.Application;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_search_movie)
public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity";
    public static final String EXTRA_SEARCH_QUERY = "query";

    @App
    Application app;

    @ViewById(R.id.search_movie_activity_search_view)
    SearchView searchView;

    @AfterViews
    void afterViews() {

        // set an exit transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            searchView.findViewById(R.id.search_src_text)
                    .setTransitionName(getString(R.string.transition_main_search));
        }

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, MovieListActivity_.class);
                intent.putExtra(EXTRA_SEARCH_QUERY, query);
                // Check if we're running on Android 5.0 or higher
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Apply activity transition
                    startActivity(intent,
                            ActivityOptions.makeSceneTransitionAnimation(
                                    MainActivity.this,
                                    searchView.findViewById(R.id.search_src_text),
                                    getString(R.string.transition_main_search)
                            ).toBundle());
                } else {
                    // Swap without transition
                    startActivity(intent);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }
}
