package com.apps.fernandes.ashley.wallpapercentral.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by gf on 27-06-2017.
 */

public class CustomVolleyRequestSingleton {

    private static CustomVolleyRequestSingleton mInstance;
    private static Context context;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private CustomVolleyRequestSingleton(Context context){
        this.context = context;
        this.requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache(){
                      private final LruCache<String,Bitmap> cache = new LruCache<String, Bitmap>(20);


                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url,bitmap);
                    }
                }


        );

    }

    public static synchronized CustomVolleyRequestSingleton getInstance(Context context){
        if(mInstance == null){
           mInstance = new CustomVolleyRequestSingleton(context);


        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
                Cache cache = new DiskBasedCache(context.getCacheDir(),10 * 1024 *1024);
                Network network = new BasicNetwork(new HurlStack());
                requestQueue = new RequestQueue(cache, network);
                requestQueue.start();
        }
        return requestQueue;
    }


    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
