package com.vladnamik.developer.movieinformationviewer.components;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vladnamik.developer.movieinformationviewer.api.APIHelper;
import com.vladnamik.developer.movieinformationviewer.api.OmdbAPI;

import org.androidannotations.annotations.EApplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@EApplication
public class Application extends android.app.Application {
    private APIHelper apiHelper;

    public Application() {
        //api init
        Gson gson = new GsonBuilder()
                .setDateFormat(OmdbAPI.DATE_FORMAT_PATTERN)
                .create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(OmdbAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        apiHelper = new APIHelper(retrofit.create(OmdbAPI.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //DBFlow instantiation
//        FlowManager.init(new FlowConfig.Builder(this).build());
    }

    public APIHelper getApiHelper() {
        return apiHelper;
    }
}
