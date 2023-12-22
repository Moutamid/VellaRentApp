package com.moutimid.vellarentapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.activities.Home.PrivacyActivity;
import com.moutimid.vellarentapp.adapter.AllVillaAdapter;
import com.moutimid.vellarentapp.adapter.OwnVillaAdapter;
import com.moutimid.vellarentapp.helper.Config;
import com.moutimid.vellarentapp.helper.Constants;
import com.moutimid.vellarentapp.model.HouseRules;
import com.moutimid.vellarentapp.model.LocationModel;
import com.moutimid.vellarentapp.model.PropertyAmenities;
import com.moutimid.vellarentapp.model.Villa;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VillaFragment extends Fragment {

    RecyclerView content_rcv, content_rcv1;
    public List<Villa> productModelList = new ArrayList<>();
    AllVillaAdapter herbsAdapter;

    EditText search;
    TextView no_text;
    ImageView mic;
    String lcode = "en-US";
    ArrayList<LocationModel> userArrayList = new ArrayList<>();
    private static final double EARTH_RADIUS = 6371;
    OwnVillaAdapter ownVillaAdapter;
    ImageView privacy, filter;

    View bg;
    LinearLayout filter_layout;
    Button btnApplyFilters, btnApplyCancel;
    SeekBar seekBarPrice;
    TextView price_range;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_villa, container, false);
        price_range = view.findViewById(R.id.price_range);
        seekBarPrice = view.findViewById(R.id.seekBarPrice);
        bg = view.findViewById(R.id.bg);
        btnApplyFilters = view.findViewById(R.id.btnApplyFilters);
        btnApplyCancel = view.findViewById(R.id.btnApplyCancel);
        filter_layout = view.findViewById(R.id.filter_layout);
        privacy = view.findViewById(R.id.privacy);
        filter = view.findViewById(R.id.filter);
        content_rcv = view.findViewById(R.id.content_rcv);
        content_rcv1 = view.findViewById(R.id.content_rcv1);
        search = view.findViewById(R.id.search);
        no_text = view.findViewById(R.id.no_text);
        mic = view.findViewById(R.id.mic);
        content_rcv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        herbsAdapter = new AllVillaAdapter(getContext(), productModelList);
        content_rcv.setAdapter(herbsAdapter);
        content_rcv1.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        ownVillaAdapter = new OwnVillaAdapter(getContext(), productModelList);
        content_rcv1.setAdapter(ownVillaAdapter);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PrivacyActivity.class));
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bg.setVisibility(View.VISIBLE);
                filter_layout.setVisibility(View.VISIBLE);
            }
        });
        btnApplyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bg.setVisibility(View.GONE);
                filter_layout.setVisibility(View.GONE);
            }
        });
        btnApplyCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bg.setVisibility(View.GONE);
                filter_layout.setVisibility(View.GONE);
            }
        });
        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int progress = seekBar.getProgress();
                price_range.setText("Select Price Range ("+progress+"$)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PrivacyActivity.class));
            }
        });
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // if result is not empty
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            // get data and append it to editText
                            ArrayList<String> d = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                            search.setText(" " + d.get(0));
                        }
                    }
                });
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, lcode);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!");
                activityResultLauncher.launch(intent);
            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        if (Config.isNetworkAvailable(getContext())) {
            getProducts();
            getRecommendedProducts();
        } else {
            Toast.makeText(getContext(), "No network connection available.", Toast.LENGTH_SHORT).show();
        }


        // Define your current latitude and longitude
        double currentLat = 31.06171;
        double currentLng = 72.95896;

        // Create a list of places with their respective latitude and longitude
        List<Villa> places = new ArrayList<>();
        places.add(new Villa(31.06093, 72.95201, "Place 1"));
        places.add(new Villa(31.06160, 72.94746, "Place 2"));
        places.add(new Villa(31.05700, 72.95113, "Place 3"));
        // Add more places as needed

        // Calculate the distance between each place and the current location
        for (Villa place : places) {
            double distance = calculateDistance(currentLat, currentLng, place.getLat(), place.getLng());
            place.setDistance(distance);
        }

        // Sort the places based on distance in ascending order
        Collections.sort(places, Comparator.comparingDouble(Villa::getDistance));

        // Display the sorted places
        for (Villa place : places) {
            Log.d("dataaaa", place.getTitle() + " - Distance: " + new DecimalFormat("##.##").format(place.getDistance()) + " km");
        }

        return view;
    }


    private void getProducts() {
//        Config.showProgressDialog(getContext());
        Constants.databaseReference().child(Config.villa).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productModelList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Villa herbsModel = ds.getValue(Villa.class);

                    DataSnapshot propertyAmenities1 = ds.child("PropertyAmenities");
                    DataSnapshot houseRules1 = ds.child("HouseRules");
                    PropertyAmenities propertyAmenities = propertyAmenities1.getValue(PropertyAmenities.class);
                    HouseRules houseRules = houseRules1.getValue(HouseRules.class);
                    herbsModel.setPropertyAmenities(propertyAmenities);
//                  herbsModel.setBathroom(bathroom1);
                    herbsModel.setHouseRules(houseRules);
//                  Log.d("dataaa", herbsModel + "  io ");
//                  herbsModel.setPropertyDetails(propertyDetails);
//                  herbsModel.setHouseRules(houseRules);
                    productModelList.add(herbsModel);
                }
                herbsAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
