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

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    // The Google Place API request code
    private static final int  PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    // Country code ot restrict the autocomplete fragment
    private static final String COUNTRY_BOUND = "GB";

    // The lat/lng values the Google Map camera should open on
    private static final LatLng OPENING_LOCATION_LATLNG = new LatLng(51.507351, -0.127758);

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

    //
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Code for the splash screen
        // Must be set before super.onCreate()
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get the floating search bar view
        mAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // sets the bounds of the autocomplete fragment to a specific region (UK)
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry(COUNTRY_BOUND)
                .build();

        mAutocompleteFragment.setFilter(autocompleteFilter);

        // Activity for when a place is from the search bar
        mAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng placeLatLng = place.getLatLng();
                mRequestUrl = getUrlFromPlace(place, DATE);

                // Gets information about crime at the chosen Place
                // Passes the location specific information on
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng london = OPENING_LOCATION_LATLNG;
        mMap.addMarker(new MarkerOptions().position(london).title("Marker in London"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(london));
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
        double latitude = roundDecimalNumber(placeLatLng.latitude, ROUND_LATLNG_TO);
        double longitude = roundDecimalNumber(placeLatLng.longitude, ROUND_LATLNG_TO);
        Log.e(LOG_TAG, "Place longitude: " + longitude);
        Log.e(LOG_TAG, "Place latitude: " + latitude);

        String url = getString(R.string.crime_at_location_url, DATE, latitude, longitude);
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