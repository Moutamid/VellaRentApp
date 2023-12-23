package com.moutimid.vellarentapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class VillaFragment extends Fragment {

    RecyclerView content_rcv, content_rcv1, search_rcv;
    public List<Villa> nearbyList = new ArrayList<>();
    public List<Villa> productModelList = new ArrayList<>();
    public List<String> country = new ArrayList<>();
    public List<String> town = new ArrayList<>();
    String[] stringArray;

    AllVillaAdapter villaAdapter;

    EditText search;
    TextView no_text;
    ImageView mic;
    String lcode = "en-US";
    ArrayList<LocationModel> userArrayList = new ArrayList<>();
    private static final double EARTH_RADIUS = 6371;
    OwnVillaAdapter ownVillaAdapter, ownVillaAdapter1;
    ImageView privacy, filter;

    View bg;
    LinearLayout filter_layout;
    Button btnApplyFilters, btnApplyCancel;
    SeekBar seekBarPrice;
    TextView text_town, nearby_title, recommanded_title;
    LottieAnimationView loading;
    CircleImageView circularImage;
    TextView title;
    CheckBox check_box;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_villa, container, false);
        text_town = view.findViewById(R.id.text_town);
        check_box = view.findViewById(R.id.check_box);
        circularImage = view.findViewById(R.id.circularImage);
        title = view.findViewById(R.id.title);
        loading = view.findViewById(R.id.loading);
        recommanded_title = view.findViewById(R.id.recommanded_title);
        nearby_title = view.findViewById(R.id.nearby_title);
        bg = view.findViewById(R.id.bg);
        btnApplyFilters = view.findViewById(R.id.btnApplyFilters);
        btnApplyCancel = view.findViewById(R.id.btnApplyCancel);
        filter_layout = view.findViewById(R.id.filter_layout);
        privacy = view.findViewById(R.id.privacy);
        filter = view.findViewById(R.id.filter);
        content_rcv = view.findViewById(R.id.content_rcv);
        content_rcv1 = view.findViewById(R.id.content_rcv1);
        search_rcv = view.findViewById(R.id.search_rcv);
        search = view.findViewById(R.id.search);
        no_text = view.findViewById(R.id.no_text);
        mic = view.findViewById(R.id.mic);
        content_rcv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        villaAdapter = new AllVillaAdapter(getContext(), nearbyList);
        content_rcv.setAdapter(villaAdapter);
        content_rcv1.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        ownVillaAdapter = new OwnVillaAdapter(getContext(), productModelList);
        content_rcv1.setAdapter(ownVillaAdapter);
        search_rcv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        ownVillaAdapter1 = new OwnVillaAdapter(getContext(), productModelList);
        search_rcv.setAdapter(ownVillaAdapter1);
        title.setText("Hi, \n" + Stash.getString("username"));
        Glide.with(getContext()).load(Stash.getString("userimage")).into(circularImage);
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (check_box.isChecked()) {
                    try {

                        LocalDate currentDate = LocalDate.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String formattedDate = currentDate.format(formatter);
                        filter_dates(formattedDate);
                    } catch (Exception e) {
                    }
                } else {
                    getProducts();
                    getRecommendedProducts();
                    recommanded_title.setVisibility(View.VISIBLE);
                    content_rcv1.setVisibility(View.VISIBLE);
                    no_text.setVisibility(View.GONE);
                    search_rcv.setVisibility(View.GONE);

                }

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, country);
        Spinner spinner = view.findViewById(R.id.spinner);
        spinner.setSelection(0);
        spinner.setAdapter(adapter);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, town);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner1 = view.findViewById(R.id.spinner_town);
        spinner1.setAdapter(adapter1);
        spinner1.setSelection(0);
        adapter1.notifyDataSetChanged();
