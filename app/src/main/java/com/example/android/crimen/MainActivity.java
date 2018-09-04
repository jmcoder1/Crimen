package com.example.android.crimen;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    // The Google Place API request code
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    // Country code ot restrict the autocomplete fragment
    private static final String COUNTRY_BOUND = "GB";

    // The lat/lng values the Google Map camera should open on
    private static final LatLng OPENING_LOCATION_LATLNG = new LatLng(51.507351, -0.127758);

    // The Log Tag for debugging and writing log messages
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // The error to handle if the user does not have the correct version of Google Play services
    private static final int EROOR_DIALOG_REQUEST = 9001;

    // Shorthand for the android manifest location permissions
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    // Android Manifest location permission request code
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    // The number of decimal places to the latitude and longitude values to
    private static final int ROUND_LATLNG_TO = 6;

    // The placeholder date for the specific month of the crimes
    // TODO: Note this has to be changed later to be dynamic
    private static final String DATE = "2018-02";

    // The string for the URL request
    private String mRequestUrl;

    // The floating search bar that remains at the top
    private PlaceAutocompleteFragment mAutocompleteFragment;

    //
    private GoogleMap mMap;

    // This variables tracks if location permissions have been granted
    private boolean mLocationPermissionGranted = false;

    private FusedLocationProviderClient mFusedLocatonProviderClient;

    // The default initial zoom of the map
    private static final float DEFAULT_ZOOM = 15f;

    // The 'my location' gps button that centres the user to their device location
    private FloatingActionButton mMyLocationFab;

    /**
     * Manipulates the map once available. This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(LOG_TAG, "onMapReady: called");

        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap.setMyLocationEnabled(true);
            mMap.setIndoorEnabled(false);

            // Hide the default button that centres map to the current user location
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            // Sets the min zoom preference - landmass/continent and max zoom preference - buildings
            mMap.setMinZoomPreference(5f);
            mMap.setMaxZoomPreference(20f);

            init();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Code for the splash screen
        // Must be set before super.onCreate()
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMyLocationFab = (FloatingActionButton) findViewById(R.id.ic_my_location);

        setFloatingActionButtonColors(mMyLocationFab,
                getResources().getColor(R.color.lightWidgetBakground),
                getResources().getColor(R.color.lightWidgetBakground));

        getLocationPermission();
        //if(isServicesValid()) {}

    }

    /*
    *
     */
    private void setFloatingActionButtonColors(FloatingActionButton fab, int primaryColor, int rippleColor) {
        int[][] states = {
                {android.R.attr.state_enabled},
                {android.R.attr.state_pressed},
        };

        int[] colors = {
                primaryColor,
                rippleColor,
        };

        ColorStateList colorStateList = new ColorStateList(states, colors);
        fab.setBackgroundTintList(colorStateList);
    }

    /*
     * This method gets the location permissions from the user to use Google Play services.
     */
    private void getLocationPermission() {
        Log.d(LOG_TAG, "Getting location permissions");

        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /*
     * This method initialises the map and the map fragment.
     */
    private void initMap() {
        Log.d(LOG_TAG, "Initialising Google map...");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);
    }

    /*
    * This method gets the device location from the user.
     */
    private void getDeviceLocation() {
        Log.d(LOG_TAG, "getDeviceLocation: getting the device's current location");

        mFusedLocatonProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocatonProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(LOG_TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                        } else {
                            Log.d(LOG_TAG, "onComplete: current location is location");
                            Toast.makeText(MainActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.d(LOG_TAG, "getDeviceLocation: SecurityException " + e.getMessage());
        }
    }

    /*
    *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       Log.d(LOG_TAG, "onRequestPermissionsResult: called");
        mLocationPermissionGranted = false;

        switch(requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0) {
                        for(int i = 0; i < grantResults.length; i++) {
                            if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                mLocationPermissionGranted = false;
                                Log.d(LOG_TAG, "onRequestPermissionsResult: false");
                                return;
                            }
                        }
                    Log.d(LOG_TAG, "onRequestPermissionsResult: granted");
                    mLocationPermissionGranted = true;
                    initMap();
                }
        }
    }

    /*
     * This method moves the Google Map camera based on LatLng and the camera zoom.
     *
     * @param latLng The location to move the camera to.
     * @param zoom The zoom the camera should focus up to.
     */
    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(LOG_TAG, "Moving camera to latitude: " + latLng.latitude + "and  longitude: "
                + latLng.longitude + " with camera zoom: " + zoom);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    /*
     * This method checks if the version of Google Play services the user has installed is valid.
     * The user must have the most up to date version Google Play services for the map to be
     * loaded.
     */
    private boolean isServicesValid() {
        Log.d(LOG_TAG, "Google Play services: valid");

        int available = GoogleApiAvailability.getInstance().
                isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS) {
            // Everything is fine and the user can make requests
            Log.d(LOG_TAG, "Google Play services: working");
            return true;

        } else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // A versioning issue we can resolve
            Dialog dialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(MainActivity.this, available, EROOR_DIALOG_REQUEST);
            dialog.show();

        } else {
            // Error cannot be resolved
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
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

    /*
    * This method initialises widgets and fragment on the map view.
    *
    * It begins by initalising the autcomplete search bar fragment.
    * Bounds: the searchable results from the GooglePlaces API is limited to the Great Britain.
    *
     */
    private void init() {

        // Get the floating search bar view
        mAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // Sets the filter to restrict to the bound of the London (UK) region
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry(COUNTRY_BOUND)
                .build();

        // Filter the autocomplete fragment to the specific bounds set
        mAutocompleteFragment.setFilter(autocompleteFilter);

        // Activity for when a place is from the search bar
        mAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng placeLatLng = place.getLatLng();
                mRequestUrl = getUrlFromPlace(place, DATE);

                // Moves the camera to the place selected
                moveCamera(placeLatLng, DEFAULT_ZOOM);

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

        mMyLocationFab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(LOG_TAG, "mGpsWidget: clicked GPS widget");
        }
    });
    }


}