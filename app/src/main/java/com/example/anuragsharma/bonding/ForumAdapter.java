package com.example.anuragsharma.bonding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by anuragsharma on 13/2/17.
 */

public class ForumAdapter extends ArrayAdapter<ForumQuestion> {
    public ForumAdapter(Context context, List<ForumQuestion> questions) {
        super(context, 0, questions);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.forum_list_item, parent, false);
        }

        ForumQuestion question = getItem(position);
        TextView titleView = (TextView) listItemView.findViewById(R.id.forum_title);
        titleView.setText(question.getTitle());
        TextView titleView2 = (TextView) listItemView.findViewById(R.id.forum_description);
        titleView2.setText(question.getDescription());
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.icon_alphabet);
        imageView.setImageDrawable(question.getTextDrawable());

        return listItemView;
    }
}
