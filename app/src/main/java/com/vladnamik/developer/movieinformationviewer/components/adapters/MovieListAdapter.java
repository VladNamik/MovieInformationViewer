package com.vladnamik.developer.movieinformationviewer.components.adapters;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.raizlabs.android.dbflow.annotation.NotNull;
import com.squareup.picasso.Picasso;
import com.vladnamik.developer.movieinformationviewer.R;
import com.vladnamik.developer.movieinformationviewer.database.entities.Movie;
import com.vladnamik.developer.movieinformationviewer.database.entities.MoviePoster;

import java.util.List;
import java.util.Locale;


public class MovieListAdapter extends ArrayAdapter<Movie> {
    private Context context;

    // View lookup cache
    private static class ViewHolder {
        TextView movieName;
        TextView movieTypeYear;
        TextView movieRating;
        ImageView poster;
    }

    public MovieListAdapter(List<Movie> data, Context context) {
        super(context, R.layout.movie_list_view_item, data);
        this.context = context;

    }

    @Override
    public @NotNull View getView(int position, @Nullable View convertView, @NotNull ViewGroup parent) {
        // Get the data item for this position
        Movie movie = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.movie_list_view_item, parent, false);
            viewHolder.movieName = convertView.findViewById(R.id.movie_list_view_item_name);
            viewHolder.movieTypeYear = convertView.findViewById(R.id.movie_list_view_item_type_year);
            viewHolder.movieRating = convertView.findViewById(R.id.movie_list_view_item_rating);
            viewHolder.poster = convertView.findViewById(R.id.movie_list_view_item_poster);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // TODO: add animation (?)
//        View result = convertView;
//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
//        lastPosition = position;

        if (movie != null) {
            viewHolder.movieName.setText(movie.getTitle());
            viewHolder.movieTypeYear.setText(String.format(Locale.getDefault(),
                    "%s, %s", movie.getType().toString(), movie.getYear()));

            if (movie.getImdbRating() != null) {
                convertView.findViewById(R.id.movie_list_view_item_rating_line).setVisibility(View.VISIBLE);
                viewHolder.movieRating.setText(String.format(Locale.getDefault(), "%.2f", movie.getImdbRating()));
            }
            else {
                convertView.findViewById(R.id.movie_list_view_item_rating_line).setVisibility(View.INVISIBLE);
            }

            if (movie.getMoviePoster() != null && movie.getMoviePoster().getMoviePoster() != null) {
                byte[] moviePosterBlob = movie.getMoviePoster().getMoviePoster().getBlob();
                viewHolder.poster.setImageBitmap(BitmapFactory.decodeByteArray(moviePosterBlob, 0, moviePosterBlob.length));
            } else if (MoviePoster.isValidUrl(movie.getPoster())) {
                Picasso.with(context).load(movie.getPoster()).into(viewHolder.poster);
            } else {
                Picasso.with(context).load(R.drawable.ic_launcher_movie).into(viewHolder.poster);
            }
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
