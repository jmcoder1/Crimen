package com.example.android.crimen;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import android.content.Context;

public class CrimeLoader extends AsyncTaskLoader<List<Crime>> {

    // Query URL for the JSON data
    private String mUrl;

    // Tag for log messages
    public static final String LOG_TAG = MainActivity.class.getName();

    /*
     * This constructor handles the creation of an CrimeLoader object.
     *
     *
     * @param context The context the class is initialised in.
     * @param url The website query data for the JSON data.
     */
    public CrimeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }
    /**
     * This method runs on the main UI thread after the background work has been
     * completed. This method receives as input, the return value from the doInBackground()
     * method. First we clear out the adapter, to get rid of crime data from a previous
     * query to police.data.uk. Then we update the adapter with the new list of crimes,
     * which will trigger the ListView to re-populate its list items.
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This method runs on a background thread and performs the network request.
     * We should not update the UI from a background thread, so we return a list of
     * {@link Crime}s as the result.
     *
     * @return List<Crime> The List containing the data about the crime.
     */
    @Override
    public List<Crime> loadInBackground() {
        // Don't perform the request if no URL is passed
        if (mUrl == null) return null;

        List<Crime> crimeData = QueryUtils.fetchCrimeData(mUrl);
        return crimeData;
    }


}
