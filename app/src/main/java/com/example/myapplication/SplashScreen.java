package com.example.myapplication;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class SplashScreen extends AppCompatActivity {
    //Приветственный экран приложения
    private static int SPLASH_OUT_TIME = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int[] images = {R.drawable.splash_screen,R.drawable.splash_screen_1,R.drawable.splash_screen_2,R.drawable.splash_screen_3};
        Random random = new Random(System.currentTimeMillis());
        int posOfImage = random.nextInt(images.length - 1);
        ImageView imageView = (ImageView) findViewById(R.id.imageView3);
        imageView.setImageResource(images[posOfImage]);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_OUT_TIME);
    }
}
