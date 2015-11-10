package com.example.andrea.musicreview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;

public class AlbumListAdapter extends ArrayAdapter<Album> {

    private int rowLayout;

    public AlbumListAdapter(Context context, int resource, List<Album> objects) {
        super(context, resource, objects);
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
            mViewHolder.name = (TextView) convertView
                    .findViewById(R.id.name);
            mViewHolder.surname = (TextView) convertView
                    .findViewById(R.id.artist);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        Album album = getItem(position);
        mViewHolder.name.setText(album.getTitle());
        mViewHolder.surname.setText(album.getArtist().getName());
        return convertView;
    }

    public void refreshList(Collection<Album> updatedModelList) {
        clear();
        addAll(updatedModelList);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView name;
        public TextView surname;
    }
}