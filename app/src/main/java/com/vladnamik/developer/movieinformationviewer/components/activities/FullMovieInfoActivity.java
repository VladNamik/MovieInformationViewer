package com.vladnamik.developer.movieinformationviewer.components.activities;

import android.support.v7.app.AppCompatActivity;

import com.vladnamik.developer.movieinformationviewer.R;
import com.vladnamik.developer.movieinformationviewer.components.fragments.FullMovieInfoFragment;

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
}
