package com.example.andrea.musicreview.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.utility.MyLoginManager;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private MyLoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        loginManager = new MyLoginManager(this);
        final Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        if(loginManager.getUserMail()!=null){
            loginButton.setText(R.string.log_out);
            final View.OnClickListener listener = this;
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginManager.discardUser();
                    loginButton.setText(R.string.log_in);
                    loginButton.setOnClickListener(listener);
                    checkFB();
                }
            });
        } else {
            checkFB();
        }
    }

    private void checkFB() {
        if(AccessToken.getCurrentAccessToken()==null){
            findViewById(R.id.fb_button_layout).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fb_login_button:
                LoginManager.getInstance().logOut();
                Intent i = new Intent(this,LoginActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.login_button:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                Log.i("ERROR", "No view found");
        }
    }
}
