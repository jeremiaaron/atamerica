package com.example.atamerica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private int splashScreenDuration = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent home = new Intent(MainActivity.this, AuthenticateActivity.class);
                startActivity(home);
                finish();
            }
        }, splashScreenDuration);
    }
}