package com.vladnamik.developer.movieinformationviewer.components.activities;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.vladnamik.developer.movieinformationviewer.R;
import com.vladnamik.developer.movieinformationviewer.api.APIHelper;
import com.vladnamik.developer.movieinformationviewer.components.Application;
import com.vladnamik.developer.movieinformationviewer.database.DBService;
import com.vladnamik.developer.movieinformationviewer.database.entities.SearchPage;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;



@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private static final String MAIN_ACTIVITY_LOG_TAG = "MainActivity";
    @App
    Application app;

    @ViewById(R.id.main_activity_sample_text_view)
    TextView sampleTextView;

    @AfterViews
    void testAPIChanges() {
        saveToDB();
    }

    void saveToDB() {
        app.getApiHelper().getPageAsync("Superman", 1, new APIHelper.Callback<SearchPage>() {
            @Override
            public void onResponse(SearchPage data) {
                Log.d(MAIN_ACTIVITY_LOG_TAG, "start getting all data from DB");
                getAllFromDB();
            }

            @Override
            public void onFailure(Throwable t) {
                sampleTextView.setText(t.getLocalizedMessage());
            }
        });
    }

    @Background
    void getAllFromDB() {

        String allDBInfo = new DBService().getAllDBInfo(false);
        showText(allDBInfo);
    }

    @UiThread
    void showText(String text) {
        sampleTextView.setText(text);
        Log.d(MAIN_ACTIVITY_LOG_TAG, text);
    }
}
