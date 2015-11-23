package com.example.andrea.musicreview.activities;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.utility.MyLoginManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private MyLoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final LoginButton fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        final Button loginButton = (Button) findViewById(R.id.login_button);
        loginManager = new MyLoginManager(this);
        final View.OnClickListener listener = this;
        switch(loginManager.getCurrentLoginService()) {
            case MyLoginManager.EMAIL_LOGIN:
                fbLoginButton.setVisibility(View.GONE);
                loginButton.setText(R.string.log_out);
                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loginManager.discardUser();
                        loginButton.setText(R.string.log_in);
                        loginButton.setOnClickListener(listener);
                    }
                });
                break;
            case MyLoginManager.FB_LOGIN:
                loginButton.setVisibility(View.GONE);
                fbLoginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loginButton.setText(R.string.log_in);
                        fbLoginButton.setVisibility(View.GONE);
                        loginButton.setOnClickListener(listener);
                        loginButton.setVisibility(View.VISIBLE);
                    }
                });
                break;
            case MyLoginManager.NOT_LOGGED:
                loginButton.setText(R.string.log_in);
                fbLoginButton.setVisibility(View.GONE);
                loginButton.setOnClickListener(this);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_button:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                Log.i("ERROR", "No view found");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
