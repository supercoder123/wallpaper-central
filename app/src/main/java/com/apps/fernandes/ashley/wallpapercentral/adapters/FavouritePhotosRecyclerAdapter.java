package com.apps.fernandes.ashley.wallpapercentral.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.apps.fernandes.ashley.wallpapercentral.R;
import com.apps.fernandes.ashley.wallpapercentral.activities.PhotoDetailsActivity;
import com.apps.fernandes.ashley.wallpapercentral.models.FavouritePhotos;

import com.apps.fernandes.ashley.wallpapercentral.models.Keys;
import com.apps.fernandes.ashley.wallpapercentral.models.PhotoModelUnsplash;
import com.apps.fernandes.ashley.wallpapercentral.network.CustomVolleyRequestSingleton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.util.Map;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by gf on 07-07-2017.
 */

public class FavouritePhotosRecyclerAdapter extends RecyclerView.Adapter<FavouritePhotosRecyclerAdapter.FavouritePhotosViewHolder> {

    Context context;
    RealmResults<FavouritePhotos> resultsList;
    FavouritePhotosViewHolder mHolder;
    ImageLoader imageLoader;
    FavouritePhotos mModel;
    RealmResults<FavouritePhotos> favList;
    boolean loadFlag = false;
    String smallUrlPx="",regUrl,fullUrl;
    Realm realm;

//another try for branch

    public FavouritePhotosRecyclerAdapter(Context context, RealmResults<FavouritePhotos> resultsList,ImageLoader imageLoader, Realm realm){
        this.context = context;
        this.resultsList = resultsList;
        this.imageLoader = imageLoader;
        this.realm = realm;

    }


