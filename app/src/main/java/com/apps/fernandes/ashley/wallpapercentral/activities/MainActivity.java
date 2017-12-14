package com.apps.fernandes.ashley.wallpapercentral.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.volley.toolbox.ImageLoader;
import com.apps.fernandes.ashley.wallpapercentral.R;
import com.apps.fernandes.ashley.wallpapercentral.adapters.TabsViewPagerAdapter;
import com.apps.fernandes.ashley.wallpapercentral.fragments.FavouriteFragment;
import com.apps.fernandes.ashley.wallpapercentral.fragments.HomeFragment;
import com.apps.fernandes.ashley.wallpapercentral.fragments.SearchFragment;
import com.apps.fernandes.ashley.wallpapercentral.network.CustomVolleyRequestSingleton;
import com.pixelcan.inkpageindicator.InkPageIndicator;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottom;
    private TabsViewPagerAdapter mTabAdapter;
    private ViewPager viewPager;
    private ImageLoader imageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottom = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
//This is a test for github



        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.nav_home_id:
                        selectedFragment = HomeFragment.newInstance();
                        break;
                    case R.id.nav_search_id:
                        selectedFragment = SearchFragment.newInstance();
                        break;
                    case R.id.nav_fav_id:
                        selectedFragment = FavouriteFragment.newInstance();
                        break;
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout,selectedFragment);
                transaction.commit();
                return true;
            }
        });


        //for first time initialization

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, HomeFragment.newInstance());
        transaction.commit();



    }
}
