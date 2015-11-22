package com.example.andrea.musicreview.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.fragments.ArtistBioFragment;
import com.example.andrea.musicreview.fragments.LastReviewsFragment;
import com.example.andrea.musicreview.fragments.ReviewDetailFragment;
import com.example.andrea.musicreview.interfaces.DetailOpener;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;

public class MainActivity extends MyBaseActivity implements DetailOpener{

    public final static String ALBUM_ID = "album_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
}
