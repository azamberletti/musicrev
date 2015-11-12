package com.example.andrea.musicreview;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.andrea.musicreview.Interfaces.Downloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class ArtistBioFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private static final String ARTIST_ID = "artist_id";
    private int artistID;
    private Artist artist;
    private LinearLayout errorMessage;
    private ViewGroup rootView;
    private Downloader downloader;
    private static final String URL = "http://www.saltedmagnolia.com/get_artist_bio.php?artist_id=";
    //private OnFragmentInteractionListener mListener;

    public static ArtistBioFragment newInstance(int id) {
        ArtistBioFragment fragment = new ArtistBioFragment();
        Bundle args = new Bundle();
        args.putInt(ARTIST_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            downloader = (Downloader) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Downloader");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            artistID = getArguments().getInt(ARTIST_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("ID", String.valueOf(artistID));
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_artist_bio, container, false);
        errorMessage = (LinearLayout)rootView.findViewById(R.id.general_error_panel);
        errorMessage.setOnClickListener(this);
        errorMessage.setVisibility(View.GONE);
        return rootView;
    }

    private Artist parse(String s) throws JSONException, ParseException{
        JSONArray array = new JSONArray(s);
        JSONObject row = array.getJSONObject(0);
        String name = (String)row.get("Name");
        String bio = (String)row.get("Bio");
        return new Artist(artistID, name, bio);
    }

    @Override
    public void onResume() {
        super.onResume();
        String request = URL + artistID;
        new BioDownloader().execute(request);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.general_error_panel:
                break;
        }
    }

    public class BioDownloader extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                getView().findViewById(R.id.loading_panel).setVisibility(View.GONE);
                if(s.equals("NON_CONNECTED_TO_INTERNET_ERROR") || s.equals("CONNECTION_TO_SERVER_ERROR")){
                    Log.i("ERROR", s);
                    errorMessage.setVisibility(View.VISIBLE);
                    return;
                }
                errorMessage.setVisibility(View.GONE);
                artist = parse(s);
                ((TextView)rootView.findViewById(R.id.artist_name)).setText(artist.getName());
                ((TextView)rootView.findViewById(R.id.artist_bio)).setText(artist.getBio());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("ERROR", "JSON_EXCEPTION");
                errorMessage.setVisibility(View.VISIBLE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            return downloader.DownloadFromURL(params[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getView().findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);
        }
    }
}
