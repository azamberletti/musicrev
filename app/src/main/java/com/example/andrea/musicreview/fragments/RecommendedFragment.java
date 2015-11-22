package com.example.andrea.musicreview.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.utility.ConnectionHandler;
import com.example.andrea.musicreview.utility.FacebookInformationHelper;
import com.facebook.AccessToken;

public class RecommendedFragment extends AlbumGridFragment{

    private String URL = "http://www.saltedmagnolia.com/post_music_likes.php";
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        rootView.findViewById(R.id.loading_panel).setVisibility(View.GONE);
        if(AccessToken.getCurrentAccessToken()!=null){
            new ListDownloader().execute();
        } else {
            View errorMessage = rootView.findViewById(R.id.general_error_panel);
            ((TextView) errorMessage.findViewById(R.id.error_message))
                    .setText("You must be logged in to Facebook to see this section");
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.findViewById(R.id.retry).setVisibility(View.GONE);
        }
        //setSource(new FacebookInformationHelper(getContext()).getRecommendedAlbums());
        return rootView;
    }

    public class ListDownloader extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
//            Log.i("albums",s);
            rootView.findViewById(R.id.loading_panel).setVisibility(View.GONE);
            setSource(s);
        }

        @Override
        protected String doInBackground(String... params) {
            String msg = new FacebookInformationHelper().getRecommendedAlbums();
            return ConnectionHandler.SendToURL(URL, msg, getContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rootView.findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);
        }
    }
}
