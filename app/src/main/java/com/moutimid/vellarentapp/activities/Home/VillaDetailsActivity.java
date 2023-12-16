package com.moutimid.vellarentapp.activities.Home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.dailogues.CalenderDialogClass;
import com.moutimid.vellarentapp.helper.Config;
import com.moutimid.vellarentapp.model.Villa;

import java.util.ArrayList;

public class VillaDetailsActivity extends AppCompatActivity {
    Villa villaModel;
    TextView map, availabillity;
    ImageView favourite_img, unfavourite_img, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_villa_details);
        availabillity = findViewById(R.id.availabillity);
        image = findViewById(R.id.image);
        map = findViewById(R.id.map);
        unfavourite_img = findViewById(R.id.unfavourite);
        favourite_img = findViewById(R.id.favourite);
        availabillity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalenderDialogClass cdd = new CalenderDialogClass(getApplicationContext());
                cdd.show();
            }
        });
        Glide.with(VillaDetailsActivity.this).load("https://firebasestorage.googleapis.com/v0/b/childfr-35a43.appspot.com/o/villa.jpg?alt=media&token=b805f476-128e-4204-a870-edbbfca0f854").into(image);
        villaModel = (Villa) Stash.getObject(Config.currentModel, Villa.class);
        ArrayList<Villa> villaModels = Stash.getArrayList(Config.favourite, Villa.class);
        if (villaModels != null) {
            for (int i = 0; i < villaModels.size(); i++) {

                if (villaModel.getKey().equals(villaModels.get(i).getKey())) {
                    unfavourite_img.setVisibility(View.VISIBLE);

                } else {
                    favourite_img.setVisibility(View.VISIBLE);

                }

            }
        }
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("latlng", villaModel.getLat() + "   " + villaModel.getLng());

                if (villaModel.getLat() > -90 && villaModel.getLat() < 90 && villaModel.getLng() > -180 && villaModel.getLng() < 180) {
                    Intent intent = new Intent(VillaDetailsActivity.this, MapActivity.class);
                    intent.putExtra("lat", villaModel.getLat());
                    intent.putExtra("lng", villaModel.getLng());
                    intent.putExtra("name", villaModel.getName());
                    startActivity(intent);

                } else {
                    Toast.makeText(VillaDetailsActivity.this, "Invalid Coordinates to show marker", Toast.LENGTH_SHORT).show();
                }

            }
        });
        favourite_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ArrayList<Villa> villaModelArrayList = Stash.getArrayList(Config.favourite, Villa.class);
                villaModelArrayList.add(villaModel);
                Stash.put(Config.favourite, villaModelArrayList);
                unfavourite_img.setVisibility(View.VISIBLE);
                favourite_img.setVisibility(View.GONE);
            }
        });
        unfavourite_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Villa> villaArrayList = Stash.getArrayList(Config.favourite, Villa.class);
                for (int i = 0; i < villaArrayList.size(); i++) {
                    if (villaArrayList.get(i).getKey().equals(villaModel.getKey())) {
                        villaArrayList.remove(i);
                    }
                }
                Stash.put(Config.favourite, villaArrayList);
                unfavourite_img.setVisibility(View.GONE);
                favourite_img.setVisibility(View.VISIBLE);
            }
        });
    }

    public void onBack(View view) {
        onBackPressed();
    }
}