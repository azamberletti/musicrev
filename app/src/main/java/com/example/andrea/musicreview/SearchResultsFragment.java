package com.example.andrea.musicreview;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsFragment extends ListFragment implements View.OnClickListener {
    private LinearLayout errorMessage;
    //    private DetailOpener detailOpener;
//    private ConnectivityChangeReceiver connectivityChangeReceiver;
    private final static String URL = "http://www.saltedmagnolia.com/search_album.php?key_words=";


//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//
//        // This makes sure that the container activity has implemented
//        // the callback interface. If not, it throws an exception
////        try {
////            detailOpener = (DetailOpener) activity;
////        } catch (ClassCastException e) {
////            throw new ClassCastException(activity.toString()
////                    + " must implement DetailOpener");
////        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        /*connectivityChangeReceiver = new ConnectivityChangeReceiver();
        getActivity().registerReceiver(
                connectivityChangeReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));*/
        new ListDownloader().execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        //getActivity().unregisterReceiver(connectivityChangeReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search_results, container, false);
        setListAdapter(new AlbumListAdapter(getActivity(), R.layout.album_list_item_layout, new ArrayList<Album>()));
        errorMessage = (LinearLayout)rootView.findViewById(R.id.general_error_panel);
        errorMessage.setOnClickListener(this);
        errorMessage.setVisibility(View.GONE);
        return rootView;
    }

    private List<Album> parse(String s) throws JSONException, ParseException {
        JSONArray array = new JSONArray(s);
        List<Album> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(new Album(array.getJSONObject(i)));
        }
        return list;
    }

    public String download() {
        if(ConnectionHandler.isConnected(getActivity())) {
            InputStream is = null;
            int len = 10000;
            try {
                Log.i("URL", URL+getArguments().getString("key_words"));
                java.net.URL url = new URL(URL+getArguments().getString("key_words"));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                int response = conn.getResponseCode();
                if (response != HttpURLConnection.HTTP_OK) {
                    throw new IOException();
                }
                is = conn.getInputStream();
                Reader reader = new InputStreamReader(is, "UTF-8");
                char[] buffer = new char[len];
                reader.read(buffer);
                return new String(buffer);
            }  catch (IOException e){
                return "CONNECTION_TO_SERVER_ERROR";
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            return "NON_CONNECTED_TO_INTERNET_ERROR";
        }
    }

    @Override
    public void onClick(View v) {
        new ListDownloader().execute();
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        Log.i("CLick", "CLick");
    }

    public class ListDownloader extends AsyncTask<String, Void, String> {

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
                ((AlbumListAdapter) getListAdapter()).refreshList(parse(s));
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
            return download();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getView().findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);
        }
    }
}
