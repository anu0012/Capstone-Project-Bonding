package com.example.anuragsharma.bonding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by anuragsharma on 13/2/17.
 */

public class Forum extends AppCompatActivity {
    private ForumAdapter mAdapter;
    private ListView l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        l = (ListView) findViewById(R.id.forums_list);

        final List<ForumQuestion> questions = new ArrayList<>();

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                // A new comment has been added, add it to the displayed list
                //Toast.makeText(Forum.this,previousChildName,Toast.LENGTH_LONG).show();
                if (previousChildName != null && previousChildName.equals("files")) {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        String name = (String) messageSnapshot.child("title").getValue();
                        String desc = (String) messageSnapshot.child("description").getValue();

                        ForumQuestion d = new ForumQuestion(name, desc, messageSnapshot.getKey());
                        questions.add(d);
                        mAdapter = new ForumAdapter(Forum.this, questions);
                        l.setAdapter(mAdapter);
                    }
                    View loadingIndicator = findViewById(R.id.forum_loading_indicator);
                    loadingIndicator.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

                String key = dataSnapshot.getKey();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                String key = dataSnapshot.getKey();


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                String key = dataSnapshot.getKey();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(Forum.this, R.string.toast_files_failed_to_load,
                        Toast.LENGTH_SHORT).show();
            }
        };
        myRef.addChildEventListener(childEventListener);

        mAdapter = new ForumAdapter(this, questions);
        l.setAdapter(mAdapter);

        mAdapter.clear();
    }
}
