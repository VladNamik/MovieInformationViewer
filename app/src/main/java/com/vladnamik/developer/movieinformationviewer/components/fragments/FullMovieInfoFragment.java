package com.vladnamik.developer.movieinformationviewer.components.fragments;


import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vladnamik.developer.movieinformationviewer.R;
import com.vladnamik.developer.movieinformationviewer.components.Application;
import com.vladnamik.developer.movieinformationviewer.database.entities.Movie;
import com.vladnamik.developer.movieinformationviewer.loaders.DataLoader;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.Locale;

@EFragment(R.layout.fragment_full_movie_info)
public class FullMovieInfoFragment extends Fragment {
    private static final String LOG_TAG = "FullMovieInfoFragment";

    private String movieImdbId;
    private Movie movie;

    @ViewById(R.id.full_movie_info_fragment_film_description_textview)
    TextView filmDescriptionTextView;

    @ViewById(R.id.full_movie_info_fragment_no_image)
    TextView posterNoImageTextView;

    @ViewById(R.id.full_movie_info_fragment_poster_imageview)
    ImageView posterImageView;

    @ViewById(R.id.full_movie_info_fragment_title_textview)
    TextView titleTextView;

    @ViewById(R.id.full_movie_info_fragment_country_textview)
    TextView countryTextView;

    @ViewById(R.id.full_movie_info_fragment_genres_textview)
    TextView genresTextView;

    @ViewById(R.id.full_movie_info_fragment_imdb_rating_textview)
    TextView imdbRatingTextView;

    @ViewById(R.id.full_movie_info_fragment_rated_textview)
    TextView ratedTextView;

    @ViewById(R.id.full_movie_info_fragment_release_date_textview)
    TextView releaseDateTextView;

    @ViewById(R.id.full_movie_info_fragment_full_image_container)
    View fullImageContainer;

    @ViewById(R.id.full_movie_info_fragment_full_image)
    ImageView fullImageView;

    @ViewById(R.id.full_movie_info_fragment_country_container)
    View countryContainer;

    @ViewById(R.id.full_movie_info_fragment_release_date_container)
    View releaseDateContainer;

    @ViewById(R.id.full_movie_info_fragment_rated_container)
    View ratedContainer;

    @ViewById(R.id.full_movie_info_fragment_imdb_rating_container)
    View imdbRatingContainer;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        movieImdbId = ((GetMovieImdbId) getActivity()).getMovieImdbId();

        getMovieAndFill();
    }

    @Background
    void getMovieAndFill() {
        try {
            Log.d(LOG_TAG, "trying to upload movie");
            movie = new DataLoader((Application) getActivity().getApplication())
                    .loadFullMovieByImdbId(movieImdbId);
            Log.d(LOG_TAG, "movie was uploaded successfully");
            fillMovieFields();
        } catch (IOException e) {
            Log.d(LOG_TAG, "problems with movie uploading");
            e.printStackTrace();
        }
    }

    @UiThread
    void fillMovieFields() {
        if (titleTextView == null)
            return;

        titleTextView.setText(String.format("%s (%s, %s)", movie.getTitle(), movie.getType(), movie.getYear()));
        filmDescriptionTextView.setText(movie.getPlot());

        if (movie.isCountryKnown()) {
            countryTextView.setText(movie.getCountry());
        } else {
            countryContainer.setVisibility(View.GONE);
        }

        genresTextView.setText(movie.getGenre());

        if (movie.isImdbRatingKnown()) {
            imdbRatingTextView.setText(String.format(Locale.getDefault(), "%.2f", movie.getImdbRating()));
        } else {
            imdbRatingContainer.setVisibility(View.GONE);
        }

        if (movie.isRatedKnown()) {
            ratedTextView.setText(movie.getRated());
        } else {
            ratedContainer.setVisibility(View.GONE);
        }

        if (movie.isReleasedKnown()) {
            releaseDateTextView.setText(movie.getReleased().toString());
        } else {
            releaseDateContainer.setVisibility(View.GONE);
        }

        Log.d(LOG_TAG, movie.getPoster());
        if (movie.getMoviePoster() != null && movie.getMoviePoster().getMoviePoster() != null) {
            byte[] moviePosterBlob = movie.getMoviePoster().getMoviePoster().getBlob();
            posterImageView.setImageBitmap(BitmapFactory.decodeByteArray(moviePosterBlob, 0, moviePosterBlob.length));
            fullImageView.setImageBitmap(BitmapFactory.decodeByteArray(moviePosterBlob, 0, moviePosterBlob.length));

            posterImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFullScreenPoster(true);
                }
            });
            fullImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFullScreenPoster(false);
                }
            });
            fullImageContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFullScreenPoster(false);
                }
            });
        } else {
            posterNoImageTextView.setText(R.string.no_image);
        }
    }

    private void setFullScreenPoster(boolean isFullScreen) {
        if (isFullScreen) {
            fullImageContainer.setVisibility(View.VISIBLE);
        } else {
            fullImageContainer.setVisibility(View.GONE);
        }
    }

    public interface GetMovieImdbId {
        String getMovieImdbId();
    }
}
