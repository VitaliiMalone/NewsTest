package com.example.user.newstest.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.newstest.R;

public class SearchableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(
                    SearchableFragment.ARG_SEARCH_QUERY,
                    getIntent().getStringExtra(SearchableFragment.ARG_SEARCH_QUERY));

            SearchableFragment fragment = new SearchableFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.searchable_fragment_container, fragment)
                    .commit();
        }
    }
}
