package com.example.andrea.musicreview.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.fragments.SearchResultsFragment;
import com.example.andrea.musicreview.interfaces.DetailOpener;
import com.example.andrea.musicreview.interfaces.Downloader;
import com.example.andrea.musicreview.utility.ConnectionHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchableActivity extends MyBaseActivity implements DetailOpener, Downloader {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.search);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Fragment fragment = new SearchResultsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("key_words", query);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment)
                    .addToBackStack(null).commit();
        }
    }
    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public void OpenAlbumReviewDetail(int id) {
        Intent intent = new Intent(SearchableActivity.this, MainActivity.class);
        Bundle b = new Bundle();
        b.putInt(MainActivity.ALBUM_ID, id); //Your id
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    @Override
    public String DownloadFromURL(String URL) {
        if (ConnectionHandler.isConnected(this)) {
            InputStream is = null;
            int len = 10000;
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
}
