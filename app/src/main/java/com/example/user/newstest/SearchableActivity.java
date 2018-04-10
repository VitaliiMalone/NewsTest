package com.example.user.newstest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SearchableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        String query = getIntent().getStringExtra("query");
        Log.v("SearchableActivity", "Received query is " + query);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new SearchableFragment();

        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        fragment.setArguments(bundle);

        fm.beginTransaction()
                .add(R.id.searchable_fragment_container, fragment)
                .commit();
    }
}
