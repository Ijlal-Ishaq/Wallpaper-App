package com.example.wallpaperapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class recycler_view extends RecyclerView.Adapter<recycler_view.viewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    static ProgressBar progressBar;
    private static DatabaseReference fref1= FirebaseDatabase.getInstance().getReference().child("wallpapers");




    List<wall_obj> imagesList;


    public recycler_view(Context context, List<wall_obj> imagesList,ProgressBar progressBar) {

        mInflater = LayoutInflater.from(context);
        this.imagesList = imagesList;
        this.context=context;
        this.progressBar= progressBar;




    }



    @NonNull
    @Override
    public recycler_view.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.items,
                parent, false);
        return new viewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final recycler_view.viewHolder holder, final int position) {

        final String mCurrent = imagesList.get(position).getLink();

//        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
//        int height = Resources.getSystem().getDisplayMetrics().heightPixels;

        final viewHolder myholder =holder;

        Picasso.get().load(mCurrent).fit().centerCrop().into(holder.img);



        holder.img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                MainActivity.mag.setVisibility(View.VISIBLE);



               Picasso.get().load(imagesList.get(position).getLink()).fit().centerCrop().into(MainActivity.mag_iv);
            //    Picasso.get().load(imagesList.get(position).getDownload_link()).fit().centerCrop().into(MainActivity.mag_iv);

//                MainActivity.mag.setVisibility(View.INVISIBLE);
                return false;

            }
        });

        holder.img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MainActivity.mag.getVisibility()==View.VISIBLE) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    int action = event.getActionMasked();
                    if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        MainActivity.mag.setVisibility(View.INVISIBLE);
                        return true;
                    }
                }
                return false;
            }
        });














        final int[] like_count = {0};

        holder.btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Animation ani = AnimationUtils.loadAnimation(context,R.anim.fadeinout);
                holder.likesv.setVisibility(View.VISIBLE);
                holder.likesv.startAnimation(ani);
                final Handler handler=new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.likesv.setVisibility(View.INVISIBLE);
                    }
                },900);
//

                if(like_count[0] ==0) {
                    like_count[0]++;

                    int Likes_s=Integer.parseInt(imagesList.get(position).getLikes());
                    Likes_s++;
                    fref1.child(imagesList.get(position).getId()).child("likes").setValue(String.valueOf(Likes_s));





                }else {
                    Toast.makeText(context, "Already Liked", Toast.LENGTH_SHORT).show();
                }




            }
        });


        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(context,detail.class);
                i.putExtra("link",imagesList.get(position));
                context.startActivity(i);
            }
        });





    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

class viewHolder extends RecyclerView.ViewHolder {
    ImageView img;
    ImageView img2;



    ImageView btn5;


    ImageView likes;

    ImageView likesv;



    public viewHolder(@NonNull View itemView) {
        super(itemView);
        this.img = (ImageView)itemView.findViewById(R.id.imageView);
        this.img2 = (ImageView)itemView.findViewById(R.id.like);
        this.likesv=itemView.findViewById(R.id.tvlikes);


        this.btn5 = itemView.findViewById(R.id.button5);

        this.likes=itemView.findViewById(R.id.tvlikes);





    }











}
}