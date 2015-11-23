package com.example.andrea.musicreview.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.interfaces.DetailOpener;
import com.example.andrea.musicreview.model.Artist;
import com.example.andrea.musicreview.utility.ConnectionHandler;
import com.example.andrea.musicreview.view.ArtistListAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ArtistSearchFragment extends ListFragment implements View.OnClickListener {
    private LinearLayout errorMessage;
    private DetailOpener detailOpener;
    private ViewGroup rootView;
    String request;
    //    private ConnectivityChangeReceiver connectivityChangeReceiver;
    private final static String URL = "http://www.saltedmagnolia.com/search_artist.php?key_words=";


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//
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
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Search an artist");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_artist_search, container, false);
        (rootView.findViewById(R.id.loading_panel)).setVisibility(View.GONE);
        setListAdapter(new ArtistListAdapter(getActivity(), R.layout.artist_list_item_layout));
        EditText editText = (EditText) rootView.findViewById(R.id.artist_search_bar);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    (rootView.findViewById(R.id.loading_panel)).setVisibility(View.VISIBLE);
                    request = URL + ((EditText) getView().findViewById(R.id.artist_search_bar)).getText();
                    request = request.replace(" ", "%20");
                    new ListDownloader().execute(request);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        errorMessage = (LinearLayout)rootView.findViewById(R.id.general_error_panel);
        errorMessage.setOnClickListener(this);
        errorMessage.setVisibility(View.GONE);
        (rootView.findViewById(R.id.loading_panel)).setVisibility(View.GONE);
        return rootView;
    }

    private List<Artist.ArtistBasicInfo> parse(String s) throws JSONException, ParseException {
        JSONArray array = new JSONArray(s);
        List<Artist.ArtistBasicInfo> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(new Artist.ArtistBasicInfo(array.getJSONObject(i)));
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
        detailOpener.OpenArtistBio(((Artist.ArtistBasicInfo) list.getItemAtPosition(position)).getId());
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
                ((ArtistListAdapter) getListAdapter()).refreshList(parse(s));
                Log.i("RESPONSE_FROM_SEARCH", s);
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
            return ConnectionHandler.DownloadFromURL(params[0], getContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rootView.findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);
        }
    }
}
