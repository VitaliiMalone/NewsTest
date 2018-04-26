package com.example.user.newstest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.user.newstest.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsPagerActivity extends AppCompatActivity {

    private static final String TAG = "NewsPagerActivity";

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_pager);
        getSupportActionBar().setElevation(0);
        ButterKnife.bind(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return NewsFragment.newInstance("general");
                    case 1:
                        return NewsFragment.newInstance("business");
                    case 2:
                        return NewsFragment.newInstance("entertainment");
                    case 3:
                        return NewsFragment.newInstance("sports");
                    case 4:
                        return NewsFragment.newInstance("technology");
                    case 5:
                        return NewsFragment.newInstance("science");
                    case 6:
                        return NewsFragment.newInstance("health");
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 7;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getResources().getString(R.string.general_title);
                    case 1:
                        return getResources().getString(R.string.business_title);
                    case 2:
                        return getResources().getString(R.string.entertainment_title);
                    case 3:
                        return getResources().getString(R.string.sports_title);
                    case 4:
                        return getResources().getString(R.string.technology_title);
                    case 5:
                        return getResources().getString(R.string.science_title);
                    case 6:
                        return getResources().getString(R.string.health_title);
                    default:
                        return null;
                }
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.v(TAG, "onQueryTextSubmit: " + query);
                Intent intent = new Intent(NewsPagerActivity.this, SearchableActivity.class);
                intent.putExtra(SearchableFragment.ARG_SEARCH_QUERY, query);
                startActivity(intent);

                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_setting:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
