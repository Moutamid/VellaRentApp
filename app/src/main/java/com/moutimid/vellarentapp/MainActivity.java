package com.moutimid.vellarentapp;


import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.Fragment.FavouriteFragment;
import com.moutimid.vellarentapp.Fragment.ProfileFragment;
import com.moutimid.vellarentapp.Fragment.VillaFragment;
import com.moutimid.vellarentapp.helper.Config;
import com.moutimid.vellarentapp.helper.GPSTracker;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;


public class MainActivity extends AppCompatActivity {

    SmoothBottomBar binding;
    private GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

// Set the status bar background color to white
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.WHITE);
        }
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                getLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding = findViewById(R.id.bottomBar);
        Config.checkApp(MainActivity.this);
        replaceFragment(new VillaFragment());
        binding.setBackground(null);
        getLocation();
        binding.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int item) {
                if (item == 2) {
                    replaceFragment(new ProfileFragment());
                } else if (item == 0) {
                    replaceFragment(new VillaFragment());
                } else if (item == 1) {
                    replaceFragment(new FavouriteFragment());
                }
                return true;
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            getLocation();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void getLocation() {
        gpsTracker = new GPSTracker(MainActivity.this);
        if (gpsTracker.canGetLocation()) {
            Config.lat = gpsTracker.getLatitude();
            Config.lng = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

}