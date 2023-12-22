package com.moutimid.vellarentapp.activities.Authentication;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.MainActivity;
import com.moutimid.vellarentapp.activities.Authentication.LoginActivity;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

// Set the status bar background color to white
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.WHITE);
        }
        setContentView(R.layout.activity_splash);

        int splashInterval = 3000;
        ImageView imageView = findViewById(R.id.logo);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                imageView,
                PropertyValuesHolder.ofFloat("scaleX", 3.9F),

                PropertyValuesHolder.ofFloat("scaleY", 3.9F)
        );
        objectAnimator.setDuration(2000);
        objectAnimator.start();
        new Handler().postDelayed(this::goToApp, splashInterval);
    }

    public void goToApp() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//        Intent mainIntent = new Intent(SplashActivity.this, HomePage.class);
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
        else
        {
            Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }
}