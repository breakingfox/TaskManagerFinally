package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_OUT_TIME = 4000;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        img = findViewById(R.id.imageView3);
       // img.setImageResource(R.drawable.);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_OUT_TIME);
    }
}
