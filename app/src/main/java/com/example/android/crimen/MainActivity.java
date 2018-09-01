package com.example.android.crimen;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    // The Google Place API request code
    private static final int  PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    // Country code ot restrict the autocomplete fragment
    private static final String COUNTRY_BOUND = "GB";

    // The Log Tag for debugging and writing log messages
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // The number of decimal places to the latitude and longitude values to
    private static final int ROUND_LATLNG_TO = 6;

    // The placeholder date for the specific month of the crimes
    // TODO: Note this has to be chnaged later to be dynamic
    private static final String DATE = "2018-02";

    // The string for the URL request
    private String mRequestUrl;

    // The floating search bar that remains at the top
    private PlaceAutocompleteFragment mAutocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Code for the splash screen
        // Must be set before super.onCreate()
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // sets the bounds of the autocomplete fragment to a specific region (UK)
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry(COUNTRY_BOUND)
                .build();

        mAutocompleteFragment.setFilter(autocompleteFilter);

        mAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng placeLatLng = place.getLatLng();
                mRequestUrl = getUrlFromPlace(place, DATE);
                // TODO: Get info about the selected place.

                // Brings up information about the specific crime at the chosen place.
                Intent crimesIntent = new Intent(MainActivity.this, CrimeActivity.class);
                Bundle b = new Bundle();
                b.putString("url", mRequestUrl);
                crimesIntent.putExtra("urlDataBundle", b);
                startActivity(crimesIntent);

                Log.i(LOG_TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(LOG_TAG, "An error occurred: " + status);
            }
        });

    }

    /*
    * This method gets the URL for the HTTP request respective to the place selected by the user.
    *
    * Note: this method only returns data for crime_at_location urls
    *
    * @param place The place the user has selected
    * @param month The month the data is being observed from.
    * @return The url for the crime HTTP request.
     */
    private String getUrlFromPlace(Place place, String month) {
        LatLng placeLatLng = place.getLatLng();
        double latitude = placeLatLng.latitude;
        double longitude = placeLatLng.longitude;
        Log.e(LOG_TAG, "Pressed Place Longitude: " + longitude);
        Log.e(LOG_TAG, "Pressed Place Latitude: " + latitude);

        //TODO Change from string concatenation to xliff tags
        String url = "https://data.police.uk/api/crimes-at-location?date=" + DATE  +"&lat=" + roundDecimalNumber(latitude, ROUND_LATLNG_TO) + "&lng=" + roundDecimalNumber(longitude,ROUND_LATLNG_TO);
        Log.e(LOG_TAG, "Request url: " + url);

        return url;
    }

    /*
    * This method rounds a decimal number to a certain decimal number.
    *
    * @param num The decimal number to round.
    * @param nDecimalPoints The number of decimal points to round to.
     */
    private double roundDecimalNumber(double num, int nDecimalPoints) {

        StringBuilder decimalStringBuilder = new StringBuilder("0.");
        for(int i = 0; i < nDecimalPoints; i++) {
            decimalStringBuilder.append("0");
        }

        DecimalFormat df = new DecimalFormat(decimalStringBuilder.toString());

        return Double.parseDouble(df.format(num));

    }
}