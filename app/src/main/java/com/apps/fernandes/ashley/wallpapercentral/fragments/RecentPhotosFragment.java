package com.apps.fernandes.ashley.wallpapercentral.fragments;


import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.apps.fernandes.ashley.wallpapercentral.R;
import com.apps.fernandes.ashley.wallpapercentral.adapters.PhotosRecyclerAdapter;
import com.apps.fernandes.ashley.wallpapercentral.models.FavouritePhotos;
import com.apps.fernandes.ashley.wallpapercentral.models.Keys;
import com.apps.fernandes.ashley.wallpapercentral.models.PhotoModelUnsplash;
import com.apps.fernandes.ashley.wallpapercentral.network.CustomVolleyRequestSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by gf on 26-06-2017.
 */

public class RecentPhotosFragment extends Fragment {

    RecyclerView mRecentRecycler;
    private List<PhotoModelUnsplash> mList = new ArrayList<>();
    private View mView;
    private PhotosRecyclerAdapter mAdapter;
    private RequestQueue requestQueue;
    private int pageIndex = 2;
    private int pixabayPageIndex = 1;
    private boolean sourceSwitch = false;
    private FloatingActionButton fabButton;
    private SwipeRefreshLayout swipe;
    private RequestQueue mRequest;
    private ImageLoader imageLoader;
    private RealmResults<FavouritePhotos> favouritePhotosRealmResults;
    private List<String> idList = new ArrayList<>();
    private static boolean fetchFlag = false;




