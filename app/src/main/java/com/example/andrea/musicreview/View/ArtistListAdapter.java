package com.example.andrea.musicreview.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.model.Artist;

import java.util.ArrayList;
import java.util.Collection;

public class ArtistListAdapter extends ArrayAdapter<Artist.ArtistBasicInfo> {
    private int rowLayout;

    public ArtistListAdapter(Context context, int resource) {
        super(context, resource, new ArrayList<Artist.ArtistBasicInfo>());
        this.rowLayout = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(rowLayout, null);
            mViewHolder = new ViewHolder();
            mViewHolder.artistTitle = (TextView) convertView
                    .findViewById(R.id.name);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        Artist.ArtistBasicInfo artist = getItem(position);
        mViewHolder.artistTitle.setText(artist.getName());
        return convertView;
    }

    public void refreshList(Collection<Artist.ArtistBasicInfo> updatedModelList) {
        clear();
        addAll(updatedModelList);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView artistTitle;
    }
}
