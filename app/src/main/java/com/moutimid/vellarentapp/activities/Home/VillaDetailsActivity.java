package com.moutimid.vellarentapp.activities.Home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.dailogues.CalenderDialogClass;
import com.moutimid.vellarentapp.helper.Config;
import com.moutimid.vellarentapp.model.UserModel;
import com.moutimid.vellarentapp.model.Villa;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VillaDetailsActivity extends AppCompatActivity {
    Villa villaModel;
    TextView map;
    ImageView favourite_img, unfavourite_img, image;
    String token_admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_villa_details);
        image = findViewById(R.id.image);
        map = findViewById(R.id.map);
        unfavourite_img = findViewById(R.id.unfavourite);
        favourite_img = findViewById(R.id.favourite);
        villaModel = (Villa) Stash.getObject(Config.currentModel, Villa.class);
        image = findViewById(R.id.image);
        map = findViewById(R.id.map);
        unfavourite_img = findViewById(R.id.unfavourite);
        favourite_img = findViewById(R.id.favourite);
        // Assign IDs to views
        Toolbar toolbar = findViewById(R.id.toolbar_);
        ImageView backpress = findViewById(R.id.backpress);
        TextView title = findViewById(R.id.title);
        ImageView image = findViewById(R.id.image);
        TextView name = findViewById(R.id.name);
        TextView price = findViewById(R.id.price);
        name.setText(villaModel.getName());
        price.setText(villaModel.getBill() + " $/month");
        Glide.with(VillaDetailsActivity.this).load(villaModel.getImage()).into(image);
        LinearLayout roomTypeLayout = findViewById(R.id.room_type_layout);
        TextView roomType = findViewById(R.id.room_type);
        LinearLayout roomAreaLayout = findViewById(R.id.room_area_layout);
        TextView roomArea = findViewById(R.id.room_area);
        roomArea.setText(villaModel.getArea() + "m²");
        LinearLayout noOfBedroomLayout = findViewById(R.id.no_of_bedroom_layout);
        TextView noOfBedroom = findViewById(R.id.no_of_bedroom);
        noOfBedroom.setText(villaModel.getBedroom() + "");
        LinearLayout noOfBathroomLayout = findViewById(R.id.no_of_bathroom_layout);
        TextView noOfBathroom = findViewById(R.id.no_of_bathroom);
        noOfBathroom.setText(villaModel.getBathRoom() + "");
        TextView descriptionTitle = findViewById(R.id.description_title);
        TextView description = findViewById(R.id.description);
        description.setText(villaModel.getDescription());
        TextView availability = findViewById(R.id.availability);
        TextView billIncluded = findViewById(R.id.bill_included);
        TextView house_rules = findViewById(R.id.house_rules);
        TextView pet_friendly = findViewById(R.id.pet_friendly);
        TextView smoke_friendly = findViewById(R.id.smoke_friendly);
        if (villaModel.getHouseRules() != null) {
            house_rules.setVisibility(View.VISIBLE);
        } else {
            house_rules.setVisibility(View.GONE);
        }
        if (villaModel.getHouseRules().isPetFriendly()) {
            pet_friendly.setVisibility(View.VISIBLE);
        } else {
            pet_friendly.setVisibility(View.GONE);
        }
        if (villaModel.getHouseRules().isSmokerFriendly()) {
            smoke_friendly.setVisibility(View.VISIBLE);
        } else {
            smoke_friendly.setVisibility(View.GONE);
        }
        if (villaModel.isBills_included()) {
            billIncluded.setText("Included");
        } else {
            billIncluded.setText("Not Included");
        }
        TextView propertyAmenitiesTitle = findViewById(R.id.property_amenities_title);
        LinearLayout dryerLayout = findViewById(R.id.dryer_layout);
        LinearLayout furnishedLayout = findViewById(R.id.furnished_layout);
        LinearLayout equippedKitchenLayout = findViewById(R.id.equipped_kitchen_layout);
        LinearLayout gardenLayout = findViewById(R.id.garden_layout);
        LinearLayout heaterLayout = findViewById(R.id.heater_layout);
        LinearLayout tvLayout = findViewById(R.id.tv_layout);
        LinearLayout wifiLayout = findViewById(R.id.wifi_layout);
        LinearLayout machine_layout = findViewById(R.id.machine_layout);
        LinearLayout parking_layout = findViewById(R.id.parking_layout);
        LinearLayout air_layout = findViewById(R.id.air_layout);
        TextView priceTitle = findViewById(R.id.price_title);
        TextView monthlyRent = findViewById(R.id.monthly_rent);
        monthlyRent.setText(villaModel.getBill() + " $/month");
        TextView bills = findViewById(R.id.bills);
        TextView included = findViewById(R.id.included);
        TextView location = findViewById(R.id.location);
        if (villaModel.isBills_included()) {
            included.setText("Included");
        } else {
            included.setText("Not Included");
        }

        location.setText(villaModel.getTitle());
