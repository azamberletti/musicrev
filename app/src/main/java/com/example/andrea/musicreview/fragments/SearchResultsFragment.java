package com.example.andrea.musicreview.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.andrea.musicreview.utility.ConnectionHandler;
import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.interfaces.DetailOpener;
import com.example.andrea.musicreview.model.Album;
import com.example.andrea.musicreview.view.AlbumListAdapter;

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
    private DetailOpener detailOpener;
//  private ConnectivityChangeReceiver connectivityChangeReceiver;
    private final static String URL = "http://www.saltedmagnolia.com/search_album.php?key_words=";
    List<Album.AlbumBasicInfo> albums = new ArrayList<>();

    @Override
    public void onAttach(Activity activity) {
       super.onAttach(activity);
//
//        // This makes sure that the container activity has implemented
//        // the callback interface. If not, it throws an exception
       try {
            detailOpener = (DetailOpener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DetailOpener");
        }
  }

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
        setListAdapter(new AlbumListAdapter(getActivity(), R.layout.album_list_item_layout, new ArrayList<Album.AlbumBasicInfo>()));
        errorMessage = (LinearLayout)rootView.findViewById(R.id.general_error_panel);
        errorMessage.setOnClickListener(this);
        errorMessage.setVisibility(View.GONE);
        return rootView;
    }

    private List<Album.AlbumBasicInfo> parse(String s) throws JSONException, ParseException {
        JSONArray array = new JSONArray(s);
        List<Album.AlbumBasicInfo> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(new Album.AlbumBasicInfo(array.getJSONObject(i)));
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        new ListDownloader().execute();
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        detailOpener.OpenAlbumReviewDetail(((Album.AlbumBasicInfo) list.getItemAtPosition(position)).getId());
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
                List<Album.AlbumBasicInfo> searchResults = parse(s);
                albums = searchResults;
                if(searchResults.isEmpty()){
                    getView().findViewById(R.id.no_result_alert).setVisibility(View.VISIBLE);
                } else {
                    ((AlbumListAdapter) getListAdapter()).refreshList(albums);
                    getView().findViewById(R.id.no_result_alert).setVisibility(View.GONE);
                }


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
            return ConnectionHandler.DownloadFromURL(URL+getArguments().getString("key_words"), getContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getView().findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);
        }
    }
}
