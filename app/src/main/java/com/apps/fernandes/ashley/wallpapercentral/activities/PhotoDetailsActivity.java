package com.apps.fernandes.ashley.wallpapercentral.activities;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alexvasilkov.gestures.GestureController;
import com.alexvasilkov.gestures.Settings;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.apps.fernandes.ashley.wallpapercentral.R;
import com.apps.fernandes.ashley.wallpapercentral.network.HandleDownloads;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


public class PhotoDetailsActivity extends AppCompatActivity {

    private ProgressBar progress;
    boolean downloadComplete = false;
    boolean click = true ;
    boolean shareEnable = false;
    boolean fileExists;
    private GestureImageView gestureImageView;
    private Toolbar toolbar;
    private CoordinatorLayout coordinator;
    private RelativeLayout relative;
    private AppBarLayout appBarLayout;
    private LinearLayout linear;
    private Button shareButton,download,setWallpaper,artistName;
    private HandleDownloads handleDownloads = new HandleDownloads();
    private String raw,full,id;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_photo_details);
        progress = (ProgressBar) findViewById(R.id.progress);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarLayout);
        gestureImageView =  (GestureImageView) findViewById(R.id.gesture_view);
        relative = (RelativeLayout) findViewById(R.id.details_relative_layout);
        artistName = (Button) findViewById(R.id.details_artist_name);
        linear = (LinearLayout) findViewById(R.id.bottom_linear_view);
        shareButton = (Button) findViewById(R.id.share_button);
        download  = (Button) findViewById(R.id.download_button);
        setWallpaper = (Button) findViewById(R.id.set_wallpaper_button);
        coordinator = (CoordinatorLayout) findViewById(R.id.coord_Layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





        Intent intent = getIntent();

        String url = intent.getStringExtra("regURL");
        String artist = intent.getStringExtra("artist");
        final String artistHtml = intent.getStringExtra("artistPage");
        full = intent.getStringExtra("fullURL");
        raw = intent.getStringExtra("rawURL");
        id  = intent.getStringExtra("id");

        artistName.setText(artist);

        gestureImageView.getController().
                getSettings()
                .setFillViewport(true)
               .setFitMethod(Settings.Fit.VERTICAL)
                .setZoomEnabled(true)
                .setPanEnabled(true)
                .setOverscrollDistance(1f, 1f);


       Glide.with(getApplicationContext()).load(url).crossFade().listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progress.setVisibility(View.GONE);
                shareEnable = true;
                return false;
            }
        })
                .into(gestureImageView);

        GestureController.OnGestureListener listener = new GestureController.OnGestureListener(){
            @Override
            public void onDown(@NonNull MotionEvent event) {

            }

            @Override
            public void onUpOrCancel(@NonNull MotionEvent event) {

            }

            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent event) {
                if(click){
                    appBarLayout.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
                    linear.animate().translationY(gestureImageView.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
                    click = !click;
                }
                else {
                    appBarLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                    linear.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                    click = !click;
                }
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(@NonNull MotionEvent event) {
                return false;
            }

            @Override
            public void onLongPress(@NonNull MotionEvent event) {

            }

            @Override
            public boolean onDoubleTap(@NonNull MotionEvent event) {
                return false;
            }
        };
        gestureImageView.getController().setOnGesturesListener(listener);






        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shareEnable){
                photoShare();
                }
            }
        });

        artistName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = /*"https://" +*/ artistHtml;
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);


            }
        });

        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileExists = handleDownloads.DownloadData(v.getContext(),full,id,v);
                downloadFinish(v.getContext().getApplicationContext(),v);
                if(fileExists){
                    setWallpaper(handleDownloads.getAlreadyDownloadedFileUri());
                }

            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileExists = handleDownloads.DownloadData(v.getContext(),full,id,v);
                downloadFinish(v.getContext().getApplicationContext(),v);
                if(fileExists){
                    Snackbar.make(coordinator,R.string.alreadyDownloaded,Snackbar.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    protected void onPause() {
        super.onPause();

      //unregisterReceiver(receiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
       // unregisterReceiver(receiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  unregisterReceiver(receiver);
    }


    public void downloadFinish(Context context, View v){
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        try {
            registerReceiver(setReceiver(context, v), filter);
        }catch (RuntimeException e){
            e.printStackTrace();
        }
        setReceiver(context,v);


    }

   public BroadcastReceiver setReceiver(final Context appContext, final View v) {

         receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                downloadComplete = true;
                long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);


                if (handleDownloads.getDownloadReference() == referenceId ){
                    Toast.makeText(appContext, "Downloaded Successfully", Toast.LENGTH_SHORT).show();
                }

                Uri uriString = handleDownloads.getDownloadManager().getUriForDownloadedFile(handleDownloads.getDownloadReference());
               // handleDownloads.setImageStatus(appContext);
                if(v.getId() == R.id.set_wallpaper_button){
                    setWallpaper(uriString);
                }

            }
        };


        return receiver;
    }




    public void setWallpaper(Uri uriString){

       // Drawable mDrawable = gestureImageView.getDrawable();
        //Bitmap mBitmap = ((GlideBitmapDrawable) mDrawable).getBitmap();

        /*WallpaperManager wallpaper = WallpaperManager.getInstance(getApplicationContext());
        try {
            wallpaper.setBitmap(mBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

       // String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "Wallpaper", null);
        Uri uri = uriString;

        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.setType("image/*");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("mimeType", "image/*");
        startActivity(Intent.createChooser(intent, "Set As "));

    }

    public void photoShare(){
        Drawable mDrawable = gestureImageView.getDrawable();
        Bitmap mBitmap = ((GlideBitmapDrawable) mDrawable).getBitmap();

        String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "Image Description", null);
        Uri uri = Uri.parse(path);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share Image"));
    }
}
