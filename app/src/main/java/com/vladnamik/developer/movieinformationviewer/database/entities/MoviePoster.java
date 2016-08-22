package com.vladnamik.developer.movieinformationviewer.database.entities;

import android.content.Context;
import android.graphics.Bitmap;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.squareup.picasso.Picasso;
import com.vladnamik.developer.movieinformationviewer.database.MovieInfoDB;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


@SuppressWarnings("unused")
@Table(database = MovieInfoDB.class)
public class MoviePoster extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    long _id;

    @Column
    private Blob moviePoster;

    public MoviePoster() {

    }

    public MoviePoster(Blob moviePoster) {
        this.moviePoster = moviePoster;
    }

    public Blob getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(Blob moviePoster) {
        this.moviePoster = moviePoster;
    }

    public void downloadPoster(String url) throws IOException {
        if (moviePoster != null) {
            return;
        }
        if (url == null || url.equals("") || url.equals("N/A")) {
            return;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = new URL(url).openStream();
            byte[] byteChunk = new byte[4096];
            int n;

            while ((n = is.read(byteChunk)) > 0) {
                baos.write(byteChunk, 0, n);
            }
            moviePoster = new Blob(baos.toByteArray());
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public void downloadPoster(Context context, String url) throws IOException {
        if (url == null || url.equals("") || url.equals("N/A")) {
            return;
        }
        if (moviePoster != null) {
            return;
        }

        Bitmap bitmap = Picasso.with(context).load(url).get();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        moviePoster = new Blob(stream.toByteArray());
    }
}
