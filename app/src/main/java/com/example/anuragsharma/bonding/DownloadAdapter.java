package com.example.anuragsharma.bonding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by anuragsharma on 11/2/17.
 */

public class DownloadAdapter extends ArrayAdapter<DownloadURLs> {

    public DownloadAdapter(Context context, List<DownloadURLs> urls) {
        super(context, 0, urls);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.download_list_item, parent, false);
        }

        DownloadURLs url = getItem(position);
        TextView titleView = (TextView) listItemView.findViewById(R.id.file_title);
        titleView.setText(url.getUrl());
        TextView titleView1 = (TextView) listItemView.findViewById(R.id.file_category);
        titleView1.setText(url.getSection());

        return listItemView;
    }
}
