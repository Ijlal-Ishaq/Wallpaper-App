package com.example.wallpaperapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.content.ContextCompat;
import androidx.core.database.DatabaseUtilsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.FirebaseAppLifecycleListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    
    RecyclerView recyclerView;
    recycler_view adp;
    ProgressBar progressBar;
    private List<wall_obj> imagesList = new ArrayList<>();
    private AdView adView;
    static ConstraintLayout mag ;
    static ImageView mag_iv ;
    DatabaseReference fref;

    public static ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }

        fref=FirebaseDatabase.getInstance().getReference().child("wallpapers");

        mag=findViewById(R.id.mag);
        mag_iv=findViewById(R.id.imageView2);
        progressBar=findViewById(R.id.progressBar);

        adView=findViewById(R.id.adView);


        adView.loadAd(new AdRequest.Builder().build());



        pb=findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);

        adp=new recycler_view(this,imagesList,progressBar);



        recyclerView=findViewById(R.id.r_view);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adp);


        fref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                try {

                    wall_obj obj = new wall_obj(
                            dataSnapshot.child("link").getValue(String.class),
                            dataSnapshot.child("att").getValue(String.class),
                            dataSnapshot.child("likes").getValue(String.class),
                            dataSnapshot.child("id").getValue(String.class),
                            dataSnapshot.child("download_link").getValue(String.class)
                    );
                    imagesList.add(0, obj);

                    pb.setVisibility(View.INVISIBLE);
                    adp.notifyDataSetChanged();
                }catch(Exception e){

                }

                }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });









    }


    public void upload(View view) {
        Intent i =new Intent(MainActivity.this,upload.class);
        startActivity(i);
    }


    private int back_press_count=0;

    @Override
    public void onBackPressed() {


        Handler handler =new Handler();

        if(back_press_count>0){
            MainActivity.super.onBackPressed();
        }else{
            back_press_count++;
            Toast.makeText(this,"Press Again to Exit App",Toast.LENGTH_SHORT).show();
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                back_press_count=0;

            }
        },1300);



    }
}

