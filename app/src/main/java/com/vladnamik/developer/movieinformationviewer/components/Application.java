package com.vladnamik.developer.movieinformationviewer.components;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.vladnamik.developer.movieinformationviewer.api.APIHelper;
import com.vladnamik.developer.movieinformationviewer.api.OmdbAPI;

import org.androidannotations.annotations.EApplication;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@EApplication
public class Application extends android.app.Application {
    private APIHelper apiHelper;

    public Application() {
        //api init
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new DBFlowExclusionStrategy())
//                .setDateFormat(OmdbAPI.DATE_FORMAT_PATTERN)
                .registerTypeAdapter(Float.class, new FloatNewDeserializer())
                .registerTypeAdapter(Date.class, new DateNewDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(OmdbAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        apiHelper = new APIHelper(retrofit.create(OmdbAPI.class));

    }

    @Override
    public void onCreate() {
        super.onCreate();

        //DBFlow instantiation
        FlowManager.init(new FlowConfig.Builder(this).build());
    }

    public APIHelper getApiHelper() {
        return apiHelper;
    }

    static class DBFlowExclusionStrategy implements ExclusionStrategy {

        // Otherwise, Gson will go through base classes of DBFlow models
        // and hang forever.
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredClass().equals(ModelAdapter.class) || f.getName().equals("_id");
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

    static class FloatNewDeserializer implements JsonDeserializer<Float> {
        @Override
        public Float deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == null || json.getAsString().equals("") || json.getAsString().equals("N/A")) {
                return null;
            } else {
                return Float.parseFloat(json.getAsString());
            }
        }
    }

    static class DateNewDeserializer implements JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == null || json.getAsString().equals("") || json.getAsString().equals("N/A")) {
                return null;
            } else {
                DateFormat dateFormat = new SimpleDateFormat(OmdbAPI.DATE_FORMAT_PATTERN, Locale.getDefault());
                try {
                    return dateFormat.parse(json.getAsString());
                } catch (ParseException e) {
                    return null;
                }
            }
        }
    }

}
