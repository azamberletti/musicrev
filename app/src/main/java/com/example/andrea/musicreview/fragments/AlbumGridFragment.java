package com.example.andrea.musicreview.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.interfaces.DetailOpener;
import com.example.andrea.musicreview.model.Album;
import com.example.andrea.musicreview.view.AlbumGridAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class AlbumGridFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener {


    private ViewGroup rootView;
    private LinearLayout errorMessage;
    private GridView grid;
    protected DetailOpener detailOpener;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_album_grid, container, false);
        grid = (GridView) rootView.findViewById(R.id.grid);
        errorMessage = (LinearLayout) rootView.findViewById(R.id.general_error_panel);
        errorMessage.setVisibility(View.GONE);
        grid.setAdapter(new AlbumGridAdapter(getActivity(), R.layout.album_grid_item_layout, new ArrayList<Album.AlbumBasicInfo>()));
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        detailOpener.OpenAlbumReviewDetail(((Album.AlbumBasicInfo) parent.getItemAtPosition(position)).getId());
    }

    protected void setSource(String s){
        try {
            ((AlbumGridAdapter) grid.getAdapter()).refreshList(parse(s));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("ERROR", "JSON_EXCEPTION");
            errorMessage.setVisibility(View.VISIBLE);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("ERROR", "PARSE_EXCEPTION");
        }
    }

}
