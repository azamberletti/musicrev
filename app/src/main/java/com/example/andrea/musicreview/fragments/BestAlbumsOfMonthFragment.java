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
import android.widget.LinearLayout;

import com.example.andrea.musicreview.view.AlbumGridAdapter;
import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.interfaces.DetailOpener;
import com.example.andrea.musicreview.interfaces.Downloader;
import com.example.andrea.musicreview.model.Album;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class BestAlbumsOfMonthFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener {

    private ViewGroup rootView;
    private LinearLayout errorMessage;
    private GridView grid;
    private DetailOpener detailOpener;
    private Downloader downloader;
    private final static String URL = "http://www.saltedmagnolia.com/get_best_of_month.php";

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
                    + " must implement DetailOpener and Downloader");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_best_albums_of_month, container, false);
        grid = (GridView) rootView.findViewById(R.id.grid);
        errorMessage = (LinearLayout) rootView.findViewById(R.id.general_error_panel);
        errorMessage.setVisibility(View.GONE);
        grid.setAdapter(new AlbumGridAdapter(getActivity(), R.layout.best_album_item_layout, new ArrayList<Album.AlbumBasicInfo>()));
        grid.setOnItemClickListener(this);
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
    public void onResume() {
        super.onResume();
        /*connectivityChangeReceiver = new ConnectivityChangeReceiver();
        getActivity().registerReceiver(
                connectivityChangeReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));*/
        new ListDownloader().execute(URL);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        detailOpener.OpenAlbumReviewDetail(((Album.AlbumBasicInfo) parent.getItemAtPosition(position)).getId());
    }

    public class ListDownloader extends AsyncTask<String, Void, String> {

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
                ((AlbumGridAdapter) grid.getAdapter()).refreshList(parse(s));
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
            rootView.findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);
        }
    }



}
