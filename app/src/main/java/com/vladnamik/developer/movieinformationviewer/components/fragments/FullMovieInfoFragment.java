package com.vladnamik.developer.movieinformationviewer.components.fragments;


import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.vladnamik.developer.movieinformationviewer.R;
import com.vladnamik.developer.movieinformationviewer.components.Application;
import com.vladnamik.developer.movieinformationviewer.database.entities.Movie;
import com.vladnamik.developer.movieinformationviewer.main.DataLoader;
import com.vladnamik.developer.movieinformationviewer.main.DataLoaderMock;
import com.vladnamik.developer.movieinformationviewer.main.DataLoaderServiceBased;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.Locale;

@EFragment(R.layout.fragment_full_movie_info)
public class FullMovieInfoFragment extends Fragment {
    private static final String FULL_MOVIE_INFO_FRAGMENT_LOG_TAG = "FullMovieInfoFragment";

    private String movieImdbId;
    private Movie movie;

    @ViewById(R.id.full_movie_info_fragment_film_description_textview)
    TextView filmDescriptionTextView;

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        movieImdbId = ((GetMovieImdbId) getActivity()).getMovieImdbId();

        getMovieAndFill();
    }

    @Background
    void getMovieAndFill() {
        try {
            Log.d(FULL_MOVIE_INFO_FRAGMENT_LOG_TAG, "trying to upload movie");
            movie = new DataLoader((Application)getActivity().getApplication())
                    .loadFullMovieByImdbId(movieImdbId);
            Log.d(FULL_MOVIE_INFO_FRAGMENT_LOG_TAG, "movie was uploaded successfully");
            fillMovieFields();
        } catch (IOException e) {
            Log.d(FULL_MOVIE_INFO_FRAGMENT_LOG_TAG, "problems with movie uploading");
            e.printStackTrace();
        }
    }

    @UiThread
    void fillMovieFields() {
        titleTextView.setText(movie.getTitle() + " (" + movie.getType() + ", " + movie.getYear() + ")");
        filmDescriptionTextView.setText(movie.getPlot());
        countryTextView.setText(movie.getCountry());
        genresTextView.setText(movie.getGenre());
        imdbRatingTextView.setText(String.format(Locale.getDefault(), "%.2f", movie.getImdbRating()));
        ratedTextView.setText(movie.getRated());
        if (movie.getReleased() != null) {
            releaseDateTextView.setText(movie.getReleased().toString());
        }
        Log.d(FULL_MOVIE_INFO_FRAGMENT_LOG_TAG, movie.getPoster());
        if (movie.getMoviePoster() != null && movie.getMoviePoster().getMoviePoster() != null) {
            byte[] moviePosterBlob = movie.getMoviePoster().getMoviePoster().getBlob();
            posterImageView.setImageBitmap(BitmapFactory.decodeByteArray(moviePosterBlob, 0, moviePosterBlob.length));
        }
    }

    public interface GetMovieImdbId {
        String getMovieImdbId();
    }
}
