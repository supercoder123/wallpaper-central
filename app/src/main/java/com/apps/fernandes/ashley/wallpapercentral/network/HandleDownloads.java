package com.apps.fernandes.ashley.wallpapercentral.network;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.apps.fernandes.ashley.wallpapercentral.activities.PhotoDetailsActivity;
import com.apps.fernandes.ashley.wallpapercentral.models.PhotoModelUnsplash;

import java.io.File;

import static android.content.Context.DOWNLOAD_SERVICE;
import static java.security.AccessController.getContext;

/**
 * Created by gf on 05-07-2017.
 */

public class HandleDownloads {

    DownloadManager downloadManager;
    long downloadReference;
    Uri alreadyDownloadedFileUri;



    public boolean DownloadData (Context context, String uri, String id, View v) {
        String idPhoto = id ;
        Context gg = context;
        downloadManager = (DownloadManager)v.getContext().getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri));
        String path = Environment.getDataDirectory().getAbsolutePath() + "/WallpaperHub/";
        request.setTitle("Downloading..").setDescription("Wallpaper is being downloaded ,please wait.");
        request.setMimeType("image/*");
        request.setDestinationInExternalPublicDir(Environment.getDataDirectory().getAbsolutePath() + "/WallpaperHub", idPhoto +".jpg").allowScanningByMediaScanner();
        //request.setVisibleInDownloadsUi(true);
        //request.setDestinationInExternalFilesDir(context,Environment.getExternalStorageDirectory().getAbsolutePath()+ "/WallpaperHub",idPhoto +".jpg");


        String pathCheck = Environment.getExternalStorageDirectory() + path + idPhoto +".jpg";
        File file = new File(pathCheck);
        if(file.exists()){
            Log.d("path",pathCheck);
           //Uri returnedFile = Uri.parse( file.getAbsolutePath().toString().trim());
             Log.d("pathn", file.getAbsolutePath());
            setAlreadyDownloadedFileUri(Uri.fromFile(file));
            Log.d("pathURI", alreadyDownloadedFileUri.toString());
            return true;
        }else{
            downloadReference = downloadManager.enqueue(request);
            Toast.makeText(gg, "Image will be downloaded. Check progress in notification bar", Toast.LENGTH_LONG).show();
           // Toast.makeText(gg, ghat, Toast.LENGTH_SHORT).show();
           // Log.d("path",ghat);

            Log.d("path",pathCheck);
          //  Toast.makeText(gg, pathCheck, Toast.LENGTH_SHORT).show();
/*            DownloadManager.Query ImageDownloadQuery = new DownloadManager.Query();
            ImageDownloadQuery.setFilterById(downloadReference);
            ImageDownloadQuery.setFilterByStatus(DownloadManager.ERROR_FILE_ALREADY_EXISTS);


            Cursor cursor = downloadManager.query(ImageDownloadQuery);
            cursor.moveToFirst();

            int filenameIndex = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_FILENAME);
            String fileUriString = cursor.getString(filenameIndex);*/
            return false;
        }






    }



    public Uri getAlreadyDownloadedFileUri() {
        return alreadyDownloadedFileUri;
    }

    public void setAlreadyDownloadedFileUri(Uri alreadyDownloadedFileUri) {
        this.alreadyDownloadedFileUri = alreadyDownloadedFileUri;
    }


    public long getDownloadReference(){
        return  downloadReference;
    }

    public DownloadManager getDownloadManager(){
        return downloadManager;
    }


    public void setImageStatus(Context gg,Cursor cursor){
        //Cursor cursor = downloadManager.query()
        int columnIndex = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);

        int columnReason = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON);
        int reason = cursor.getInt(columnReason);


        String statusText = "";
        String reasonText = "";

        switch (status){

            case DownloadManager.STATUS_FAILED:
                statusText = "STATUS_FAILED";
                switch(reason){
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonText = "ERROR_CANNOT_RESUME";
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonText = "ERROR_DEVICE_NOT_FOUND";
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonText = "ERROR_FILE_ALREADY_EXISTS";
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonText = "ERROR_FILE_ERROR";
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonText = "ERROR_HTTP_DATA_ERROR";
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonText = "ERROR_INSUFFICIENT_SPACE";
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonText = "ERROR_TOO_MANY_REDIRECTS";
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonText = "ERROR_UNHANDLED_HTTP_CODE";
                        break;
                    case DownloadManager.ERROR_UNKNOWN:
                        reasonText = "ERROR_UNKNOWN";
                        break;
                }
                break;
            case DownloadManager.STATUS_PAUSED:
                statusText = "STATUS_PAUSED";
                switch(reason){
                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                        reasonText = "PAUSED_QUEUED_FOR_WIFI";
                        break;
                    case DownloadManager.PAUSED_UNKNOWN:
                        reasonText = "PAUSED_UNKNOWN";
                        break;
                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                        reasonText = "PAUSED_WAITING_FOR_NETWORK";
                        break;
                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                        reasonText = "PAUSED_WAITING_TO_RETRY";
                        break;
                }
                break;
            case DownloadManager.STATUS_PENDING:
                statusText = "STATUS_PENDING";
                break;
            case DownloadManager.STATUS_RUNNING:
                statusText = "DOWNLOAD_STATUS_RUNNING";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                statusText = "STATUS_SUCCESSFUL";
                break;

        }

        if(statusText.equals("STATUS_FAILED")) {
            Toast.makeText(gg,reasonText , Toast.LENGTH_SHORT).show();
        }else if (statusText.equals("STATUS_PAUSED")){
            Toast.makeText(gg,reasonText , Toast.LENGTH_SHORT).show();
        }else if(statusText.equals("STATUS_PENDING")){
            Toast.makeText(gg,statusText , Toast.LENGTH_SHORT).show();
        }else if(statusText.equals("STATUS_RUNNING")){
            Toast.makeText(gg,statusText , Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(gg,statusText , Toast.LENGTH_SHORT).show();
        }
    }




}
