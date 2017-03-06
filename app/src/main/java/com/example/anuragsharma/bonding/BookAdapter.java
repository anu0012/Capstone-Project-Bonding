package com.example.anuragsharma.bonding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuragsharma on 18/2/17.
 */

public class BookAdapter extends ArrayAdapter<BookDetails> {

    public BookAdapter(Context context, List<BookDetails> bookDetailses) {
        super(context, 0, bookDetailses);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        BookDetails book = getItem(position);
        TextView titleView = (TextView) listItemView.findViewById(R.id.textview_bookname);
        titleView.setText(book.getBookName());
        TextView titleView2 = (TextView) listItemView.findViewById(R.id.textview_bookowner);
        titleView2.setText(book.getOwnerName());

        return listItemView;
    }
}