    @Override
    public FavouritePhotosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemRow= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row,parent,false);
        mHolder = new FavouritePhotosViewHolder(itemRow);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(final FavouritePhotosViewHolder holder, final int position) {
        mModel = resultsList.get(position);
        holder.textView.setText(resultsList.get(position).getvArtistName());

        holder.imageView.setImageResource(android.R.color.transparent);

/*        if(mModel.getvPhotoSource().equals("pixabay") && response.statusCode == 400){
           // getPixabayDataFav(mModel.getvPhotoId());
            Toast.makeText(context, "getting new link", Toast.LENGTH_SHORT).show();
        }*/
        //imageLoader.get(mModel.getvPhotoSmallUrl() , ImageLoader.getImageListener(holder.networkImageView, R.drawable.default_image, R.drawable.no_image));

        holder.imageView.setBackgroundColor(Color.parseColor(mModel.getvPhotoColor()));
       // holder.rowAnimations();
       // holder.networkImageView.setScaleType(ImageView.ScaleType.FIT_XY);
       // holder.networkImageView.setImageUrl(mModel.getvPhotoSmallUrl(), imageLoader);
       // Glide.with(context).load(resultsList.get(position).getvPhotoSmallUrl()).diskCacheStrategy(DiskCacheStrategy.ALL);

         SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                holder.imageView.setImageBitmap(bitmap);
                loadFlag = true;
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
               String picId = resultsList.get(position).getvPhotoId();
                //Log.d("url",picId);
               // holder.imageView.setImageResource(R.drawable.no_image);
               /// Toast.makeText(context, "Link expired yo" + picId, Toast.LENGTH_SHORT).show();
                getPixabayDataFav(picId,position);
                loadFlag = false;
            }
        };
                 Glide.with(context).load(resultsList.get(position).getvPhotoSmallUrl()).asBitmap()
                         .diskCacheStrategy(DiskCacheStrategy.ALL).into(target);

    }

    /*
    @Override
    public void onViewAttachedToWindow(FavouritePhotosRecyclerAdapter.FavouritePhotosViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Log.d("test", "onViewAttachedToWindow: ");
        int wPosition = holder.getAdapterPosition();
        mModel = resultsList.get(wPosition);
        imageLoader.get(mModel.getvPhotoSmallUrl(), ImageLoader.getImageListener(holder.networkImageView, R.drawable.default_image, R.drawable.no_image));
        holder.networkImageView.setBackgroundColor(Color.parseColor(mModel.getvPhotoColor()));
        holder.networkImageView.setImageUrl(mModel.getvPhotoSmallUrl(), imageLoader);
        holder.networkImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.rowAnimations();
    }*/



    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    public class FavouritePhotosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imageView;
        public TextView textView;
        public LikeButton likeButton;

        public FavouritePhotosViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.artist_name_id);
            imageView = (ImageView) itemView.findViewById(R.id.list_row_image_view);
            likeButton = (LikeButton)itemView.findViewById(R.id.heart_button);
            likeButton.setVisibility(View.INVISIBLE);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            mModel = resultsList.get(pos);
            Toast.makeText(context,resultsList.get(pos).getvPhotoSource(), Toast.LENGTH_SHORT).show();

            String regular = mModel.getvPhotoRegularUrl();
            String full = mModel.getvPhotoFullUrl();
            Log.d("url", "onClick:"+ full);
            //String raw = resultsList.get(pos).getvPhotoRawUrl();
            String artist = mModel.getvArtistName();
            String id = mModel.getvPhotoId();
            String artistPageLink = mModel.getvArtistUrl();
            Intent intent = new Intent(context, PhotoDetailsActivity.class);
            intent.putExtra("regURL",regular);
            intent.putExtra("fullURL",full);
            intent.putExtra("id",id);
            Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
            Log.d("picid",id);
            intent.putExtra("artistPage",artistPageLink);
           // intent.putExtra("rawURL",raw);
            intent.putExtra("artist",artist);
            context.startActivity(intent);
        }
        public void rowAnimations(){
            Animation animFadeIn = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.fade_in);
            animFadeIn.reset();
            imageView.clearAnimation();
            imageView.startAnimation(animFadeIn);

        }
    }


    public void getPixabayDataFav(final String photoId, final int position){
        //swipe.setRefreshing(true);
        String pixabayUrl = Keys.PIXABAY_FETCH_BY_ID + photoId.trim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,pixabayUrl,null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //swipe.setRefreshing(false);
                            smallUrlPx = parsePXDataFav(response,position,photoId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(mView.getContext(),error.toString(), Toast.LENGTH_SHORT).show();
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            switch(response.statusCode){
                                case 400:
                                    Toast.makeText(context, "link expired", Toast.LENGTH_SHORT).show();
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


        CustomVolleyRequestSingleton.getInstance(context.getApplicationContext()).getRequestQueue().add(jsonObjectRequest);
        // mRequest = Volley.newRequestQueue(mView.getContext().getApplicationContext());
        //mRequest.add(jsonObjectRequest);


    }


    public String parsePXDataFav(JSONObject object, int position, final String photoId) throws JSONException {
        JSONArray jsonArray ;
        jsonArray = object.getJSONArray("hits");

      //  for (int i = 0; i<jsonArray.length();i++){
            FavouritePhotos favPhoto = new FavouritePhotos();
            JSONObject resJson = null;
            String smallUrl=" ";

            try {
                resJson = jsonArray.getJSONObject(0);
                //jsonArray.getString()
                 smallUrl = resJson.getString(Keys.TAG_WEBFORMAT_URL_PX);
                 fullUrl = resJson.getString(Keys.TAG_FULLHD_URL_PX);
                 regUrl = resJson.getString(Keys.TAG_LARGEIMAGE_URL_PX);
                Log.d("urlFix", "parsePXDataFav:"+fullUrl);
               // modelUnsplash.setArtistName(resJson.getString(Keys.TAG_USER_PX));
               // mModel.setvPhotoSmallUrl(resJson.getString(Keys.TAG_WEBFORMAT_URL_PX));
                //mModel.setvPhotoFullUrl(resJson.getString(Keys.TAG_FULLHD_URL_PX));
                //mModel.setvPhotoRegularUrl(resJson.getString(Keys.TAG_LARGEIMAGE_URL_PX));
               // mModel.setvPhotoId(resJson.getString(Keys.TAG_ID_HASH_PX));
               // mModel.setvArtistUrl("http://www.sps-shop.com/ekmps/shops/stcadmin6678/images/everyday-card-with-you-are-awesome-image-10312-p.jpg");
               // modelUnsplash.setPhotoSource("pixabay");
               // String colors[] = {"#bcc2e5","#d6db51","#e2d1f9","#edb4bb"};
               // Random rand = new Random();
               // int randNum = rand.nextInt(4);
               // modelUnsplash.setPhotoColor(colors[randNum]);

               // favList = realm.where(FavouritePhotos.class).equalTo("vPhotoId",photoId).findAll();
                final String finalSmallUrl = smallUrl;

                ////..code to update realm database (favourite photos) for expired pixabay photo links
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                    FavouritePhotos ff = realm.where(FavouritePhotos.class).equalTo("vPhotoId",photoId).findFirst();
                        ff.setvPhotoSmallUrl(finalSmallUrl);
                        ff.setvPhotoFullUrl(fullUrl);
                        ff.setvPhotoRegularUrl(regUrl);
                        realm.insertOrUpdate(ff);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //mList.add(modelUnsplash);
       // }
        notifyDataSetChanged();
        return smallUrl;
    }
}
