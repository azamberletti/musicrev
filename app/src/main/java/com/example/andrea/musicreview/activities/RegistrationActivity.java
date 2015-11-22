package com.example.andrea.musicreview.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.utility.CheckForm;
import com.example.andrea.musicreview.utility.ConnectionHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends Activity implements View.OnClickListener {
    public static final String USER_NAME = "Name";
    public static final String USER_MAIL = "Email";
    private static final String USER_PSWD = "Password";
    private static final String URL = "http://www.saltedmagnolia.com/new_user.php";
    public static final String REGISTERED_USER = "USER";
    private EditText nameEdt;
    private EditText mailEdt;
    private EditText pswdEdt;
    private CheckForm checkForm;
    private EditText pswd2Edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        findViewById(R.id.general_error_panel).setVisibility(View.GONE);
        nameEdt = (EditText) findViewById(R.id.name);
        mailEdt = (EditText) findViewById(R.id.email);
        pswdEdt = (EditText) findViewById(R.id.pswd);
        pswd2Edt = (EditText) findViewById(R.id.pswd_retype);
        checkForm = new CheckForm(getApplicationContext());
        findViewById(R.id.signup_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.signup_button:
                if(!checkForm.isEmpty(nameEdt) | !checkForm.isEmpty(mailEdt) |
                        !checkForm.pswdIsCorrect(pswdEdt) | !checkForm.pswdIsSame(pswdEdt, pswd2Edt)){
                    Map<String, String> jsonMap = new HashMap<>();
                    jsonMap.put(USER_NAME, nameEdt.getText().toString());
                    jsonMap.put(USER_MAIL, mailEdt.getText().toString());
                    jsonMap.put(USER_PSWD, pswdEdt.getText().toString());
                    new RegistrationSender().execute(URL, new JSONObject(jsonMap).toString());
                }
                break;
        }
    }

    public class RegistrationSender extends AsyncTask<String, Void, String> {

        private String jsonUser;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            final View errorMessage = findViewById(R.id.general_error_panel);
            final View mainLayout = findViewById(R.id.signup_forms);
            if (!(s.charAt(0)=='1')) {
                Log.i("ERROR", s);
                mainLayout.setVisibility(View.GONE);
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        errorMessage.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                errorMessage.setVisibility(View.GONE);
                mainLayout.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.putExtra(REGISTERED_USER, jsonUser);
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            jsonUser = params[1];
            return ConnectionHandler.SendToURL(params[0], params[1], getApplicationContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
