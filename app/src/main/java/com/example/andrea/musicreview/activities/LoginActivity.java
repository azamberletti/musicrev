package com.example.andrea.musicreview.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.utility.CheckForm;
import com.example.andrea.musicreview.utility.ConnectionHandler;
import com.example.andrea.musicreview.utility.MyLoginManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String URL = "http://www.saltedmagnolia.com/get_user.php?email=";
    private static final int REGISTRATION_REQUEST = 1;
    private CallbackManager callbackManager;
    private TextView info;
    private ProfileTracker mProfileTracker;
    private EditText mailEdt;
    private EditText pswdEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.loading_panel).setVisibility(View.GONE);
        callbackManager = CallbackManager.Factory.create();
        info = (TextView)findViewById(R.id.info);
        mailEdt = (EditText) findViewById(R.id.email);
        pswdEdt = (EditText) findViewById(R.id.pswd);
        findViewById(R.id.register).setOnClickListener(this);
            //ImageView image = (ImageView)findViewById(R.id.profile_image);
        findViewById(R.id.login_button).setOnClickListener(this);
        LoginButton fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        if(AccessToken.getCurrentAccessToken()!=null){
            findViewById(R.id.or_divider).setVisibility(View.GONE);
            fbLoginButton.setVisibility(View.GONE);
        } else {
            fbLoginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends, user_likes"));
            fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                /*info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );*/
                    //Profile profile = Profile.getCurrentProfile();

                    if (Profile.getCurrentProfile() == null) {
                        mProfileTracker = new ProfileTracker() {
                            @Override
                            protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                                Log.v("facebook - profile", profile2.getFirstName());
                                mProfileTracker.stopTracking();
                            }
                        };
                        mProfileTracker.startTracking();
                    } else {
                        Profile profile = Profile.getCurrentProfile();
                        Log.v("facebook - profile", profile.getFirstName());
                    }
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    // Application code
                                    Log.v("LoginActivity", response.toString());
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender,birthday,music");
                    request.setParameters(parameters);
                    request.executeAsync();
                    openNextActivity();
                }

                @Override
                public void onCancel() {
                    info.setText("Login attempt canceled.");
                }

                @Override
                public void onError(FacebookException e) {
                    info.setText("Login attempt failed.");
                }
            });
        }







/*      FacebookSdk.sdkInitialize(getApplicationContext());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);*/
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.add(R.id.container, new LoginFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    private void onUserReceived(String jsonUser){
        MyLoginManager loginManager = new MyLoginManager(getApplicationContext());
        loginManager.setUser(jsonUser);
        if(AccessToken.getCurrentAccessToken()==null){
            findViewById(R.id.login_forms).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.info)).setText("Welcome " + loginManager.getUserName() + "!\r\n" +
                    "If you want you can still sign in with Facebook to have an even more personalized experience.");
        } else {
            openNextActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REGISTRATION_REQUEST){
            if(resultCode==RESULT_OK){
                onUserReceived(data.getExtras().getString(RegistrationActivity.REGISTERED_USER));
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void openNextActivity(){
        Log.i("log", "log");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.skip) {
            openNextActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.register:
                Intent intent = new Intent(this, RegistrationActivity.class);
                startActivityForResult(intent, REGISTRATION_REQUEST);
                break;
            case R.id.login_button:
                CheckForm checkForm = new CheckForm(this);
                if(!checkForm.isEmpty(mailEdt) | checkForm.pswdIsCorrect(pswdEdt)){
                    new UserChecker().execute(URL + mailEdt.getText() + "&pswd=" + pswdEdt.getText());
                }
                break;
        }
    }
    /*
    @Override
    public void onClick(View v) {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
        shareButton = (ShareButton)findViewById(R.id.fb_share_button);
        shareButton.setShareContent(content);
    }
    */
    public class UserChecker extends AsyncTask<String, Void, String> {

        private static final String WRONG_PSWD_MSG = "Password is wrong";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            findViewById(R.id.loading_panel).setVisibility(View.GONE);
            if (s.equals("NON_CONNECTED_TO_INTERNET_ERROR") || s.equals("CONNECTION_TO_SERVER_ERROR")) {
                Toast.makeText(getApplicationContext(), "There was an error with the connection. Please try again",
                        Toast.LENGTH_SHORT).show();
                Log.i("ERROR", s);
                return;
            } else if (s.equals(WRONG_PSWD_MSG)){
                pswdEdt.setError(WRONG_PSWD_MSG);
            } else if (s.equals("null")){
                mailEdt.setError("No user matches the given email");
            } else {
                onUserReceived(s);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            return ConnectionHandler.DownloadFromURL(params[0], getApplicationContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);
        }
    }
}
