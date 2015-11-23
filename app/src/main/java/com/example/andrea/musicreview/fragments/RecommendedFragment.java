package com.example.andrea.musicreview.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.utility.ConnectionHandler;
import com.example.andrea.musicreview.utility.FacebookInformationHelper;
import com.example.andrea.musicreview.utility.MyLoginManager;
import com.facebook.AccessToken;

import org.w3c.dom.Text;

public class RecommendedFragment extends AlbumGridFragment{

    private static final String URL = "http://www.saltedmagnolia.com/post_music_likes.php";
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = super.onCreateView(inflater, container, savedInstanceState);

        rootView.findViewById(R.id.loading_panel).setVisibility(View.GONE);
        if(new MyLoginManager(getContext()).getCurrentLoginService().equals(MyLoginManager.FB_LOGIN)){
            new ListDownloader().execute();
        } else {
            View errorMessage = rootView.findViewById(R.id.general_error_panel);
            rootView.findViewById(R.id.login_error_layout).setVisibility(View.VISIBLE);
            ((TextView)rootView.findViewById(R.id.error_message)).setText(R.string.error_log_with_facebook);
            //errorMessage.setVisibility(View.VISIBLE);
            errorMessage.findViewById(R.id.retry).setVisibility(View.GONE);
        }
        //setSource(new FacebookInformationHelper(getContext()).getRecommendedAlbums());
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(AccessToken.getCurrentAccessToken()!=null){
            new ListDownloader().execute();
        } else {
            View errorMessage = rootView.findViewById(R.id.general_error_panel);
            rootView.findViewById(R.id.login_error_layout).setVisibility(View.VISIBLE);
            ((TextView)rootView.findViewById(R.id.error_message)).setText(R.string.error_log_with_facebook);
            //errorMessage.setVisibility(View.VISIBLE);
            errorMessage.findViewById(R.id.retry).setVisibility(View.GONE);
        }
    }


    public class ListDownloader extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
//            Log.i("albums",s);
            rootView.findViewById(R.id.loading_panel).setVisibility(View.GONE);
            rootView.findViewById(R.id.general_error_panel).setVisibility(View.GONE);
            if (s == null || s.equals("NON_CONNECTED_TO_INTERNET_ERROR") || s.equals("CONNECTION_TO_SERVER_ERROR")) {
                rootView.findViewById(R.id.general_error_panel).setVisibility(View.VISIBLE);
                return;
            }
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