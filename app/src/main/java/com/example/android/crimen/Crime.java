package com.example.android.crimen;

public class Crime {

    // The type of crime that was recorded
    private String mCategory;

    // The latitude of the crime that was recorded
    private Long mLatitude;

    // The longitude of the crime that was recorded
    private Long mLongitude;

    // The name of the strret that the crime was approximated to be recorded
    private String mStreetName;

    // The month and year (YYYY-MM) the crime was recorded
    private String mDate;

    // The outcome of the crime
    private String mCrimeOutcome;

    // The date of the outcome of the crime
    private String mCrimeOutcomeDate;


    /*
    * This constructor takes 5 parameters in order to create an instance of the Crime class.
    *
    * @param category The type or category of the crime.
    * @param latitude The approximate latitude of the crime.
    * @param longitude The approximate longitude of the crime.
    * @param streetName The name of the street the crime was approximately recorded at.
    * @param date The date of the crime (YYYY-MM).
     */
    public Crime(String category, Long latitude, Long longitude, String streetName, String date) {
        mCategory = category;
        mLatitude = latitude;
        mLongitude = longitude;
        mStreetName = streetName;
        mDate = date;
    }

    /*
    * This method gets the type of crime that was recorded.
    *
    * @return The category of the crime that was recorded.
     */
    public String getCategory() {
        return mCategory;
    }

    /*
    * This method sets the type of the crime that was recorded.
    *
    * @param Thee category of the crime that was recorded.
     */
    public void setCategory(String category) {
        mCategory = category;
    }

    /*
    * This method gets the approximate latitude of the crime that was recorded.
    *
    * @return The latitude of the crime.
     */
    public Long getLatitude() {
        return mLatitude;
    }

    /*
    * This method sets the approximate latitude of the crime that was recorded.
    *
    * @param The latitude of the crime.
     */
    public void setLatitude(Long latitude) {
        mLatitude = latitude;
    }

    /*
    * This method gets the approximate longitude of the crime that was recorded.
    *
    * @return The longitude of the crime.
     */
    public Long getLongitude() {
        return mLongitude;
    }

    /*
    * This method sets the approximate longitude of the crime that was recorded.
    *
    * @param The longitude of the crime.
     */
    public void setLongitude(Long longitude) {
        mLongitude = longitude;
    }

    /*
    * This method gets the approximate street name of the crime.
    *
    * @return The street name of the crime.
     */
    public String getStreetName() {
        return mStreetName;
    }

    /*
    * This method sets the approximate street name of the crime.
    *
    * @param The street name of the crime.
     */
    public void setStreetName(String streetName) {
        mStreetName = streetName;
    }

    /*
    * This method gets the month the crime was recorded.
    *
    * @return The month of the crime.
     */
    public String getDate() {
        return mDate;
    }

    /*
    * This method sets the month the crime was recorded.
    *
    * @param The month of the crime.
     */
    public void setDate(String date) {
        mDate = date;
    }

    /*
    * This method gets the the outcome (status) of the crime.
    *
    * @return The outcome of the crime.
     */
    public String getCrimeOutcome() {
        return mCrimeOutcome;
    }

    /*
    * This method set the outcome (status) of the crime.
    *
    * @param The outcome of the crime.
     */
    public void setCrimeOutcome(String crimeOutcome) {
        mCrimeOutcome = crimeOutcome;
    }

    /*
    * This method gets the outcome date of the crime.
    *
    * @return The outcome date of the crime.
     */
    public String getCrimeOutcomeDate() {
        return mCrimeOutcomeDate;
    }

    /*
    * This method sets the outcome date of the crime.
    *
    * @param The outcome date of the crime.
     */
    public void setCrimeOutcomeDate(String outcomeCrimeDate) {
        mCrimeOutcomeDate = outcomeCrimeDate;
    }
}
