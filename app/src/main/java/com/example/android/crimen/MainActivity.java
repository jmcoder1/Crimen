package com.example.android.crimen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlaceDetectionClient;
import android.support.v4.app.FragmentActivity;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

public class MainActivity extends AppCompatActivity {

    // The floating search bar that remains at the top of the application
    private FloatingSearchView mFloatingSearchView;

    // The Google Place API request code
    private static final int  PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Make sure this is before calling super.onCreate
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handles what happens with the floating searchview
        mFloatingSearchView = findViewById(R.id.floating_search_view);

        mFloatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(MainActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }

                //get suggestions based on newQuery

                //pass them on to the search view
                //mFloatingSearchView.swapSuggestions(newSuggestions);
            }
        });
    }
}
