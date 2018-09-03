package com.example.android.crimen;

import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CrimeActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Crime>> {

    // ConnectivityManager that queries the network and handles if there is connection or not
    private ConnectivityManager connectivityManager;

    // Constant value for the Crime Loader ID.
    // You can choose any number. Only comes into play
    // when dealing wth multiple loaders.
    private static final int CRIME_LOADER_ID = 1;

    // Tag for logging and debugging messages
    public static final String LOG_TAG = CrimeActivity.class.getName();

    // The string for the URL request
    private String mCrimeDataRequest;

    // The Crime array adapter
    private CrimeAdapter mAdapter;

    // The empty crime data TextView
    private TextView mEmptyDataTextView;

    // The ProgressBar view that appears when handling an event takes to long
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crimes_activity);

        // Gets bundle from the intent containing data about the HTTP request
        Intent i = getIntent();
        Bundle b = i.getBundleExtra("urlDataBundle");
        mCrimeDataRequest = b.getString("url");

        // Create a connectivityManager that handles connection to the internet
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // Find a reference to the progress bar view
        mProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);

        // Create a new {@link ArrayAdapter} of crimes
        mAdapter = new CrimeAdapter(this, new ArrayList<Crime>());

        // Find a reference to the {@link ListView} in the layout
        ListView crimesListView = (ListView) findViewById(R.id.list);

        // The TextView that is displayed when there is not crime data to find
        mEmptyDataTextView = (TextView) findViewById(R.id.empty_text_view);

        // Sets the TextView to appear if there is nothing to display on the crimesListView
        crimesListView.setEmptyView(mEmptyDataTextView);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        crimesListView.setAdapter(mAdapter);


        crimesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // TODO: Stuff to do when an item in the list is clicked
            }
        });
        if(isConnected) {
            LoaderManager loaderManager = getLoaderManager();

            // Initialise the loader with the id, pass in null as the bundle,
            // Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(CRIME_LOADER_ID, null, this);
        } else {
            mEmptyDataTextView.setText(R.string.no_connectivity);
            mProgressBar.setVisibility(View.INVISIBLE);
        }

    }

    /*
     * This function handles creating a loader when the LoaderManager has determined that
     * a loader with the specified ID does not exist.
     *
     * @param i
     * @param bundle
     *
     * @return CrimeLoader that does not already exist with the specified ID.
     */
    @Override
    public Loader<List<Crime>> onCreateLoader(int i, Bundle bundle) {
        return new CrimeLoader(this, mCrimeDataRequest);
    }

    /*
     * This function handles the activity that happens on the main thread (UI thread) when the
     * activity on the background thread has finished. It handles updating the UI and the information
     * that is passed onto the UI.
     *
     * @param loader The CrimeLoader that handles all the data.
     * @param earthquakeData The parsed JSON data about the earthquakes.
     *
     */
    @Override
    public void onLoadFinished(Loader<List<Crime>> loader, List<Crime> earthquakeData) {
        // Sets the empty state TextView to have display the relevant no earthquake string
        mEmptyDataTextView.setText(R.string.no_crime_data);

        // Sets the progressBar to invisible once the CrimeLoader has finished querying
        mProgressBar.setVisibility(View.INVISIBLE);

        // Clear the adapter of previous crime data
        mAdapter.clear();

        // If there is a valid list of {@link Crime}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakeData != null && !earthquakeData.isEmpty()) {
            mAdapter.addAll(earthquakeData);
        }
    }
    /*
     * This function handles the activity involved when the LoaderManager determines that the data
     * it handles is no longer valid.
     *
     * @param loader The CrimeLoader that handles all the data.
     */
    @Override
    public void onLoaderReset(Loader<List<Crime>> loader) {
        mAdapter.clear();
    }

}
