package com.example.android.crimen;

import android.app.Activity;
import java.text.DecimalFormat;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CrimeAdapter extends ArrayAdapter<Crime> {

    // The TAG used for debugging and logging activity to the logcat
    private static final String LOG_TAG = CrimeAdapter.class.getSimpleName();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param crimes A List of Crime objects to display in a list
     */
    public CrimeAdapter(Activity context, ArrayList<Crime> crimes) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for five TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, crimes);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Crime} object located at this position in the list
        Crime currentEarthquake = getItem(position);

        // Find the ImageView in the list_item.xml layout with the crime icon
        ImageView crimeIconImageView = (ImageView) listItemView.findViewById(R.id.crimeIcon);

        // Find the TextView in the list_item.xml layout with the ID for the 'crimeStreetName'
        TextView crimeStreetNameTextView = (TextView) listItemView.findViewById(R.id.crimeStreetName);
        // Sets the crime street name of the cimr to the TextView
        String crimeStreetName = currentEarthquake.getStreetName();
        crimeStreetNameTextView.setText(crimeStreetName);

        // Find the TextView in the list_item.xml layout with the ID for the 'crime category'
        TextView crimeCategoryTextView = (TextView) listItemView.findViewById(R.id.crimeCategory);
        // Sets the category of the crime to the TextView
        String crimeCategory = currentEarthquake.getCategory();
        crimeCategoryTextView.setText(crimeCategory);

        // Find the TextView in the list_item.xml layout with the ID for the 'date'
        TextView crimeDateTextView = (TextView) listItemView.findViewById(R.id.crimeDate);
        // Sets the date of the crime to the TextView
        String crimeDate = currentEarthquake.getDate();
        crimeDateTextView.setText(crimeDate);

        // Find the TextView in the list_item.xml layout with the ID for the 'latitude'
        TextView crimeLatitudeTextView = (TextView) listItemView.findViewById(R.id.crimeLatitude);
        // Sets latitude of the crime to the TextView
        Long crimeLatitude = currentEarthquake.getLatitude();
        crimeLatitudeTextView.setText(String.format("%d", crimeLatitude));

        // Find the TextView in the list_item.xml iwth the ID for the 'longitude'
        TextView crimeLongitudeTextView = (TextView) listItemView.findViewById(R.id.crimeLongitude);
        // Sets the longitude of the crime to the TextView
        Long crimeLongitude = currentEarthquake.getLongitude();
        crimeLongitudeTextView.setText(String.format("%d", crimeLongitude));

        // Return the whole list item layout (containing a TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}