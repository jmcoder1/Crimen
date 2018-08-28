package com.example.android.crimen;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from data.police.uk.
 */
public final class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    // The tag for logging and debugging
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Query the data.police.uk url and return a list of {@link Crime} objects.
     */
    public static List<Crime> fetchCrimeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create a list of {@link Crime}s
        List<Crime> crimes = extractFeatureFromJson(jsonResponse);
        // Return the list of {@link Earthquake}s
        return crimes;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Crime} objects that has been built up from
     * parsing a JSON response.
     * parsing the given JSON response.
     */
    private static List<Crime> extractFeatureFromJson(String crimesJSONResponse) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(crimesJSONResponse)) {
            return null;
        }
        List<Crime> crimes = new ArrayList<>();
        // Try to parse the JSON response string. If there's a problem with the way the JSON

        try {
            // Create a JSONArray from the crime JSON response string.
            JSONArray crimeJSONArray = new JSONArray(crimesJSONResponse);

            // For each crime in the JSON array, create an {@link Crime} object
            for (int i = 0; i < crimeJSONArray.length(); i++) {

                // Get a single crime at position i within the list of crimes
                JSONObject currentCrime = crimeJSONArray.getJSONObject(i);

                // Extract the crime category from the current crime
                String category = currentCrime.getString("category");

                // Extract the location JSONObject from the JSONArray with location information
                // Extract the value for the key called latitude
                // Extract the value for the key called longitude
                JSONObject location = currentCrime.getJSONObject("location");
                String latitude = location.getString("latitude");
                String longitude = location.getString("longitude");

                // Extract the street location JSONObject from the location JSON Object
                // Extract the value for the key called street name
                JSONObject streetLocation = location.getJSONObject("street");
                String streetName = streetLocation.getString("name");

                // Extract the value for the key called date (month) from the current crime
                String date = currentCrime.getString("month");

                // Create a new {@link Crime} object with the category, latitude, longitude
                // streetName, and date from the JSON response.
                Crime crime = new Crime(category, latitude, longitude, streetName, date);

                // Add the new {@link Crime} to the list of crimes.
                crimes.add(crime);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the crime JSON results", e);
        }

        // Return the list of crimes
        return crimes;
    }
}