package com.example.andrea.musicreview.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.model.Artist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

public class ArtistListAdapter extends ArrayAdapter<Artist.ArtistBasicInfo> {
    private int rowLayout;
    private Context context;
    public ArtistListAdapter(Context context, int resource) {
        super(context, resource, new ArrayList<Artist.ArtistBasicInfo>());
        this.rowLayout = resource;
        this.context = context;
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
            mViewHolder.image = (com.example.andrea.musicreview.view.SquareImageView) convertView
                    .findViewById(R.id.artist_image);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        Artist.ArtistBasicInfo artist = getItem(position);
        mViewHolder.artistTitle.setText(artist.getName());
        Picasso.with(context).load("http://www.saltedmagnolia.com/" + artist.getImagePath())
                .resize(140, 140)
                .centerCrop()
                .into(mViewHolder.image);
        return convertView;
    }

    public void refreshList(Collection<Artist.ArtistBasicInfo> updatedModelList) {
        clear();
        addAll(updatedModelList);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView artistTitle;
        public com.example.andrea.musicreview.view.SquareImageView image;
    }
}