// Request for Rent button
        Button requestButton = findViewById(R.id.request_button);

        if (villaModel.getPropertyAmenities() != null) {
            propertyAmenitiesTitle.setVisibility(View.VISIBLE);
        } else {
            propertyAmenitiesTitle.setVisibility(View.GONE);

        }
        if (villaModel.getPropertyAmenities().isDryer()) {
            dryerLayout.setVisibility(View.VISIBLE);
        } else {
            dryerLayout.setVisibility(View.GONE);
        }
        if (villaModel.getPropertyAmenities().isFurnished()) {
            furnishedLayout.setVisibility(View.VISIBLE);
        } else {
            furnishedLayout.setVisibility(View.GONE);
        }
        if (villaModel.getPropertyAmenities().isGarden()) {
            gardenLayout.setVisibility(View.VISIBLE);
        } else {
            gardenLayout.setVisibility(View.GONE);
        }
        if (villaModel.getPropertyAmenities().isEquippedKitchen()) {
            equippedKitchenLayout.setVisibility(View.VISIBLE);
        } else {
            equippedKitchenLayout.setVisibility(View.GONE);
        }
        if (villaModel.getPropertyAmenities().isHeating()) {
            heaterLayout.setVisibility(View.VISIBLE);
        } else {
            heaterLayout.setVisibility(View.GONE);
        }
        if (villaModel.getPropertyAmenities().isWifi()) {
            wifiLayout.setVisibility(View.VISIBLE);
        } else {
            wifiLayout.setVisibility(View.GONE);
        }
        if (villaModel.getPropertyAmenities().isTv()) {
            tvLayout.setVisibility(View.VISIBLE);
        } else {
            tvLayout.setVisibility(View.GONE);
        }
        if (villaModel.getPropertyAmenities().isWashingMachine()) {
            machine_layout.setVisibility(View.VISIBLE);
        } else {
            machine_layout.setVisibility(View.GONE);
        }
        if (villaModel.getPropertyAmenities().isParking()) {
            parking_layout.setVisibility(View.VISIBLE);
        } else {
            parking_layout.setVisibility(View.GONE);
        }
        if (villaModel.getPropertyAmenities().isAirConditioning()) {
            air_layout.setVisibility(View.VISIBLE);
        } else {
            air_layout.setVisibility(View.GONE);
        }

        availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalenderDialogClass cdd = new CalenderDialogClass(VillaDetailsActivity.this);
                cdd.show();
            }
        });
        DatabaseReference z = FirebaseDatabase.getInstance().getReference().child("RentApp").child("Admin");
        z.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get the value of the "token" field
                token_admin = dataSnapshot.child("token").getValue().toString();

                // Use the token as needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to read value.", databaseError.toException());
            }
        });
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

    public void booking(View view) {
        sendFCMPush(token_admin);
    }

    private void sendFCMPush(String token) {
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();

        try {
            UserModel userModel = (UserModel) Stash.getObject("UserDetails", UserModel.class);

            notifcationBody.put("title", "Booking Alert");
            notifcationBody.put("message", userModel.name+ " wants to book this Villa");
            notification.put("to", token); // Use the FCM token of the recipient device
            notification.put("data", notifcationBody);

            Log.e("DATAAAAAA", notification.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.POST,
                Config.NOTIFICATIONAPIURL,
                notification,
                response -> {
                    Log.e("True", response + "");
                    Log.d("Response", response.toString());
                },
                error -> {
                    Log.e("False", error + "");
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "key=" + Config.SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(VillaDetailsActivity.this);
        int socketTimeout = 1000 * 60; // 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }

}