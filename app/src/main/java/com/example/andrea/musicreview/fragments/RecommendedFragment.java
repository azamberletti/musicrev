package com.example.andrea.musicreview.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andrea.musicreview.utility.FacebookInformationHelper;

public class RecommendedFragment extends AlbumGridFragment{

    private final static String URL = "http://www.saltedmagnolia.com/get_best_of_month.php";

    @Override
    public String getURL() {
        return URL;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        new FacebookInformationHelper(getContext()).sendInformation();
        return rootView;
    }
}
