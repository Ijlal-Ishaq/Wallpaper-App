package com.example.wallpaperapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class upload extends AppCompatActivity {



    private EditText et_link;
    private EditText et_att;
    private EditText et_att2;

    private Button btn_up;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);




        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }





        et_link=findViewById(R.id.et_link);
        et_att=findViewById(R.id.et_att);
        et_att2=findViewById(R.id.et_att2);

        btn_up=findViewById(R.id.btn_up);





        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                wall_obj obj=new wall_obj(et_link.getText().toString().trim(),et_att.getText().toString().trim(),"0","",et_att2.getText().toString().trim());

                DatabaseReference fref= FirebaseDatabase.getInstance().getReference().child("wallpapers");

                String key=fref.push().getKey();

                obj.setId(key);

                fref.child(key).setValue(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent i =new Intent(upload.this,upload.class);
                        startActivity(i);
                        finish();
                    }
                });



            }
        });











    }
}