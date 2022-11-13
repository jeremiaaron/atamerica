package com.example.atamerica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import com.example.atamerica.java_class.DataHelper;
import com.example.atamerica.models.AppUserModel;

import java.util.Objects;

import JavaLibrary.AESUtil;

public class MainActivity extends AppCompatActivity {

    private AESUtil hashFunction;
    private int splashScreenDuration = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // request for current activity to allow internet connection
        // this must be in every activity/fragments
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        hashFunction = new AESUtil();

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.atamerica", Context.MODE_PRIVATE);
        boolean isRemember = sharedPreferences.getBoolean(getResources().getString(R.string.user_remember_hash), false);

        if (isRemember) {
            String encryptedEmail = sharedPreferences.getString(getResources().getString(R.string.user_email_hash), "");
            String encryptedEncryptedPassword = sharedPreferences.getString(getResources().getString(R.string.user_password_hash), "");

            String decryptedEmail = hashFunction.decrypt(encryptedEmail);
            String decryptedPassword = hashFunction.decrypt(encryptedEncryptedPassword);

            AppUserModel model = DataHelper.Query.ReturnAsObject("SELECT * FROM AppUser WHERE Email = ?; ", AppUserModel.class, new Object[] { decryptedEmail });
            if (model != null && Objects.equals(decryptedPassword, model.PasswordHash)) {
                Intent intent = new Intent(MainActivity.this, ParentActivity.class);
                startActivity(intent);
                finish();

                return;
            }
        }

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