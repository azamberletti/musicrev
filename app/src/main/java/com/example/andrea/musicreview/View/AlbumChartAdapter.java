package com.example.andrea.musicreview.view;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.model.Album;

import java.util.ArrayList;

public class AlbumChartAdapter extends AlbumListAdapter {

    public AlbumChartAdapter(FragmentActivity activity, int best_album_item_layout, ArrayList<Album.AlbumBasicInfo> albumBasicInfos) {
        super(activity, best_album_item_layout, albumBasicInfos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        int chartPosition = position +1;
        TextView title = (TextView) convertView.findViewById(R.id.name);
        title.setText(chartPosition + ". " + title.getText());
        return convertView;
    }
}
