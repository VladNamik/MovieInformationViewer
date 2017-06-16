package com.vladnamik.developer.movieinformationviewer.components.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.vladnamik.developer.movieinformationviewer.R;
import com.vladnamik.developer.movieinformationviewer.database.entities.Movie;
import com.vladnamik.developer.movieinformationviewer.database.entities.SearchPage;
import com.vladnamik.developer.movieinformationviewer.main.DataLoaderMock;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@EFragment
public class MovieListFragment extends ListFragment {
    private static final String MOVIE_LIST_FRAGMENT_LOG_TAG = "MovieListFragment";
    private static final String NAME_KEY = "name";
    private static final String TYPE_YEAR_KEY = "typeYear";
    private String query;
    private int nextPageToUploadNumber = 1;
    private int allMoviesOnQueryNumber = 0;
    private List<Map<String, String>> moviesData = new ArrayList<>();
    private List<Movie> movies = new ArrayList<>();
    private OnMovieSelected onMovieSelected;
    private View headerView;
    private TextView headerSearchQuery;
    private TextView headerSearchResultsNumber;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        onMovieSelected = (OnMovieSelected) getActivity();

        query = ((GetQueryParams) getActivity()).getQuery();

        String[] from = {NAME_KEY, TYPE_YEAR_KEY};
        int[] to = {R.id.movie_list_view_item_name, R.id.movie_list_view_item_type_year};
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), moviesData,
                R.layout.movie_list_view_item, from, to);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(MOVIE_LIST_FRAGMENT_LOG_TAG, "start onListItemClick()");
                view.setSelected(true);
                onMovieSelected.onMovieSelected(movies.get(i - 1).getImdbID());//i - 1 because of header
            }
        });

        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView lw, final int firstVisibleItem,
                                 final int visibleItemCount, final int totalItemCount) {
                Log.d(MOVIE_LIST_FRAGMENT_LOG_TAG, "firstVisibleItem = " + firstVisibleItem
                        + "; visibleItemCount = " + visibleItemCount + "; allMoviesOnQueryNumber = "
                        + allMoviesOnQueryNumber + "; moviesNumber = " + movies.size());
                // -1 because of header
                if ((firstVisibleItem + visibleItemCount - 1) == movies.size()
                        && (firstVisibleItem + visibleItemCount - 1) < allMoviesOnQueryNumber) {
                    tryToAddNewDataInList();
                }
            }
        });

        headerView = getActivity().getLayoutInflater().inflate(R.layout.header_for_movie_listview, null);
        headerSearchQuery = (TextView) headerView.findViewById(R.id.header_for_movie_listview_search_query);
        headerSearchResultsNumber = (TextView) headerView.findViewById(R.id.header_for_movie_listview_search_results_number);
        headerSearchQuery.setText(query);
        headerSearchResultsNumber.setText(String.format(Locale.getDefault(), "%d", allMoviesOnQueryNumber));
        getListView().addHeaderView(headerView);

        tryToAddNewDataInList();
    }

    @Background(serial = "list_data")
    void tryToAddNewDataInList() {
        Log.d(MOVIE_LIST_FRAGMENT_LOG_TAG, "start trying to add new data in list");
        try {
            SearchPage searchPage = new DataLoaderMock()
                    .loadPage(query, nextPageToUploadNumber);
            if (searchPage != null) {
                Log.d(MOVIE_LIST_FRAGMENT_LOG_TAG, "start process response");
                allMoviesOnQueryNumber = searchPage.getTotalResults();
                nextPageToUploadNumber++;
                addNewDataToAdapter(searchPage.getMovies());
            }

        } catch (IOException e) {
            Log.d(MOVIE_LIST_FRAGMENT_LOG_TAG, "data loading failed");
            e.printStackTrace();
        }
        Log.d(MOVIE_LIST_FRAGMENT_LOG_TAG, "end trying to add new data in list");
    }

    @UiThread
    void addNewDataToAdapter(List<Movie> movies) {
        Log.d(MOVIE_LIST_FRAGMENT_LOG_TAG, "start adding new data to adapter");
        headerSearchResultsNumber.setText(String.format(Locale.getDefault(), "%d", allMoviesOnQueryNumber));
        Map<String, String> bufMap;
        for (Movie movie : movies) {
            Log.d(MOVIE_LIST_FRAGMENT_LOG_TAG, movie.toString());
            bufMap = new HashMap<>();
            bufMap.put(NAME_KEY, movie.getTitle());
            bufMap.put(TYPE_YEAR_KEY, movie.getType().toString() + ", " + movie.getYear());
            this.moviesData.add(bufMap);
        }
        this.movies.addAll(movies);
        ((SimpleAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @UiThread
    public void refreshQuery(String newQuery) {
        query = newQuery;
        nextPageToUploadNumber = 1;
        allMoviesOnQueryNumber = 0;
        headerSearchQuery.setText(query);
        headerSearchResultsNumber.setText(String.format(Locale.getDefault(), "%d", allMoviesOnQueryNumber));
        moviesData.clear();
        movies.clear();
        ((SimpleAdapter) getListAdapter()).notifyDataSetChanged();
        tryToAddNewDataInList();
    }

    public interface GetQueryParams {
        String getQuery();
    }

    public interface OnMovieSelected {
        void onMovieSelected(String movieImdbId);
    }

}
