package com.example.atamerica;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.atamerica.dbhandler.DataHelper;
import com.example.atamerica.taskhandler.TaskRunner;
import com.mysql.jdbc.StringUtils;

import java.util.Objects;
import java.util.concurrent.Callable;

import JavaLibrary.AESUtil;

public class MainActivity extends AppCompatActivity {

    private class AuthenticateTask implements Callable<Boolean> {

        @Override
        public Boolean call() {
            AESUtil hash = new AESUtil();

            try {
                SharedPreferences settings = getSharedPreferences("com.example.atamerica", Context.MODE_PRIVATE);
                boolean remember = settings.getBoolean(getResources().getString(R.string.user_remember_hash), false);

                if (remember) {
                    String encryptedEmail = settings.getString(getResources().getString(R.string.user_email_hash), "");
                    String encryptedEncryptedPassword = settings.getString(getResources().getString(R.string.user_password_hash), "");

                    if (!StringUtils.isNullOrEmpty(encryptedEmail) && !StringUtils.isNullOrEmpty(encryptedEncryptedPassword)) {
                        String email = hash.decrypt(encryptedEmail);
                        String passwordHash = hash.decrypt(encryptedEncryptedPassword);

                        String userPasswordHash = DataHelper.Query.ReturnAsString("SELECT PasswordHash FROM AppUser WHERE Email = ?; ", new Object[] { email });
                        return (!StringUtils.isNullOrEmpty(userPasswordHash) && Objects.equals(passwordHash, userPasswordHash));
                    }
                }
            }
            catch (Exception e) {
                Log.e("ERROR", "Error initializing Account Manager.");
                e.printStackTrace();
            }

            return false;
        }
    }

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

        new Handler().postDelayed(() -> new TaskRunner().executeAsyncPool(new AuthenticateTask(), (data) -> {
            Intent intent;
            if (data) {
                intent = new Intent(MainActivity.this, ParentActivity.class);
            }
            else {
                intent = new Intent(MainActivity.this, AuthenticateActivity.class);
            }
            startActivity(intent);
            finish();
        }), 500);
    }
}