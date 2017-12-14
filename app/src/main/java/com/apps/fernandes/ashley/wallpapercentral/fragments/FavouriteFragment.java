package com.apps.fernandes.ashley.wallpapercentral.fragments;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.apps.fernandes.ashley.wallpapercentral.R;
import com.apps.fernandes.ashley.wallpapercentral.adapters.FavouritePhotosRecyclerAdapter;
import com.apps.fernandes.ashley.wallpapercentral.adapters.PhotosRecyclerAdapter;
import com.apps.fernandes.ashley.wallpapercentral.models.FavouritePhotos;
import com.apps.fernandes.ashley.wallpapercentral.network.CustomVolleyRequestSingleton;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by gf on 26-06-2017.
 */

public class FavouriteFragment extends Fragment {

    private View mView;
    private RecyclerView mFavouriteRecycler;
    private FloatingActionButton favFabButton;
    private FavouritePhotosRecyclerAdapter mAdapter;
    private RealmResults<FavouritePhotos> favouritePhotosRealmResults;
    private ImageLoader imageLoader;




    public static FavouriteFragment newInstance(){
        FavouriteFragment fragment = new FavouriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        imageLoader = CustomVolleyRequestSingleton.getInstance(getContext().getApplicationContext()).getImageLoader();

        mView = inflater.inflate(R.layout.fragment_favourite, container, false);

        favFabButton = (FloatingActionButton) mView.findViewById(R.id.fab_fav);
        mFavouriteRecycler = (RecyclerView) mView.findViewById(R.id.fav_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mView.getContext());
        mFavouriteRecycler.setItemViewCacheSize(4);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setItemPrefetchEnabled(true);
        linearLayoutManager.setInitialPrefetchItemCount(150);
        mFavouriteRecycler.setLayoutManager(linearLayoutManager);
        mFavouriteRecycler.setVerticalScrollBarEnabled(true);

        //Realm realm = Realm.getDefaultInstance();
        final Realm realm = Realm.getInstance(new RealmConfiguration.Builder().name("NewRealmDatabase.realm").build());
        favouritePhotosRealmResults = realm.where(FavouritePhotos.class).findAll();

        mAdapter = new FavouritePhotosRecyclerAdapter(getContext(),favouritePhotosRealmResults,imageLoader,realm);
        mFavouriteRecycler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();



        favFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFavouriteRecycler.smoothScrollToPosition(0);
            }
        });

        customToast("Favourites");

        return mView;
    }
    public void customToast(String text){

        LayoutInflater li = getActivity().getLayoutInflater();
        //Getting the View object as defined in the customtoast.xml file
        View layout = li.inflate(R.layout.custom_toast,
                (ViewGroup) mView.findViewById(R.id.toastRoot));
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
