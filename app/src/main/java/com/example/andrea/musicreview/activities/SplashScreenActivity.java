package com.example.andrea.musicreview.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.utility.MyLoginManager;
import com.facebook.FacebookSdk;

public class SplashScreenActivity extends Activity {

    private final int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!FacebookSdk.isInitialized()){
            FacebookSdk.sdkInitialize(getApplicationContext());
        }
        setContentView(R.layout.splash_screen);
        if((new MyLoginManager(getApplicationContext()).getCurrentLoginService()).equals(MyLoginManager.EMAIL_LOGIN)){
            Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        sleep(SPLASH_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        launchLoginOrMain();
                    }
                }
            };
            timerThread.start();
        }
    }

    private void launchLoginOrMain(){
        if(new MyLoginManager(getApplicationContext()).getCurrentLoginService().equals(MyLoginManager.NOT_LOGGED)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
