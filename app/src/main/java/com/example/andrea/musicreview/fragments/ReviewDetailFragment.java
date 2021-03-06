package com.example.andrea.musicreview.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.interfaces.DetailOpener;
import com.example.andrea.musicreview.model.Album;
import com.example.andrea.musicreview.utility.ConnectionHandler;
import com.example.andrea.musicreview.utility.MyLoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;

public class ReviewDetailFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    public static final String FRAGMENT_TAG = "ReviewDetailFragment";
    private ViewGroup rootView;
    private ImageButton favoriteButton;
    private LinearLayout errorMessage;
    private String URL = "http://www.saltedmagnolia.com/get_review_detail.php?album_id=";
    private final static String ALBUM_ID = "album_id";
    private Album album;
    private int albumID;
    ScrollView scrollView;
    private DetailOpener detailOpener;
    private final static String URL_SET_FAVORITE = "http://www.saltedmagnolia.com/set_favorite.php?album_id=";
    private final static String URL_DISCARD_FAVORITE = "http://www.saltedmagnolia.com/discard_favorite.php?album_id=";
    private MyLoginManager myLoginManager;

    public static ReviewDetailFragment newInstance(int id) {
        ReviewDetailFragment fragment = new ReviewDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ALBUM_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
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
        myLoginManager = new MyLoginManager(getContext());
    }

    private void setURL(){
        albumID = getArguments().getInt(ALBUM_ID);
        if (myLoginManager.getUserID()!=null) {
            URL = URL + albumID + "&user_id=" + myLoginManager.getUserID();
        } else {
            URL = URL + albumID + "&user_id=no_id";
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setURL();
        new ReviewDownloader().execute(URL);
        getActivity().setTitle("MusicReview");
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
        rootView.findViewById(R.id.fb_share).setOnClickListener(this);
        scrollView = (ScrollView) rootView.findViewById(R.id.scroll_view);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.favorite_icon:
                String id = myLoginManager.getUserID();
                if(id!=null){
                    if(this.album.isFavorite()){
                        new FavoriteUpdater().execute(URL_DISCARD_FAVORITE + albumID + "&user_id="
                                + id);
                    } else {
                        new FavoriteUpdater().execute(URL_SET_FAVORITE + albumID + "&user_id="
                                + id);
                    }
                } else {
                    Toast.makeText(getContext(), "You must be logged in to set an album as your favorite",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.artist_name:
                detailOpener.OpenArtistBio(album.getArtist().getId());
                break;
            case R.id.play_on_spotify:
                String uri = album.getSpotifyURI();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
                break;
            case R.id.fb_share:
                ShareDialog.show(this, new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("http://corsi.unibo.it/Laurea/IngegneriaScienzeInformatiche/Pagine/default.aspx"))
                        .setContentTitle("Sto leggendo la review di " + album.getTitle() + " su MusicReview!!!")
                        .setContentDescription("Andrea sta leggendo la review di "
                                + album.getTitle() + " by " + album.getArtist().getName() +
                                " su MusicReview. dacci un'occhiata anche tu!")
                        .setImageUrl(Uri.parse("http://saltedmagnolia.com/" + album.getImageURL()))
                        .build());
                break;
            case R.id.general_error_panel:
                setURL();
                new ReviewDownloader().execute(URL);
                break;
            default:
                Log.i("ERROR", "No view found.");
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    private Album parse(String input) throws JSONException, ParseException{
        JSONArray array = new JSONArray(input);
        JSONObject row = array.getJSONObject(0);
        return new Album(row);
    }

    private void setFavoriteIcon(boolean isFavorite){
        Drawable favoriteIcon = isFavorite ? ContextCompat.getDrawable(getActivity(), R.mipmap.ic_ic_favorite_red_36dp) :
                ContextCompat.getDrawable(getActivity(), R.mipmap.ic_ic_favorite_border_white_36dp);
        favoriteButton.setImageDrawable(favoriteIcon);
    }

    public class ReviewDownloader extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                rootView.findViewById(R.id.loading_panel).setVisibility(View.GONE);
                if(s.equals("NON_CONNECTED_TO_INTERNET_ERROR") || s.equals("CONNECTION_TO_SERVER_ERROR")){
                    Log.i("ERROR", s);
                    scrollView.setVisibility(View.GONE);
                    errorMessage.setVisibility(View.VISIBLE);
                    return;
                }
                scrollView.setVisibility(View.VISIBLE);
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
                getActivity().setTitle(album.getTitle());
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
            return ConnectionHandler.DownloadFromURL(params[0], getContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RelativeLayout loadingPanel = (RelativeLayout)rootView.findViewById(R.id.loading_panel);
            if(loadingPanel != null){
                loadingPanel.setVisibility(View.VISIBLE);
            }
        }
    }

    public class FavoriteUpdater extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
                rootView.findViewById(R.id.loading_panel).setVisibility(View.GONE);
//              rootView.findViewById(R.id.scroll_view).setVisibility(View.VISIBLE);
            if(s.equals("NON_CONNECTED_TO_INTERNET_ERROR") || s.equals("CONNECTION_TO_SERVER_ERROR") || !(s.charAt(0)=='1')){
                Log.i("ERROR", s);
                scrollView.setVisibility(View.GONE);
            } else {
                album.switchFavorite();
                setFavoriteIcon(album.isFavorite());
                scrollView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            return ConnectionHandler.DownloadFromURL(params[0], getContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rootView.findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);
        }
    }
}
