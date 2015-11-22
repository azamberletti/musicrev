package com.example.andrea.musicreview.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.interfaces.DetailOpener;
import com.example.andrea.musicreview.model.Album;
import com.example.andrea.musicreview.model.Artist;
import com.example.andrea.musicreview.utility.ConnectionHandler;
import com.example.andrea.musicreview.view.AlbumGridAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class ArtistBioFragment extends android.support.v4.app.Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String ARTIST_ID = "artist_id";
    private int artistID;
    private TextView albumFrom;
    private Artist artist;
    private GridView grid;
    private LinearLayout errorMessage;
    private ViewGroup rootView;
    private static final String ARTIST_BIO_URL = "http://www.saltedmagnolia.com/get_artist_bio.php?artist_id=";
    //private static final String ARTIST_ALBUM_URL = "http://www.saltedmagnolia.com/get_artist_albums.php?artist_id=";
    private DetailOpener detailOpener;

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
        try {
            detailOpener = (DetailOpener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DetailOpener");
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
        albumFrom = (TextView)rootView.findViewById(R.id.album_from);
        grid = (GridView) rootView.findViewById(R.id.grid);
        grid.setAdapter(new AlbumGridAdapter(getActivity(), R.layout.album_grid_item_layout, new ArrayList<Album.AlbumBasicInfo>()));
        grid.setOnItemClickListener(this);
        return rootView;
    }

    private Artist parse(String s) throws JSONException, ParseException{
        JSONArray array = new JSONArray(s);
        String name = "";
        String bio = "";
        String image = "";
        for (int i = 0; i < array.length(); i++) {
            if(!array.getJSONObject(i).isNull("Bio")){
                bio = (String) array.getJSONObject(i).get("Bio");
                name = (String) array.getJSONObject(i).get("Name");
                image = (String) array.getJSONObject(i).get("Image");
            }
        }
        return new Artist(artistID, name, bio, image);
    }
    private List<Album.AlbumBasicInfo> parseArtistAlbums(String s) throws JSONException, ParseException {
        JSONArray array = new JSONArray(s);
        List<Album.AlbumBasicInfo> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            if(!array.getJSONObject(i).isNull("Title")){
                list.add(new Album.AlbumBasicInfo(array.getJSONObject(i)));
            }
        }
        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        String request = ARTIST_BIO_URL + artistID;
        new BioDownloader().execute(request);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.general_error_panel:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        detailOpener.OpenAlbumReviewDetail(((Album.AlbumBasicInfo) parent.getItemAtPosition(position)).getId());
    }

    public class BioDownloader extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                rootView.findViewById(R.id.loading_panel).setVisibility(View.GONE);
                if(s.equals("NON_CONNECTED_TO_INTERNET_ERROR") || s.equals("CONNECTION_TO_SERVER_ERROR")){
                    Log.i("ERROR", s);
                    errorMessage.setVisibility(View.VISIBLE);
                    return;
                }
                errorMessage.setVisibility(View.GONE);
                artist = parse(s);
                ((TextView)rootView.findViewById(R.id.artist_name)).setText(artist.getName());
                ((TextView)rootView.findViewById(R.id.artist_bio)).setText(artist.getBio());
                Picasso.with(getActivity()).load("http://www.saltedmagnolia.com/" + artist.getImage())
                        .resize(600, 250)
                        .centerCrop()
                        .into(((ImageView) rootView.findViewById(R.id.image)));
                albumFrom.setText(artist.getName());
                ((AlbumGridAdapter) grid.getAdapter()).refreshList(parseArtistAlbums(s));
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
            return ConnectionHandler.DownloadFromURL(params[0], getContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getView().findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);
        }
    }
}
