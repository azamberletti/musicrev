package com.example.andrea.musicreview.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.utility.ConnectionHandler;
import com.example.andrea.musicreview.utility.MyLoginManager;
import com.facebook.AccessToken;

import org.w3c.dom.Text;

public class FavoriteFragment extends AlbumGridFragment {

    private final static String URL = "http://www.saltedmagnolia.com/get_favorite_albums.php?user_id=";
    private View rootView;
    private MyLoginManager myLoginManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        rootView.findViewById(R.id.loading_panel).setVisibility(View.GONE);
        myLoginManager = new MyLoginManager(getContext());
        //TextView title = (TextView) rootView.findViewById(R.id.grid_title);
        //title.setText(R.string.favorite_albums);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*connectivityChangeReceiver = new ConnectivityChangeReceiver();
        getActivity().registerReceiver(
                connectivityChangeReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));*/
        String id = myLoginManager.getUserID();
        if (id!=null) {
            new ListDownloader().execute(URL + myLoginManager.getUserID());
        } else {
            View errorMessage = rootView.findViewById(R.id.general_error_panel);
            ((TextView) errorMessage.findViewById(R.id.error_message))
                    .setText("You must be logged in to see this section");
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.findViewById(R.id.retry).setVisibility(View.GONE);
        }
    }

    public class ListDownloader extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            rootView.findViewById(R.id.loading_panel).setVisibility(View.GONE);
            View errorMessage = rootView.findViewById(R.id.general_error_panel);
            if (s.equals("NON_CONNECTED_TO_INTERNET_ERROR") || s.equals("CONNECTION_TO_SERVER_ERROR")) {
                Log.i("ERROR", s);
                errorMessage.setVisibility(View.VISIBLE);
                return;
            }
            errorMessage.setVisibility(View.GONE);
            setSource(s);
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
