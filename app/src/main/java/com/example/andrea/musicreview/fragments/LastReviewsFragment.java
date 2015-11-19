package com.example.andrea.musicreview.fragments;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.activities.LoginActivity;
import com.example.andrea.musicreview.interfaces.DetailOpener;
import com.example.andrea.musicreview.interfaces.Downloader;
import com.example.andrea.musicreview.model.Album;
import com.example.andrea.musicreview.utility.FacebookInformationHelper;
import com.example.andrea.musicreview.view.AlbumListAdapter;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class LastReviewsFragment extends ListFragment implements View.OnClickListener {

    private RelativeLayout errorMessage;
    private DetailOpener detailOpener;
    private Downloader downloader;
    private ViewGroup rootView;
//    private ConnectivityChangeReceiver connectivityChangeReceiver;
    private final static String URL = "http://www.saltedmagnolia.com/get_last_5_albums.php";


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
    public void onResume() {
        super.onResume();
        new ListDownloader().execute(URL);
        //new FacebookInformationHelper(getActivity()).sendInformation();
    }

    @Override
    public void onPause() {
        super.onPause();
        //getActivity().unregisterReceiver(connectivityChangeReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_last_reviews, container, false);
        setListAdapter(new AlbumListAdapter(getActivity(), R.layout.album_list_item_layout, new ArrayList<Album.AlbumBasicInfo>()));
        errorMessage = (RelativeLayout)rootView.findViewById(R.id.general_error_panel);
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
        switch(v.getId()){
            case R.id.general_error_panel:
                new ListDownloader().execute(URL);
                break;
        }
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);
        detailOpener.OpenAlbumReviewDetail(((Album.AlbumBasicInfo)list.getItemAtPosition(position)).getId());
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
            return downloader.DownloadFromURL(params[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rootView.findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);
        }
    }
}
