package com.example.atamerica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.atamerica.dbhandler.DataHelper;
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

        // Styling splash screen
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

        // request for current activity to allow internet connection
        // this must be in every activity/fragments
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        hashFunction = new AESUtil();

        new Handler().postDelayed(() -> {
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
                }
                else {
                    Intent intent = new Intent(MainActivity.this, AuthenticateActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            else {
                Intent intent = new Intent(MainActivity.this, AuthenticateActivity.class);
                startActivity(intent);
                finish();
            }

        }, splashScreenDuration);
    }
}