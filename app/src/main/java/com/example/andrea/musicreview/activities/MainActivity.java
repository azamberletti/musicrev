package com.example.andrea.musicreview.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import java.net.URL;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.fragments.ArtistBioFragment;
import com.example.andrea.musicreview.fragments.LastReviewsFragment;
import com.example.andrea.musicreview.fragments.ReviewDetailFragment;
import com.example.andrea.musicreview.interfaces.DetailOpener;
import com.example.andrea.musicreview.interfaces.Downloader;
import com.example.andrea.musicreview.utility.ConnectionHandler;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;

public class MainActivity extends MyBaseActivity implements Downloader, DetailOpener{

    public final static String ALBUM_ID = "album_id";
    private AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        final Intent intent = new Intent(this, LoginActivity.class);
/*        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if(AccessToken.getCurrentAccessToken()==null) {
                    startActivity(intent);
                }
            }
        };*/
        Bundle b = getIntent().getExtras();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                launchLogin();
            }
        };
        if(b != null && b.containsKey(ALBUM_ID)){
            OpenAlbumReviewDetail(b.getInt(ALBUM_ID));
        } else {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new LastReviewsFragment()).commit();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        launchLogin();
    }

    private void launchLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        if(AccessToken.getCurrentAccessToken()==null) {
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        return true;
    }

    @Override
    public void OpenAlbumReviewDetail(int id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, ReviewDetailFragment.newInstance(id), ReviewDetailFragment.FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void OpenArtistBio(int id){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, ArtistBioFragment.newInstance(id))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public String DownloadFromURL(String URL) {
        if (ConnectionHandler.isConnected(this)) {
            InputStream is = null;
            int len = 10000;
            try {
                URL url = new URL(URL);
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
