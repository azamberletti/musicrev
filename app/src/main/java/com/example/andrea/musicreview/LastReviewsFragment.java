package com.example.andrea.musicreview;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.andrea.musicreview.Interfaces.DetailOpener;
import com.example.andrea.musicreview.Interfaces.Downloader;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class LastReviewsFragment extends ListFragment implements View.OnClickListener {

    private RelativeLayout errorMessage;
    private DetailOpener detailOpener;
    private Downloader downloader;
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
        /*connectivityChangeReceiver = new ConnectivityChangeReceiver();
        getActivity().registerReceiver(
                connectivityChangeReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));*/
        new ListDownloader().execute(URL);
    }

    @Override
    public void onPause() {
        super.onPause();
        //getActivity().unregisterReceiver(connectivityChangeReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_last_reviews, container, false);
        setListAdapter(new AlbumListAdapter(getActivity(), R.layout.album_list_item_layout, new ArrayList<Album>()));
        errorMessage = (RelativeLayout)rootView.findViewById(R.id.general_error_panel);
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


    @Override
    public void onClick(View v) {
        new ListDownloader().execute();
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        detailOpener.OpenAlbumReviewDetail(((Album)list.getItemAtPosition(position)).getId());
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
            return downloader.DownloadFromURL(params[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getView().findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);
        }
    }
}
