package com.apps.fernandes.ashley.wallpapercentral.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.apps.fernandes.ashley.wallpapercentral.fragments.CategoriesFragment;
import com.apps.fernandes.ashley.wallpapercentral.fragments.PopularPhotosFragment;
import com.apps.fernandes.ashley.wallpapercentral.fragments.RecentPhotosFragment;

/**
 * Created by gf on 26-06-2017.
 */

public class TabsViewPagerAdapter extends FragmentStatePagerAdapter{

    public TabsViewPagerAdapter(FragmentManager fm){
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
            return  CategoriesFragment.newInstance();

            case 1:
                RecentPhotosFragment f = new RecentPhotosFragment();
                f.newInstance();
            return f;

            case 2:
            return PopularPhotosFragment.newInstance();

        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
