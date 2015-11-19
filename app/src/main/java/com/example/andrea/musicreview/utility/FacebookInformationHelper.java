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

/**
 * Created by luca-campana on 19/11/15.
 */
public class FacebookInformationHelper {

    private JSONObject facebookAPIResponse;
    private Context context;
    private String URL = "http://www.saltedmagnolia.com/post_music_likes.php";

    public FacebookInformationHelper(Context context) {
        this.context = context;
    }

    public void sendInformation() {

        String response;
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        Log.v("FACEBOOK_JSON", object.toString());
                        facebookAPIResponse = object;
                        new ListDownloader().execute("");
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "music");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void SendToURL(String URL) {
        InputStream is = null;
        OutputStream os = null;
        if (ConnectionHandler.isConnected(context)) {
            try {

                java.net.URL url = new URL(URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
/*                int response = conn.getResponseCode();
                if (response != HttpURLConnection.HTTP_OK) {
                    Log.i("ERROR_CODE", String.valueOf(response));
                    throw new IOException();
                }*/

                os = conn.getOutputStream();
                BufferedWriter wr = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                //BufferedOutputStream wr = new BufferedOutputStream(conn.getOutputStream());
                wr.write(facebookAPIResponse.toString());
                wr.flush();
                wr.close();
                conn.connect();
                int len = 10000;
                is = conn.getInputStream();
                Reader reader = new InputStreamReader(is, "UTF-8");
                char[] buffer = new char[len];
                reader.read(buffer);
                String st = new String(buffer);
                Log.i("RESPONSE", st);

/*
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                    Log.i("RESPONSE", "" + sb.toString());
                }
                br.close();*/


            } catch (IOException e) {
                Log.i("ERROR", e.toString());
            } finally {

                if (is != null && os != null) {
                    try {
                        is.close();
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Log.i("ERROR", "NON_CONNECTED_TO_INTERNET_ERROR");
        }

    }

    public class ListDownloader extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {

        }

        @Override
        protected String doInBackground(String... params) {
            SendToURL(URL);
            return "ok";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}
