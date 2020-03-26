package com.example.marketonline.splashscreen;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.marketonline.R;
import com.example.marketonline.loginscreen.LoginScreen;
import com.example.marketonline.mainscreen.MainScreen;
import com.example.marketonline.utils.AppConstants;
import com.example.marketonline.utils.PermissionHelper;
import com.example.marketonline.utils.PreferencesUtils;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if (PermissionHelper.checkRuntimePermissions(this)) {
            startApp();
        }
    }

    private void startApp() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!PreferencesUtils
                        .getString(SplashScreen.this, AppConstants.USER_ID, AppConstants.NON_USER_ID)
                        .equals(AppConstants.NON_USER_ID)) {
                    startActivity(new Intent(SplashScreen.this, MainScreen.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashScreen.this, LoginScreen.class));
                    finish();
                }
            }
        }, AppConstants.SPLASH_TIME_OUT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConstants.COMMON_REQUEST_PERMISSION_CODE) {
            boolean permissionAllowed = true;
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //TODO Define a toast here
                    permissionAllowed = false;
                    finish();
                }
            }
            if (permissionAllowed) {
                startApp();
            }
        }
    }
}
