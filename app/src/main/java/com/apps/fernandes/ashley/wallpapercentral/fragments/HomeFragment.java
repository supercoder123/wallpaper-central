package com.apps.fernandes.ashley.wallpapercentral.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.SupportActivity;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.fernandes.ashley.wallpapercentral.R;
import com.apps.fernandes.ashley.wallpapercentral.adapters.TabsViewPagerAdapter;
import com.apps.fernandes.ashley.wallpapercentral.animations.ZoomOutPageTransformer;
import com.pixelcan.inkpageindicator.InkPageIndicator;

/**
 * Created by gf on 26-06-2017.
 */

public class HomeFragment extends Fragment {
    private ViewPager viewPager;
    private View view;
    private TabsViewPagerAdapter mTabAdapter;
    public static HomeFragment newInstance(){
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager_id);
        mTabAdapter = new TabsViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(mTabAdapter);
        viewPager.setPageTransformer(true,new ZoomOutPageTransformer());
        //viewPager.setPadding(20,0,20,0);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(20);
        customToast("Categories");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {

                if(position == 0){
                    customToast("categories");
                }
                else if(position == 1){
                    //Toast.makeText(view.getContext(), "Latest", Toast.LENGTH_SHORT).setGravity(Gravity.C).show();
                    customToast("Latest");
                }else if(position == 2){
                    //Toast.makeText(view.getContext(), "Popular", Toast.LENGTH_SHORT).show();
                    customToast("popular");

                }else if(position == 3){

                }else{

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setRetainInstance(true);
        InkPageIndicator inkPageIndicator = (InkPageIndicator)view.findViewById(R.id.indicator);
        inkPageIndicator.setViewPager(viewPager);






        return view;
    }

    public void customToast(String text){

        LayoutInflater li = getActivity().getLayoutInflater();
        //Getting the View object as defined in the customtoast.xml file
        View layout = li.inflate(R.layout.custom_toast,
                (ViewGroup) view.findViewById(R.id.toastRoot));
        TextView textView = (TextView) layout.findViewById(R.id.toastText);
        textView.setText(text);
        final Toast toast = new Toast(getContext().getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(layout);//setting the view of custom toast layout
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 500);
    }


}
