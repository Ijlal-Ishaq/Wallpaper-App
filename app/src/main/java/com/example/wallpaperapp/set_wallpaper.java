package com.example.wallpaperapp;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class set_wallpaper extends AsyncTask {


    private Context context;
    private ConstraintLayout cv;
    private String link;
    private String screen;

    public set_wallpaper(Context context, ConstraintLayout cv, String link,String screen) {
        this.context = context;
        this.cv = cv;
        this.link = link;
        this.screen=screen;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        try {


                java.net.URL url = new java.net.URL(link);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                set_wall(myBitmap);



        } catch (IOException e) {
            Toast.makeText(context, "Unsuccessful", Toast.LENGTH_LONG).show();
            cv.setVisibility(View.INVISIBLE);

        }
        return null;
    }

    private void set_wall(Bitmap bitmap) {

        WallpaperManager wallpaperManager =WallpaperManager.getInstance(context);
        try{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && bitmap!=null) {
//
                if(screen=="hs"){
                    wallpaperManager.setBitmap(bitmap);
                }else if(screen=="ls"){
                    wallpaperManager.setBitmap(bitmap,null,false,WallpaperManager.FLAG_LOCK);
                }else if(screen=="bh"){
                    wallpaperManager.setBitmap(bitmap);
                    wallpaperManager.setBitmap(bitmap,null,false,WallpaperManager.FLAG_LOCK);
                }

            }else {

             wallpaperManager.setBitmap(bitmap);

            }
        }catch (Exception e){

        }

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Wallpaper set Successfully", Toast.LENGTH_LONG).show();
                cv.setVisibility(View.INVISIBLE);
            }
        }, 100);

    }
}
