package com.example.anuragsharma.bonding;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by anuragsharma on 25/02/17.
 */

public class DiscussionWidgetRemoteViewService extends RemoteViewsService {
    private ArrayList<LauncherActivity.ListItem> listItemList = new ArrayList<>();
    private Context context = null;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String data = "hello";
    private Map<Integer, String> map = new HashMap<>();
    private Map<Integer, String> map_title = new HashMap<>();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {


        return new RemoteViewsFactory() {

            @Override
            public void onCreate() {
                //Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                map.clear();
                map_title.clear();
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            if (postSnapshot.getKey().equals("forums")) {
                                int i = 0;
                                for (DataSnapshot childSnap : postSnapshot.getChildren()) {

                                    map.put(i, childSnap.getKey());
                                    data = (String) childSnap.child("title").getValue();
                                    map_title.put(i, data);
                                    String description = (String) childSnap.child("description").getValue();
                                    i++;
                                }

                            }


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message

                        // ...
                    }
                });

                final long identityToken = Binder.clearCallingIdentity();
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                return map.size();
            }

            @Override
            public RemoteViews getViewAt(int position) {

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_discussion_item);

                String topic = map_title.get(map_title.size() - 1 - position);

                views.setTextViewText(R.id.widget_discussion_thread_textview, topic);

                ChildKey key = new ChildKey(map.get(map.size() - 1 - position));
                final Intent intent = new Intent(getApplicationContext(), DiscussionScreen.class);
                intent.putExtra("key", key);
                views.setOnClickFillInIntent(R.id.widget_list_item, intent);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                // return new RemoteViews(getPackageName(), R.layout.widget_discussion_item);
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}