//        Constants.databaseReference().child("Locations").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    Villa herbsModel1 = ds.getValue(Villa.class);
//                    userArrayList.add(new LocationModel(herbsModel1.getLat(), herbsModel1.getLng(), herbsModel1.getName()));
//                    Config.dismissProgressDialog();
//
//                }
//                Stash.put("Locations", userArrayList);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        Config.dismissProgressDialog();

    }

    private void getRecommendedProducts() {
//        Config.showProgressDialog(getContext());
        Constants.databaseReference().child(Config.villa).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productModelList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Villa herbsModel = ds.getValue(Villa.class);
                    Log.d("dataa", herbsModel.isBills_included() + "");

                    DataSnapshot propertyAmenities1 = ds.child("PropertyAmenities");
                    DataSnapshot houseRules1 = ds.child("HouseRules");
                    PropertyAmenities propertyAmenities = propertyAmenities1.getValue(PropertyAmenities.class);
                    HouseRules houseRules = houseRules1.getValue(HouseRules.class);
                    herbsModel.setPropertyAmenities(propertyAmenities);
//                  herbsModel.setBathroom(bathroom1);
                    herbsModel.setHouseRules(houseRules);
//                  Log.d("dataaa", herbsModel + "  io ");
//                  herbsModel.setPropertyDetails(propertyDetails);
//                  herbsModel.setHouseRules(houseRules);
                    productModelList.add(herbsModel);
                }
                ownVillaAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
//        Constants.databaseReference().child("Locations").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    Villa herbsModel1 = ds.getValue(Villa.class);
//                    userArrayList.add(new LocationModel(herbsModel1.getLat(), herbsModel1.getLng(), herbsModel1.getName()));
//                    Config.dismissProgressDialog();
//
//                }
//                Stash.put("Locations", userArrayList);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        Config.dismissProgressDialog();

    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Villa> filteredlist = new ArrayList<Villa>();

        // running a for loop to compare elements.
        for (Villa item : productModelList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            content_rcv.setVisibility(View.GONE);
            no_text.setVisibility(View.VISIBLE);
        } else {
            no_text.setVisibility(View.GONE);
            content_rcv.setVisibility(View.VISIBLE);

            // at last we are passing that filtered
            // list to our adapter class.
            herbsAdapter.filterList(filteredlist);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS * c;

        return distance;
    }

}