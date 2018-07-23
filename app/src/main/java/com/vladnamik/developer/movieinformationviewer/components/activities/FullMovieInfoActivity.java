package com.vladnamik.developer.movieinformationviewer.components.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.vladnamik.developer.movieinformationviewer.R;
import com.vladnamik.developer.movieinformationviewer.components.fragments.FullMovieInfoFragment;
import com.vladnamik.developer.movieinformationviewer.database.entities.Movie;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

@EActivity(R.layout.activity_only_full_movie_info)
public class FullMovieInfoActivity extends AppCompatActivity implements FullMovieInfoFragment.GetMovieImdbId {

    @Extra(MovieListActivity.EXTRA_MOVIE_IMDB_ID)
    String movieImdbId;

    private Intent shareIntent;

    @Override
    public String getMovieImdbId() {
        return movieImdbId;
    }

    @AfterViews
    public void afterViews() {
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_subject));
        shareIntent.putExtra(Intent.EXTRA_TEXT, Movie.imdbUrlFromId(movieImdbId));

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);

        MenuItem item_share = menu.findItem(R.id.menu_item_share);
        item_share.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(Intent.createChooser(shareIntent,
                        FullMovieInfoActivity.this.getResources()
                                .getString(R.string.share_via)));
                return true;
            }
        });

        MenuItem item_link = menu.findItem(R.id.menu_item_link);
        item_link.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Movie.imdbUrlFromId(movieImdbId)));
                startActivity(i);
                return true;
            }
        });

        return (super.onCreateOptionsMenu(menu));
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