    public static RecentPhotosFragment newInstance(){
        RecentPhotosFragment fragment = new RecentPhotosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecentPhotosFragment fragme = new RecentPhotosFragment();
        if(fragme.isAdded()){
            Toast.makeText(getContext(), "is added", Toast.LENGTH_SHORT).show();
        }
        Log.d("frag", "onCreate: frag");
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Toast.makeText(context, "on attach recent", Toast.LENGTH_SHORT).show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        imageLoader = CustomVolleyRequestSingleton.getInstance(getContext().getApplicationContext()).getImageLoader();
        mView = inflater.inflate(R.layout.fragment_recent, container, false);
        fabButton = (FloatingActionButton) mView.findViewById(R.id.fab);
        mRecentRecycler = (RecyclerView) mView.findViewById(R.id.recent_recycler_id);
        swipe = (SwipeRefreshLayout) mView.findViewById(R.id.swipeRefreshLayout);
        mRecentRecycler.setItemViewCacheSize(10);

        Realm.init(mView.getContext());
      //  final Realm realm = Realm.getDefaultInstance();
        final Realm realm = Realm.getInstance(new RealmConfiguration.Builder().name("NewRealmDatabase.realm").build());
        favouritePhotosRealmResults = realm.where(FavouritePhotos.class).findAll();

        for (int i=0; i<favouritePhotosRealmResults.size();i++) {
        String sId = favouritePhotosRealmResults.get(i).getvPhotoId();
            idList.add(sId);
        }

        Log.d("frag", "onCreateView: ");
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setItemPrefetchEnabled(true);
        linearLayoutManager.setInitialPrefetchItemCount(100);
        mRecentRecycler.setLayoutManager(linearLayoutManager);
        mRecentRecycler.setVerticalScrollBarEnabled(true);


            //getData();
            getPixabayData(pixabayPageIndex);
            pixabayPageIndex++;

        //getPixabayData(pixabayPageIndex);


        mAdapter = new PhotosRecyclerAdapter(mList,getContext(),RecentPhotosFragment.newInstance(),imageLoader,realm,idList);
        mRecentRecycler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mRecentRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLastItemDisplaying(mRecentRecycler)){
                    Log.d("scrolltest", "onScrolled: ");
                   // getNewData(pageIndex);
                   // pageIndex += 1;
                    if(sourceSwitch){
                        getPixabayData(pixabayPageIndex);
                        pixabayPageIndex += 1;
                        Log.d("scrolltest", "Pixabay: ");

                    }else{
                        getNewData(pageIndex);
                        pageIndex += 1;
                        Log.d("scrolltest", "Unsplash: ");

                    }
                    sourceSwitch = !sourceSwitch;

                }
            }
        });

        mRecentRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter = new PhotosRecyclerAdapter(mList,getContext(),RecentPhotosFragment.newInstance(),imageLoader,realm,idList);
                mRecentRecycler.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              //  mList.clear();
                getData();
                //getPixabayData(pixabayPageIndex);
                swipe.setRefreshing(false);
            }
        });

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecentRecycler.scrollToPosition(0);
                Toast.makeText(getActivity(),String.valueOf(mList.size()), Toast.LENGTH_SHORT).show();
            }
        });
        return mView;
    }

    public void getData(){
      //  final ProgressDialog loading = ProgressDialog.show(mView.getContext(),"Loading Data", "Please wait...",false,true);
        swipe.setRefreshing(true);
        Log.d("frag", "getData: ");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Keys.FULL_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                      //  loading.dismiss();
                        swipe.setRefreshing(false);
                        parseData(response);
                    }

                },
            new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.toString().equals("Rate Limit Exceeded"))
                {
                    Toast.makeText(getContext(), "Rate Limit Exceeded", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(mView.getContext(),error.toString(), Toast.LENGTH_SHORT).show();
            }
        }



        );
        mRequest =  CustomVolleyRequestSingleton.getInstance(mView.getContext().getApplicationContext()).getRequestQueue();

        //        .add(jsonArrayRequest);

       // mRequest = Volley.newRequestQueue(mView.getContext().getApplicationContext());
        mRequest.add(jsonArrayRequest);

    }


    public void getNewData(int pageIndex){
        swipe.setRefreshing(true);
        Log.d("frag", "getNewData: ");
        String URL = Keys.API_URL + Keys.PHOTO_PARAM + Keys.PAGE_PARAM + String.valueOf(pageIndex) + Keys.PER_PAGE_PARAM + Keys.API_KEY_WPARAM;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        swipe.setRefreshing(false);
                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(mView.getContext(),error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        CustomVolleyRequestSingleton.getInstance(mView.getContext().getApplicationContext()).getRequestQueue().add(jsonArrayRequest);
        // CustomVolleyRequestSingleton.getRequestQueue().add(jsonArrayRequest);
        // mRequest.add(jsonArrayRequest);
        // requestQueue.add(jsonArrayRequest);
    }


    public void getPixabayData(int pxPageIndex){
        swipe.setRefreshing(true);
        Log.d("frag", "getPixabayData: ");
        String pixabayUrl = Keys.PIXABAY_API_URL + Keys.PIXABAY_PAGE_PARAM + String.valueOf(pxPageIndex);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,pixabayUrl,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            swipe.setRefreshing(false);
                            parsePXData(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mView.getContext(),error.toString(), Toast.LENGTH_SHORT).show();
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            switch(response.statusCode){
                                case 400:
                                    Toast.makeText(mView.getContext(), "link expired", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };


        CustomVolleyRequestSingleton.getInstance(mView.getContext().getApplicationContext()).getRequestQueue().add(jsonObjectRequest);
        // mRequest = Volley.newRequestQueue(mView.getContext().getApplicationContext());
        //mRequest.add(jsonObjectRequest);


    }


    public void parsePXData(JSONObject object) throws JSONException {
        JSONArray jsonArray ;
        jsonArray = object.getJSONArray("hits");

        for (int i = 0; i<jsonArray.length();i++){
            PhotoModelUnsplash modelUnsplash = new PhotoModelUnsplash();
            JSONObject resJson = null;

            try {
                resJson = jsonArray.getJSONObject(i);
                modelUnsplash.setArtistName(resJson.getString(Keys.TAG_USER_PX));
                modelUnsplash.setPhotoSmallUrl(resJson.getString(Keys.TAG_WEBFORMAT_URL_PX));
                modelUnsplash.setPhotoFullUrl(resJson.getString(Keys.TAG_FULLHD_URL_PX));
                modelUnsplash.setPhotoRegularUrl(resJson.getString(Keys.TAG_LARGEIMAGE_URL_PX));
                modelUnsplash.setPhotoId(resJson.getString(Keys.TAG_ID_HASH_PX));
                modelUnsplash.setArtistUrl("http://www.sps-shop.com/ekmps/shops/stcadmin6678/images/everyday-card-with-you-are-awesome-image-10312-p.jpg");
                modelUnsplash.setPhotoSource("pixabay");
                String colors[] = {"#bcc2e5","#d6db51","#e2d1f9","#edb4bb"};
                Random rand = new Random();
                int randNum = rand.nextInt(4);
                modelUnsplash.setPhotoColor(colors[randNum]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mList.add(modelUnsplash);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void parseData(JSONArray array){

        for(int i = 0; i<array.length();i++){
            PhotoModelUnsplash model = new PhotoModelUnsplash();
            JSONObject json = null;

            try {
                //response object
                json = array.getJSONObject(i);
                model.setPhotoId(json.getString("id"));
                model.setPhotoHeight(Integer.valueOf(json.getString(Keys.TAG_HEIGHT)));
                model.setPhotoWidth(Integer.valueOf(json.getString(Keys.TAG_WIDTH)));
                model.setPhotoColor(json.getString(Keys.TAG_COLOR));

                //user object
                JSONObject user = json.getJSONObject(Keys.TAG_USER);
                model.setArtistName(user.getString(Keys.TAG_NAME));

                //urls object
                JSONObject urls = json.getJSONObject(Keys.TAG_URLS);
                model.setPhotoRawUrl(urls.getString(Keys.TAG_RAW));
                model.setPhotoFullUrl(urls.getString(Keys.TAG_FULL));
                model.setPhotoSmallUrl(urls.getString(Keys.TAG_SMALL));
                model.setPhotoThumbUrl(urls.getString(Keys.TAG_THUMB));
                model.setPhotoRegularUrl(urls.getString(Keys.TAG_REGULAR));
                model.setPhotoSource("unsplash");
                //links object
                JSONObject links = json.getJSONObject(Keys.TAG_LINKS);
                model.setArtistUrl(links.getString(Keys.TAG_USER_HTML));
                model.setPhotoDownloadUrl(links.getString(Keys.TAG_DOWNLOAD_LINK));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            mList.add(model);
        }

                mAdapter.notifyDataSetChanged();
    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("frag", "onDestroy: ");
    }
}
