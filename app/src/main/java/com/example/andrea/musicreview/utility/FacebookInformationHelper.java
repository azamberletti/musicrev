package com.example.andrea.musicreview.utility;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.model.User;
import com.example.andrea.musicreview.view.ArtistListAdapter;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;

public class FacebookInformationHelper {

    private String facebookAPIResponse;
    private Context context;
    private String recommendedAlbums;

    public FacebookInformationHelper(Context context) {
        this.context = context;
    }

    public String getRecommendedAlbums() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        Log.v("FACEBOOK_JSON", object.toString());
                        facebookAPIResponse = object.toString();
                        //new ListDownloader().execute("");
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "music");
        request.setParameters(parameters);
        request.executeAndWait();
        return facebookAPIResponse;
    }

}
