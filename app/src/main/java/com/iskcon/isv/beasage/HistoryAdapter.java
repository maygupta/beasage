package com.iskcon.isv.beasage;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by mayank on 1/13/17.
 */
public class HistoryAdapter extends ArrayAdapter<History> {

    List<History> mediaList;

    public HistoryAdapter(Context context, List<History> objects) {
        super(context, 0, objects);
        mediaList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        History media = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.media_layout, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.song_name);
        TextView tvSongType = (TextView) convertView.findViewById(R.id.tvSongType);
        TextView tvCount = (TextView) convertView.findViewById(R.id.tvCount);

        tvSongType.setText(media.mDate);
        textView.setText(media.mBook);
        tvCount.setText(media.mCount + " " + (media.mIsPage ? "pages" : "slokas"));

        ImageView ivAuthorImage = (ImageView) convertView.findViewById(R.id.ivAuthorImage);

        Picasso.with(getContext()).load(media.mUrl).into(ivAuthorImage);

        return convertView;

    }

}
