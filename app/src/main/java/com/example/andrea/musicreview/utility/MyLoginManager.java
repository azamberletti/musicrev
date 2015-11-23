package com.example.andrea.musicreview.utility;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.activities.RegistrationActivity;
import com.facebook.AccessToken;
import com.facebook.Profile;

import org.json.JSONException;
import org.json.JSONObject;

public class MyLoginManager {

    private static final String USER_MAIL = "USER_EMAIL";
    private static final String USER_NAME = "USER_NAME";
    public static final String FB_LOGIN = "FB";
    public static final String EMAIL_LOGIN = "EMAIL";
    public static final String NOT_LOGGED = "NONE";
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
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCurrentLoginService(){
        if(getUserMail()!=null){
            return EMAIL_LOGIN;
        } else if(AccessToken.getCurrentAccessToken()!=null){
            return FB_LOGIN;
        } else {
            return NOT_LOGGED;
        }
    }

    public String getUserName() {
        switch(getCurrentLoginService()) {
            case FB_LOGIN:
                return Profile.getCurrentProfile().getName();
            case EMAIL_LOGIN:
                SharedPreferences sharedPref = context.getSharedPreferences(
                        context.getString(R.string.login_preferences_file_key), Context.MODE_PRIVATE);
                return sharedPref.getString(USER_NAME, null);
            default:
                return null;
        }
    }

    public String getUserMail() {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.login_preferences_file_key), Context.MODE_PRIVATE);
        String mail = sharedPref.getString(USER_MAIL, null);
        return mail;
    }

    public void discardUser() {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.login_preferences_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(USER_MAIL);
        editor.remove(USER_NAME);
        editor.apply();
    }

    public String getUserID(){
        switch(getCurrentLoginService()) {
            case FB_LOGIN:
                return AccessToken.getCurrentAccessToken().getUserId();
            case EMAIL_LOGIN:
                return getUserMail();
            default:
                return null;
        }
    }
}
