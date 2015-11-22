package com.example.andrea.musicreview.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.activities.RegistrationActivity;
import com.facebook.AccessToken;

import org.json.JSONException;
import org.json.JSONObject;

public class MyLoginManager {

    private static final String USER_MAIL = "USER_EMAIL";
    private static final String USER_NAME = "USER_NAME";
    private Context context;

    public MyLoginManager(Context context){
        this.context = context;
    }

    public void setUser(String jsonUser) {
        try {
            JSONObject user = new JSONObject(jsonUser);
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.login_preferences_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(USER_MAIL, (String) user.get(RegistrationActivity.USER_MAIL));
            editor.putString(USER_NAME, (String) user.get(RegistrationActivity.USER_NAME));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.login_preferences_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString(USER_NAME, null);
    }

    public String getUserMail() {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.login_preferences_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString(USER_MAIL, null);
    }

    public void discardUser() {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.login_preferences_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(USER_MAIL);
        editor.remove(USER_NAME);
        editor.commit();
    }

    public String getUserID(){
        if(getUserMail()==null){
            return AccessToken.getCurrentAccessToken()==null?
                    null: AccessToken.getCurrentAccessToken().getUserId();
        } else {
            return getUserMail();
        }
    }
}
