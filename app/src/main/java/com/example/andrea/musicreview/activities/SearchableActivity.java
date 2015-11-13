package com.example.andrea.musicreview.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.fragments.SearchResultsFragment;
import com.example.andrea.musicreview.interfaces.DetailOpener;

public class SearchableActivity extends MyBaseActivity implements DetailOpener {
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
}
