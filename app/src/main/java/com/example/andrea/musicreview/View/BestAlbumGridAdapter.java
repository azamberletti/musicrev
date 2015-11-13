package com.example.andrea.musicreview.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.model.Album;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.List;

/**
 * Created by luca-campana on 13/11/15.
 */
public class BestAlbumGridAdapter extends ArrayAdapter<Album.AlbumBasicInfo> {

    private int rowLayout;
    private Context context;

    public BestAlbumGridAdapter(Context context, int resource, List<Album.AlbumBasicInfo> objects) {
        super(context, resource, objects);
        this.context = context;
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
            mViewHolder.albumTitle = (TextView) convertView
                    .findViewById(R.id.name);
            mViewHolder.artist = (TextView) convertView
                    .findViewById(R.id.artist);
            mViewHolder.albumCover = (ImageView) convertView
                    .findViewById(R.id.album_cover);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        Album.AlbumBasicInfo album = getItem(position);
        mViewHolder.albumTitle.setText(album.getTitle());
        mViewHolder.artist.setText(album.getArtist().getName());
        Picasso.with(context).load("http://www.saltedmagnolia.com/" + album.getImageURL())
                .into( mViewHolder.albumCover);
        return convertView;
    }

    public void refreshList(Collection<Album.AlbumBasicInfo> updatedModelList) {
        clear();
        addAll(updatedModelList);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView albumTitle;
        public TextView artist;
        public ImageView albumCover;
    }

}
