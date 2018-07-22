package com.vladnamik.developer.movieinformationviewer.components.fragments;

import android.app.ListFragment;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.vladnamik.developer.movieinformationviewer.R;
import com.vladnamik.developer.movieinformationviewer.components.Application;
import com.vladnamik.developer.movieinformationviewer.components.adapters.MovieListAdapter;
import com.vladnamik.developer.movieinformationviewer.database.entities.Movie;
import com.vladnamik.developer.movieinformationviewer.database.entities.SearchPage;
import com.vladnamik.developer.movieinformationviewer.main.DataLoader;
import com.vladnamik.developer.movieinformationviewer.main.DataLoaderMock;
import com.vladnamik.developer.movieinformationviewer.main.DataLoaderServiceBased;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@EFragment
public class MovieListFragment extends ListFragment {
    private static final String LOG_TAG = "MovieListFragment";
    private String query;
    private int nextPageToUploadNumber = 1;
    private int allMoviesOnQueryNumber = 0;
    private MovieListAdapter listAdapter;
    private List<Movie> movies = new ArrayList<>();
    private OnMovieSelected onMovieSelected;
    private View headerView;
    private TextView headerSearchQuery;
    private TextView headerSearchResultsNumber;
    private SwipeRefreshLayout swipeLayout;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        changeListViewStyle();

        swipeLayout = getActivity().findViewById(R.id.swipe_container);
        if (swipeLayout != null)
        {
            swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshQuery(query);
                }
            });
        }

        onMovieSelected = (OnMovieSelected) getActivity();

        query = ((GetQueryParams) getActivity()).getQuery();

        listAdapter = new MovieListAdapter(movies, getActivity());
        setListAdapter(listAdapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) { // avoiding header clicking
                    Log.d(LOG_TAG, "start onListItemClick()");
                    view.setSelected(true);
                    onMovieSelected.onMovieSelected(movies.get(i - 1).getImdbID()); // i - 1 because of header
                }
            }
        });

        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView lw, final int firstVisibleItem,
                                 final int visibleItemCount, final int totalItemCount) {
                Log.d(LOG_TAG, "firstVisibleItem = " + firstVisibleItem
                        + "; visibleItemCount = " + visibleItemCount + "; allMoviesOnQueryNumber = "
                        + allMoviesOnQueryNumber + "; moviesNumber = " + movies.size());
                // -1 because of header
                if ((firstVisibleItem + visibleItemCount - 1) == movies.size()
                        && (firstVisibleItem + visibleItemCount - 1) < allMoviesOnQueryNumber) {
                    tryToAddNewDataInList();
                }

                int topRowVerticalPosition = (lw == null || lw.getChildCount() == 0) ? 0 : lw.getChildAt(0).getTop();
                swipeLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

        headerView = getActivity().getLayoutInflater().inflate(R.layout.header_for_movie_listview, null);
        headerView.setClickable(false);
        headerSearchQuery = headerView.findViewById(R.id.header_for_movie_listview_search_query);
        headerSearchResultsNumber = headerView.findViewById(R.id.header_for_movie_listview_search_results_number);
        headerSearchQuery.setText(query);
        headerSearchResultsNumber.setText(String.format(Locale.getDefault(), "%d", allMoviesOnQueryNumber));
        getListView().addHeaderView(headerView);

        tryToAddNewDataInList();
    }

    protected void changeListViewStyle()
    {
        // divider
        int[] colors = {0, getResources().getColor(R.color.colorAccent), 0};
        getListView().setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        getListView().setDividerHeight(1);
    }

    @Background(serial = "list_data")
    void tryToAddNewDataInList() {
        Log.d(LOG_TAG, "start trying to add new data in list");
        try {
            if (getActivity() != null) {
                SearchPage searchPage = new DataLoader((Application) getActivity().getApplication())
                        .loadPage(query, nextPageToUploadNumber);
                if (searchPage != null) {
                    Log.d(LOG_TAG, "start process response");
                    allMoviesOnQueryNumber = searchPage.getTotalResults();
                    nextPageToUploadNumber++;
                    addNewDataToAdapter(searchPage.getMovies());
                }
            }

        } catch (IOException e) {
            Log.d(LOG_TAG, "data loading failed");
            e.printStackTrace();
        }
        afterNewDataAdded();
        Log.d(LOG_TAG, "end trying to add new data in list");
    }

    @UiThread
    void afterNewDataAdded()
    {
        swipeLayout.setRefreshing(false);
    }

    @UiThread
    void addNewDataToAdapter(List<Movie> movies) {
        Log.d(LOG_TAG, "start adding new data to adapter");
        headerSearchResultsNumber.setText(String.format(Locale.getDefault(), "%d", allMoviesOnQueryNumber));
        Map<String, String> bufMap;
        for (Movie movie : movies) {
            Log.d(LOG_TAG, movie.toString());
        }
        this.movies.addAll(movies);
        ((MovieListAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @UiThread
    public void refreshQuery(String newQuery) {
        query = newQuery;
        nextPageToUploadNumber = 1;
        allMoviesOnQueryNumber = 0;
        headerSearchQuery.setText(query);
        headerSearchResultsNumber.setText(String.format(Locale.getDefault(), "%d", allMoviesOnQueryNumber));
        movies.clear();
        ((MovieListAdapter)getListAdapter()).notifyDataSetChanged();
        tryToAddNewDataInList();
    }

    public interface GetQueryParams {
        String getQuery();
    }

    public interface OnMovieSelected {
        void onMovieSelected(String movieImdbId);
    }

}
