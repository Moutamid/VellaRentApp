package com.moutimid.vellarentapp;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.Fragment.FavouriteFragment;
import com.moutimid.vellarentapp.Fragment.ProfileFragment;
import com.moutimid.vellarentapp.Fragment.VillaFragment;
import com.moutimid.vellarentapp.helper.Config;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;


public class MainActivity extends AppCompatActivity {

    SmoothBottomBar binding;

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
        binding = findViewById(R.id.bottomBar);
        Config.checkApp(MainActivity.this);
        replaceFragment(new VillaFragment());
        binding.setBackground(null);

        binding.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int item) {
                if (item == 2) {
                    replaceFragment(new ProfileFragment());
                } else if (item ==0) {
                    replaceFragment(new VillaFragment());
                } else if (item == 1) {
                    replaceFragment(new FavouriteFragment());
                }
                return true;
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


}