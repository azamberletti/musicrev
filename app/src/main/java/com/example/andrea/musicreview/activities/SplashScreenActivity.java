package com.example.andrea.musicreview.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.andrea.musicreview.R;
import com.facebook.FacebookSdk;

/**
 * Created by luca-campana on 21/11/15.
 */
public class SplashScreenActivity extends Activity {

    private final int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if(!FacebookSdk.isInitialized()){
            FacebookSdk.sdkInitialize(getApplicationContext());
        }
        setContentView(R.layout.splash_screen);
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(SPLASH_TIME);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}
