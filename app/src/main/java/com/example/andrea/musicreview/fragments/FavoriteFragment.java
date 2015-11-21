package com.example.andrea.musicreview.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andrea.musicreview.R;
import com.facebook.AccessToken;

public class FavoriteFragment extends AlbumGridFragment {

    private final static String URL = "http://www.saltedmagnolia.com/get_favorite_albums.php?user_id=";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        TextView title = (TextView) rootView.findViewById(R.id.grid_title);
        title.setText(R.string.favorite_albums);
        return rootView;
    }

    @Override
    public String getURL() {
        return URL + AccessToken.getCurrentAccessToken().getUserId();
    }
}
