package com.example.andrea.musicreview.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.interfaces.DetailOpener;
import com.example.andrea.musicreview.interfaces.Downloader;
import com.example.andrea.musicreview.model.Album;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewDetailFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private ViewGroup rootView;
    private ImageButton favoriteButton;
    private Drawable favoriteIcon;
    private LinearLayout errorMessage;
    private Downloader downloader;
    private String URL = "http://www.saltedmagnolia.com/get_review_detail.php?album_id=";
    private final static String ALBUM_ID = "album_id";
    private Album album;
    private int albumID;
    private DetailOpener detailOpener;

    public static ReviewDetailFragment newInstance(int id) {
        ReviewDetailFragment fragment = new ReviewDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ALBUM_ID, id);
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
            detailOpener = (DetailOpener) activity;
            downloader = (Downloader) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Downloader and DetailOpener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            albumID = getArguments().getInt(ALBUM_ID);
        }
        URL = URL + albumID;
    }

    @Override
    public void onResume() {
        super.onResume();
        new ReviewDownloader().execute(URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_review_detail, container, false);
        favoriteButton = (ImageButton) rootView.findViewById(R.id.favorite_icon);
        rootView.findViewById(R.id.artist_name).setOnClickListener(this);
        favoriteButton.setOnClickListener(this);
        errorMessage = (LinearLayout)rootView.findViewById(R.id.general_error_panel);
        errorMessage.setOnClickListener(this);
        errorMessage.setVisibility(View.GONE);
        rootView.findViewById(R.id.play_on_spotify).setOnClickListener(this);
        rootView.findViewById(R.id.scroll_view).setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.favorite_icon:
                this.album.setIsFavorite(!this.album.isFavorite());
                setFavoriteIcon(this.album.isFavorite());
                break;
            case R.id.artist_name:
                detailOpener.OpenArtistBio(album.getArtist().getId());
                break;
            case R.id.play_on_spotify:
                String uri = album.getSpotifyURI();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(i);
                break;
            case R.id.general_error_panel:
                new ReviewDownloader().execute(URL);
                break;
            default:
                Log.i("ERROR", "No view found.");
        }
    }

    private Album parse(String input) throws JSONException, ParseException{
        JSONArray array = new JSONArray(input);
        JSONObject row = array.getJSONObject(0);
        return new Album(row);
    }

    private void setFavoriteIcon(boolean isFavorite){
        favoriteIcon = isFavorite? ContextCompat.getDrawable(getActivity(), R.mipmap.ic_ic_favorite_red_36dp):
                ContextCompat.getDrawable(getActivity(),R.mipmap.ic_ic_favorite_border_white_36dp);
        favoriteButton.setImageDrawable(favoriteIcon);
    }

    public class ReviewDownloader extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                getView().findViewById(R.id.loading_panel).setVisibility(View.GONE);
                getView().findViewById(R.id.scroll_view).setVisibility(View.VISIBLE);
                if(s.equals("NON_CONNECTED_TO_INTERNET_ERROR") || s.equals("CONNECTION_TO_SERVER_ERROR")){
                    Log.i("ERROR", s);
                    errorMessage.setVisibility(View.VISIBLE);
                    return;
                }
                errorMessage.setVisibility(View.GONE);
                album = parse(s);
                ((TextView) rootView.findViewById(R.id.artist_name)).setText(album.getArtist().getName());
                ((TextView) rootView.findViewById(R.id.album_tile)).setText(album.getTitle());
                ((TextView) rootView.findViewById(R.id.album_review)).setText(album.getReview());
                ((TextView) rootView.findViewById(R.id.grade)).setText(String.valueOf(album.getGrade()));
                Log.i("IMAGE_URL", album.getImageURL());
                Picasso.with(getActivity()).load("http://www.saltedmagnolia.com/" + album.getImageURL())
                        .into((ImageView) rootView.findViewById(R.id.album_cover));
                setFavoriteIcon(album.isFavorite());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("ERROR", "JSON_EXCEPTION");
                errorMessage.setVisibility(View.VISIBLE);
            } catch (ParseException e){
                e.printStackTrace();
                Log.i("ERROR", "PARSE_EXCEPTION");
                errorMessage.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            return downloader.DownloadFromURL(params[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RelativeLayout loadingPanel = (RelativeLayout)getView().findViewById(R.id.loading_panel);
            if(loadingPanel != null){
                loadingPanel.setVisibility(View.VISIBLE);
            }
        }
    }
}
