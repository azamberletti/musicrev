package com.example.andrea.musicreview.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.andrea.musicreview.R;
import com.facebook.login.LoginManager;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_button:
                LoginManager.getInstance().logOut();
                Intent i = new Intent(this,LoginActivity.class);
                startActivity(i);
                finish();
                break;
            default:
                Log.i("ERROR", "No view found");
        }
    }
}
