package com.apps.fernandes.ashley.wallpapercentral.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.apps.fernandes.ashley.wallpapercentral.R;
import com.apps.fernandes.ashley.wallpapercentral.activities.PhotoDetailsActivity;
import com.apps.fernandes.ashley.wallpapercentral.models.FavouritePhotos;
import com.apps.fernandes.ashley.wallpapercentral.models.PopularPhotos;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by gf on 15-07-2017.
 */

public class PopularPhotosRecyclerAdapter extends RecyclerView.Adapter<PopularPhotosRecyclerAdapter.PopularPhotosViewHolder>{
    List<PopularPhotos> adapList;
    private ImageLoader imageLoader ;
    private Context context;
    PopularPhotosViewHolder mHolder ;
    Fragment fragment;
    Realm realm;
    RealmResults<FavouritePhotos> favList;
    PopularPhotos mModel;
    List<String> idList;


    public PopularPhotosRecyclerAdapter(List<PopularPhotos> adapList, Context context,
                                 Fragment fragment,ImageLoader imageLoader,Realm realm,
                                 List<String> idList){
        this.adapList = adapList;
        this.context = context;
        this.fragment = fragment;
        this.imageLoader = imageLoader;
        this.realm = realm;
        this.idList = idList;
        setHasStableIds(true);


    }



    @Override
    public PopularPhotosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemRow= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row,parent,false);
        mHolder = new PopularPhotosViewHolder(itemRow);
        return mHolder;    }

    @Override
    public void onBindViewHolder(final PopularPhotosViewHolder holder, final int position) {


        Log.d("popular", "onBindViewHolder: ");
        mModel = adapList.get(position);
        holder.imageView.setImageResource(android.R.color.transparent);
        holder.textView.setText(mModel.getArtistName());
        holder.imageView.setBackgroundColor(Color.parseColor(mModel.getPhotoColor()));
        //holder.rowAnimations();
        //holder.networkImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //holder.networkImageView.setImageUrl(mModel.getPhotoSmallUrl(), imageLoader);

        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                holder.imageView.setImageBitmap(bitmap);
            }
        };



        Glide.with(context).load(adapList.get(position).getPhotoSmallUrl()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(target);









        if (idList.contains(mModel.getPhotoId())){
            holder.likeButton.setLiked(true);
        }else{
            holder.likeButton.setLiked(false);
        }




        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            FavouritePhotos fav;
            PopularPhotos likeModel = adapList.get(position);
            @Override
            public void liked(LikeButton likeButton) {
                Toast.makeText(context, "liked", Toast.LENGTH_SHORT).show();

                fav = new FavouritePhotos(likeModel.getArtistName(),likeModel.getArtistUrl(),likeModel.getPhotoFullUrl(),
                        likeModel.getPhotoColor(),likeModel.getPhotoRegularUrl(),likeModel.getPhotoSmallUrl(),likeModel.getPhotoId(),likeModel.getPhotoSource());

                idList.add(fav.getvPhotoId());

                realm.beginTransaction();
                FavouritePhotos favCopy = realm.copyToRealm(fav);
                realm.commitTransaction();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Toast.makeText(context, "unliked", Toast.LENGTH_SHORT).show();
                realm.beginTransaction();
                idList.remove(fav.getvPhotoId());
                favList = realm.where(FavouritePhotos.class).equalTo("vPhotoId",likeModel.getPhotoId()).findAll();
                favList.deleteAllFromRealm();
                realm.commitTransaction();

            }
        });





        //holder.networkImageView.setImageBitmap(null);

        Log.d("test", "onBindViewHolder:"+adapList.get(position).getArtistName());


    }

    @Override
    public int getItemCount() {
        return adapList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class PopularPhotosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView;
        public TextView textView;
        public LikeButton likeButton;

        public PopularPhotosViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.artist_name_id);
            // networkImageView = (NetworkImageView) itemView.findViewById(R.id.list_row_image_view);
            imageView = (ImageView) itemView.findViewById(R.id.list_row_image_view);
            //networkImageView.setDrawingCacheEnabled(true);
            //networkImageView.buildDrawingCache();
            likeButton = (LikeButton)itemView.findViewById(R.id.heart_button);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();

            String regular = adapList.get(pos).getPhotoRegularUrl();
            String full = adapList.get(pos).getPhotoFullUrl();
            String raw = adapList.get(pos).getPhotoRawUrl();
            String artist = adapList.get(pos).getArtistName();
            String id = adapList.get(pos).getPhotoId();
            String artistPageLink = adapList.get(pos).getArtistUrl();
            Intent intent = new Intent(context, PhotoDetailsActivity.class);
            intent.putExtra("regURL",regular);
            intent.putExtra("fullURL",full);
            intent.putExtra("id",id);
            intent.putExtra("artistPage",artistPageLink);
            intent.putExtra("rawURL",raw);
            intent.putExtra("artist",artist);
            context.startActivity(intent);
        }
    }
}
