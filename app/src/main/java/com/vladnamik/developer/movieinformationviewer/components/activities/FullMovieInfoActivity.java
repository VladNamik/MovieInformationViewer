package com.vladnamik.developer.movieinformationviewer.components.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.vladnamik.developer.movieinformationviewer.R;
import com.vladnamik.developer.movieinformationviewer.components.fragments.FullMovieInfoFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

@EActivity(R.layout.activity_only_full_movie_info)
public class FullMovieInfoActivity extends AppCompatActivity implements FullMovieInfoFragment.GetMovieImdbId {

    @Extra(MovieListActivity.EXTRA_MOVIE_IMDB_ID)
    String movieImdbId;

    @Override
    public String getMovieImdbId() {
        return movieImdbId;
    }

    @AfterViews
    public void createAppBar()
    {
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
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
}
