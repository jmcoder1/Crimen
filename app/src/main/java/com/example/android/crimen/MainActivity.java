package com.example.android.crimen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlaceDetectionClient;
import android.support.v4.app.FragmentActivity;
import android.widget.RelativeLayout;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MainActivity extends AppCompatActivity {

    //
    private static final LatLngBounds BOUNDS_LONDON = new LatLngBounds(new LatLng(57.149651, -2.099075), new LatLng(51.621441, -3.943646));

    // The floating search bar that remains at the top of the application
    private FloatingSearchView mFloatingSearchView;

    // The Google Place API request code
    private static final int  PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    // The drawer that opens when the search bar is pressed and displays options
    private RelativeLayout autocompleteRelativeLayout;

    LatLngBounds.Builder latlngBuilder = new LatLngBounds.Builder();

    private GoogleApiClient mGoogleApiClient;


    // The adapter for the autocompleted places
    private PlaceAutoCompleteInterfacemAdapter mAutocompletAdapter = new PlaceAutoCompleteInterface(this, R.id.floating_search_view, mGoogleApiClient, BOUNDS_LONDON, TYPE_FILTER_NONE );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Make sure this is before calling super.onCreate
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the RelativeLayout prediction drawer
        autocompleteRelativeLayout = (RelativeLayout) findViewById(R.id.predictedRow);

        // Handles what happens with the floating searchview
        mFloatingSearchView = findViewById(R.id.floating_search_view);

        mFloatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                //get suggestions based on newQuery

                //pass them on to the search view
                //mFloatingSearchView.swapSuggestions(newSuggestions);
            }
        });
    }
}
