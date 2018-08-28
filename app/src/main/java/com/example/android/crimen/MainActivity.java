package com.example.android.crimen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class MainActivity extends AppCompatActivity {

    // The floating search bar that remains at the top
    private PlaceAutocompleteFragment autocompleteFragment;

    // The Google Place API request code
    private static final int  PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    // The Log Tag for debugging and writing log messages
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Code for the splash screen
        // Must be set before super.onCreate()
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                Intent crimesIntent = new Intent(MainActivity.this, CrimeActivity.class);
                startActivity(crimesIntent);

                // TODO: Get info about the selected place.
                Log.i(LOG_TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(LOG_TAG, "An error occurred: " + status);
            }
        });

    }
}