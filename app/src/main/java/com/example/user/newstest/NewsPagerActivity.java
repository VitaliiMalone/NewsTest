package com.example.user.newstest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class NewsPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_pager);
        getSupportActionBar().setElevation(0);

        viewPager = findViewById(R.id.viewpager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return NewsFragment.newInstance("general", position);
                    case 1:
                        return NewsFragment.newInstance("business", position);
                    case 2:
                        return NewsFragment.newInstance("entertainment", position);
                    case 3:
                        return NewsFragment.newInstance("sports", position);
                    case 4:
                        return NewsFragment.newInstance("technology", position);
                    case 5:
                        return NewsFragment.newInstance("science", position);
                    case 6:
                        return NewsFragment.newInstance("health", position);
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

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_setting:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
