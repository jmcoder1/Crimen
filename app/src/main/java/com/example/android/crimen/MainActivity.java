package com.example.android.crimen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arlib.floatingsearchview.FloatingSearchView;

public class MainActivity extends AppCompatActivity {

    // The floating search bar that remains at the top of the application
    private FloatingSearchView mFloatingSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Make sure this is before calling super.onCreate
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFloatingSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);

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
