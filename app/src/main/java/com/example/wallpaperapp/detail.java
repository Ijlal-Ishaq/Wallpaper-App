package com.example.wallpaperapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.RunnableFuture;

public class detail extends AppCompatActivity {



    private ImageView iv;
    private wall_obj obj;
    private TextView att;
    private Button set_wall;
    private Button down;
    private Button down_chrome;
    private File file;
    private String dirPath, fileName;
    private ConstraintLayout cv;
    private ProgressDialog progressDialog;
    private TextView cvt;
    private TextView hs;
    private TextView ls;
    private TextView both;
    private TextView likes;
    private Dialog dialog;
    private InterstitialAd interstitialAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }


        interstitialAd=new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                interstitialAd.show();
            }
        });




        obj=getIntent().getParcelableExtra("link");

        cv=findViewById(R.id.cv);
        cvt=findViewById(R.id.textView6);

        likes=findViewById(R.id.textView5);

        iv=findViewById(R.id.imageView3);
        att=findViewById(R.id.textView4);
        set_wall=findViewById(R.id.button3);
        down_chrome=findViewById(R.id.button);

        Picasso.get().load(obj.getLink()).fit().centerCrop().into(iv);

        ViewCompat.setElevation(iv,9);

        att.setText("attribution: "+obj.getAtt());
        likes.setText("likes: "+obj.getLikes());




        dialog =new Dialog(detail.this);
        dialog.setContentView(R.layout.dialog);






        set_wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                dialog.show();



            }
        });
        down=findViewById(R.id.button4);

        final int requestCode=3;
        final String permission="";

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(detail.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE )
                        != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(detail.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
                }

                else if(ContextCompat.checkSelfPermission(detail.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE )
                        != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(detail.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                }else{

                down_wallpaper(obj.getDownload_link(),0);
                }
            }
        });




        AndroidNetworking.initialize(getApplicationContext());

        dirPath = Environment.getExternalStorageDirectory()+"/Wallpapers/";
        fileName = obj.getId().replace("-","").replace("_","")+".jpeg";
        file = new File(dirPath);




        down_chrome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getDownload_link()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setPackage("com.android.chrome");
                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    // Chrome is probably not installed
                    // Try with the default browser
                    i.setPackage(null);
                    startActivity(i);
                }
            }
        });




    }

    private void down_wallpaper(final String link, final int i) {




            cv.setVisibility(View.VISIBLE);
            cvt.setText("Downloading...");




        if (!file.exists()) {
            file.mkdirs();
        }




        AndroidNetworking.download(link, dirPath, fileName)
                .build()
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                        if(i!=1){
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(detail.this, "Download Successful", Toast.LENGTH_LONG).show();
                                cv.setVisibility(View.INVISIBLE);
                            }
                        }, 500);
                        }

                        MediaScannerConnection.scanFile(detail.this, new String[] { dirPath+fileName},
                                null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {

                                    }
                                });




                    }

                    @Override
                    public void onError(ANError anError) {


                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(detail.this, "Download Unsuccessful", Toast.LENGTH_LONG).show();
                                    cv.setVisibility(View.INVISIBLE);
                                }
                            }, 100);


                    }
                });



    }



    public void hs(View view) {
        dialog.dismiss();
        cvt.setText("Setting Wallpaper...");
        cv.setVisibility(View.VISIBLE);
        new set_wallpaper(detail.this,cv,obj.getDownload_link(),"hs").execute(obj.getDownload_link());

    }

    public void ls(View view) {
        dialog.dismiss();
        cvt.setText("Setting Wallpaper...");
        cv.setVisibility(View.VISIBLE);
        new set_wallpaper(detail.this,cv,obj.getDownload_link(),"ls").execute(obj.getDownload_link());
    }

    public void both(View view) {
        dialog.dismiss();
        cvt.setText("Setting Wallpaper...");
        cv.setVisibility(View.VISIBLE);
        new set_wallpaper(detail.this,cv,obj.getDownload_link(),"bh").execute(obj.getDownload_link());
    }
}




