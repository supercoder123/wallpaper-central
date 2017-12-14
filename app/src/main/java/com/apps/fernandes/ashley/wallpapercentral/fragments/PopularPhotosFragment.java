package com.apps.fernandes.ashley.wallpapercentral.fragments;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.apps.fernandes.ashley.wallpapercentral.adapters.PopularPhotosRecyclerAdapter;
import com.apps.fernandes.ashley.wallpapercentral.models.FavouritePhotos;
import com.apps.fernandes.ashley.wallpapercentral.models.Keys;
import com.apps.fernandes.ashley.wallpapercentral.models.PopularPhotos;
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

public class PopularPhotosFragment extends Fragment {

    RecyclerView mPopularRecycler;
    private List<PopularPhotos> mList = new ArrayList<>();
    private View mView;
    private PopularPhotosRecyclerAdapter mAdapter;
    private RequestQueue requestQueue;
    private int pageIndex = 1;
    private int pixabayPageIndex = 1;
    private boolean sourceSwitch = true;
    private FloatingActionButton fabButtonPop;
    private SwipeRefreshLayout swipePop;
    private RequestQueue mRequest;
    private ImageLoader imageLoader;
    private RealmResults<FavouritePhotos> favouritePhotosRealmResults;
    private List<String> idList = new ArrayList<>();

    public static PopularPhotosFragment newInstance(){
        PopularPhotosFragment fragment = new PopularPhotosFragment();
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
        mView = inflater.inflate(R.layout.fragment_popular_photos, container, false);
        fabButtonPop = (FloatingActionButton) mView.findViewById(R.id.fab_popular);
        mPopularRecycler = (RecyclerView) mView.findViewById(R.id.popular_recycler_id);
        swipePop = (SwipeRefreshLayout) mView.findViewById(R.id.swipeRefreshLayoutPopular);
        mPopularRecycler.setItemViewCacheSize(10);

        Realm.init(mView.getContext());
        //  final Realm realm = Realm.getDefaultInstance();
        final Realm realm = Realm.getInstance(new RealmConfiguration.Builder().name("NewRealmDatabase.realm").build());
        favouritePhotosRealmResults = realm.where(FavouritePhotos.class).findAll();

        for (int i=0; i<favouritePhotosRealmResults.size();i++) {
            String sId = favouritePhotosRealmResults.get(i).getvPhotoId();
            idList.add(sId);
        }


        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setItemPrefetchEnabled(true);
        linearLayoutManager.setInitialPrefetchItemCount(100);
        mPopularRecycler.setLayoutManager(linearLayoutManager);
        mPopularRecycler.setVerticalScrollBarEnabled(true);


        getData(pageIndex);

        //getPixabayData(pixabayPageIndex);

        Log.d("popular", "onCreateView: ");
        mAdapter = new PopularPhotosRecyclerAdapter(mList,getContext(),PopularPhotosFragment.newInstance(),imageLoader,realm,idList);
        mPopularRecycler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mPopularRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLastItemDisplaying(mPopularRecycler)){
                    Log.d("scrolltest", "onScrolled: ");
                    // getNewData(pageIndex);
                    // pageIndex += 1;
                    if(sourceSwitch){
                        getPixabayData(pixabayPageIndex);
                        pixabayPageIndex += 1;
                        Log.d("scrolltest", "Pixabay: ");

                    }else{
                        pageIndex += 1;
                        getData(pageIndex);
                        Log.d("scrolltest", "Unsplash: ");

                    }
                    sourceSwitch = !sourceSwitch;

                }
            }
        });

        mPopularRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter = new PopularPhotosRecyclerAdapter(mList,getContext(),RecentPhotosFragment.newInstance(),imageLoader,realm,idList);
                mPopularRecycler.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }
        });

        swipePop.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               // mList.clear();
                getData(pageIndex);
                //getPixabayData(pixabayPageIndex);
                swipePop.setRefreshing(false);
            }
        });

        fabButtonPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopularRecycler.scrollToPosition(0);
                Toast.makeText(getActivity(),String.valueOf(mList.size()), Toast.LENGTH_SHORT).show();
            }
        });
        return mView;
    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    public void getData(int pageIndex){
        //  final ProgressDialog loading = ProgressDialog.show(mView.getContext(),"Loading Data", "Please wait...",false,true);
        swipePop.setRefreshing(true);

        String url = Keys.API_URL_UNSPLASH_POPULAR + String.valueOf(pageIndex);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //  loading.dismiss();
                        swipePop.setRefreshing(false);
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
        mRequest.add(jsonArrayRequest);

    }

    public void parseData(JSONArray array){

        for(int i = 0; i<array.length();i++){
            PopularPhotos model = new PopularPhotos();
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



    public void getPixabayData(int pxPageIndex){
        swipePop.setRefreshing(true);
        String pixabayUrl = Keys.PIXABAY_POPULAR + Keys.PIXABAY_PAGE_PARAM + String.valueOf(pxPageIndex);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,pixabayUrl,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            swipePop.setRefreshing(false);
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
            PopularPhotos modelUnsplash = new PopularPhotos();
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


}
