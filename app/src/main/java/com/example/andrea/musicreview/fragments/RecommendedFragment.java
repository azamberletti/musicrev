package com.example.andrea.musicreview.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.utility.ConnectionHandler;
import com.example.andrea.musicreview.utility.FacebookInformationHelper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecommendedFragment extends AlbumGridFragment{

    private String URL = "http://www.saltedmagnolia.com/post_music_likes.php";
    private View rootView;

    @Override
    public String getURL() {
        return URL;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        new ListDownloader().execute();
        //setSource(new FacebookInformationHelper(getContext()).getRecommendedAlbums());
        return rootView;
    }

    private String SendToURL(String URL, String msg) {
        InputStream is = null;
        OutputStream os = null;
        if (ConnectionHandler.isConnected(getContext())) {
            try {

                java.net.URL url = new URL(URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
//                int response = conn.getResponseCode();
//                if (response != HttpURLConnection.HTTP_OK) {
//                    Log.i("ERROR_CODE", String.valueOf(response));
//                    throw new IOException();
//                }

                os = conn.getOutputStream();
                BufferedWriter wr = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                //BufferedOutputStream wr = new BufferedOutputStream(conn.getOutputStream());
                wr.write(msg);
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
                return st;
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
                return null;
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
            return null;
        }
    }

    public class ListDownloader extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
//            Log.i("albums",s);
            rootView.findViewById(R.id.loading_panel).setVisibility(View.GONE);
            setSource(s);
        }

        @Override
        protected String doInBackground(String... params) {
            String msg = new FacebookInformationHelper(getContext()).getRecommendedAlbums();
            return SendToURL(URL, msg);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rootView.findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);
        }
    }
}
