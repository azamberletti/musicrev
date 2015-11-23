package com.example.andrea.musicreview.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionHandler{

    public static boolean isConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

     public static String DownloadFromURL(String URL, Context context) {
        if (isConnected(context)) {
            InputStream is = null;
            int len = 20000;
            try {
                java.net.URL url = new URL(URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                int response = conn.getResponseCode();
                if (response != HttpURLConnection.HTTP_OK) {
                    throw new IOException();
                }
                is = conn.getInputStream();
                Reader reader = new InputStreamReader(is, "UTF-8");
                char[] buffer = new char[len];
                reader.read(buffer);
                return new String(buffer);
            } catch (IOException e) {
                return "CONNECTION_TO_SERVER_ERROR";
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            return "NON_CONNECTED_TO_INTERNET_ERROR";
        }
    }



    public static String SendToURL(String URL, String msg, Context context) {
        InputStream is = null;
        OutputStream os = null;
        if (isConnected(context)) {
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
                if(msg != null){
                    wr.write(msg);
                }
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
}