// Set the item selection listener for the spinner

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                // Handle the selected item here
                String selectedItem = (String) parent.getItemAtPosition(position);
                Toast.makeText(getContext(), "" + selectedItem, Toast.LENGTH_SHORT).show();
                // Do something with the selected item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
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
                if (charSequence.length() == 0) {
                    getProducts();
                    recommanded_title.setVisibility(View.VISIBLE);
                    content_rcv1.setVisibility(View.VISIBLE);
                    search_rcv.setVisibility(View.GONE);
                    no_text.setVisibility(View.GONE);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
                if (editable.length() == 0) {
                    getProducts();
                    recommanded_title.setVisibility(View.VISIBLE);
                    content_rcv1.setVisibility(View.VISIBLE);
                    search_rcv.setVisibility(View.GONE);
                    no_text.setVisibility(View.GONE);
                }
            }
        });

        if (Config.isNetworkAvailable(getContext())) {
            getProducts();
            getRecommendedProducts();
        } else {
            Toast.makeText(getContext(), "No network connection available.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }


    private void getProducts() {
        Constants.databaseReference().child(Config.villa).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nearbyList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Villa villaModel = ds.getValue(Villa.class);
                    if (villaModel.verified) {
                        DataSnapshot propertyAmenities1 = ds.child("PropertyAmenities");
                        DataSnapshot houseRules1 = ds.child("HouseRules");
                        PropertyAmenities propertyAmenities = propertyAmenities1.getValue(PropertyAmenities.class);
                        HouseRules houseRules = houseRules1.getValue(HouseRules.class);
                        villaModel.setPropertyAmenities(propertyAmenities);
                        villaModel.setHouseRules(houseRules);
                        double distance = calculateDistance(Config.lat, Config.lng, villaModel.getLat(), villaModel.getLng());
                        villaModel.setDistance(distance);
                        DataSnapshot imagesSnapshot = ds.child("images");
                        Map<String, String> imagesMap = new HashMap<>();
                        for (DataSnapshot imageSnapshot : imagesSnapshot.getChildren()) {
                            String imageKey = imageSnapshot.getKey();
                            String imageUrl = imageSnapshot.getValue(String.class);
                            imagesMap.put(imageKey, imageUrl);
                        }

                        villaModel.setImages(imagesMap);
                        nearbyList.add(villaModel);
                        Log.d("distance", distance + "");
                        if (distance < 300) {
                            Log.d("distance", "entered" + distance);
                        }
                    }
                    Collections.sort(nearbyList, Comparator.comparingDouble(Villa::getDistance));
                    villaAdapter = new AllVillaAdapter(getContext(), nearbyList);
                    loading.setVisibility(View.GONE);
                    if (nearbyList.size() > 0) {
                        nearby_title.setVisibility(View.VISIBLE);
                        content_rcv.setVisibility(View.VISIBLE);

                    }

                    villaAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getRecommendedProducts() {
//        Config.showProgressDialog(getContext());
        Constants.databaseReference().child(Config.villa).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productModelList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Villa villaModel = ds.getValue(Villa.class);
                    Log.d("dataa", villaModel.isBills_included() + "");
                    if (villaModel.verified) {
                        DataSnapshot propertyAmenities1 = ds.child("PropertyAmenities");
                        DataSnapshot houseRules1 = ds.child("HouseRules");
                        PropertyAmenities propertyAmenities = propertyAmenities1.getValue(PropertyAmenities.class);
                        HouseRules houseRules = houseRules1.getValue(HouseRules.class);
                        villaModel.setPropertyAmenities(propertyAmenities);
                        villaModel.setHouseRules(houseRules);
                        DataSnapshot imagesSnapshot = ds.child("images");
                        country.add(villaModel.city_name);
                        town.add(villaModel.town_name);
                        Map<String, String> imagesMap = new HashMap<>();
                        for (DataSnapshot imageSnapshot : imagesSnapshot.getChildren()) {
                            String imageKey = imageSnapshot.getKey();
                            String imageUrl = imageSnapshot.getValue(String.class);
                            imagesMap.put(imageKey, imageUrl);
                        }

                        villaModel.setImages(imagesMap);
                        productModelList.add(villaModel);
                        recommanded_title.setVisibility(View.VISIBLE);
                        text_town.setText(villaModel.town_name.toString());

                    }
//                    stringArray = new String[productModelList.size()];
//                    for (int i = 0; i <= productModelList.size(); i++) {
//                        stringArray[i] = productModelList.get(i).town_name;
//
//                    }

                    ownVillaAdapter.notifyDataSetChanged();
                    ownVillaAdapter1.notifyDataSetChanged();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


    }

    private void filter(String text) {
        ArrayList<Villa> filteredlist = new ArrayList<Villa>();
        for (Villa item : productModelList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
            else  if (item.town_name.contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
            else  if (item.city_name.contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
            else if (item.getTitle().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            nearby_title.setVisibility(View.GONE);
            recommanded_title.setVisibility(View.GONE);
            search_rcv.setVisibility(View.GONE);
            content_rcv.setVisibility(View.GONE);
            content_rcv1.setVisibility(View.GONE);
            no_text.setVisibility(View.VISIBLE);
        } else {

            nearby_title.setVisibility(View.GONE);
            recommanded_title.setVisibility(View.GONE);
            content_rcv.setVisibility(View.GONE);
            content_rcv1.setVisibility(View.GONE);
            no_text.setVisibility(View.GONE);
            search_rcv.setVisibility(View.VISIBLE);

            // at last we are passing that filtered
            // list to our adapter class.
            ownVillaAdapter1.filterList(filteredlist);
        }
    }

    private void filter_dates(String text) {
        ArrayList<Villa> filteredlist = new ArrayList<Villa>();
        for (Villa item : productModelList) {
            if (item.available_dates.contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            nearby_title.setVisibility(View.GONE);
            recommanded_title.setVisibility(View.GONE);
            search_rcv.setVisibility(View.GONE);
            content_rcv.setVisibility(View.GONE);
            content_rcv1.setVisibility(View.GONE);
            no_text.setVisibility(View.VISIBLE);
        } else {
            nearby_title.setVisibility(View.GONE);
            recommanded_title.setVisibility(View.GONE);
            content_rcv.setVisibility(View.GONE);
            content_rcv1.setVisibility(View.GONE);
            no_text.setVisibility(View.GONE);
            search_rcv.setVisibility(View.VISIBLE);

            // at last we are passing that filtered
            // list to our adapter class.
            ownVillaAdapter1.filterList(filteredlist);
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