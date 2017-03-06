package com.example.anuragsharma.bonding;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by anuragsharma on 16/2/17.
 */

public class DiscussionScreen extends AppCompatActivity implements Serializable {
    SwipeRefreshLayout mSwipeRefreshLayout;
    private CommentAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private static final int DOWNLOAD_LOADER_ID = 1;
    private ArrayList<Comment> comments = new ArrayList<>();
    private ListView l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion_screen);
        final EditText commentEditText = (EditText) findViewById(R.id.edittext_comment);
        Button commentSubmit = (Button) findViewById(R.id.button_comment_submit);
        final TextView topic = (TextView) findViewById(R.id.topic);
        final TextView desc = (TextView) findViewById(R.id.description);
        final ChildKey childKey = (ChildKey) getIntent().getSerializableExtra("key");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    if (postSnapshot.getKey().equals("forums")) {

                        for (DataSnapshot childSnap : postSnapshot.getChildren()) {
                            if (childSnap.getKey().equals(childKey.getKey())) {

                                String name = (String) childSnap.child("title").getValue();
                                String description = (String) childSnap.child("description").getValue();
                                topic.setText(name);
                                desc.setText(description);
                            }
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


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.comments_swipe_refresh_layout);
        l = (ListView) findViewById(R.id.comments_list);

        final ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                if (dataSnapshot.getKey().equals("forums")) {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        if (messageSnapshot.getKey().equals(childKey.getKey())) {

                            for (DataSnapshot childSnapshot : messageSnapshot.getChildren()) {
                                if (childSnapshot.getKey().equals("comments")) {
                                    for (DataSnapshot commentSnapshot : childSnapshot.getChildren()) {
                                        String comment = (String) commentSnapshot.child("comment").getValue();
                                        String user = (String) commentSnapshot.child("username").getValue();
                                        Comment d = new Comment(comment, user);
                                        comments.add(d);
                                    }
                                    Collections.reverse(comments);
                                    mAdapter = new CommentAdapter(DiscussionScreen.this, comments);
                                    l.setAdapter(mAdapter);
                                }
                            }
                        }
                    }

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

                Toast.makeText(DiscussionScreen.this, R.string.toast_comments_failed_to_load,
                        Toast.LENGTH_SHORT).show();
            }
        };
        myRef.addChildEventListener(childEventListener);

        // Comment Submit OnClickListener
        commentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentData = commentEditText.getText().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userData = user.getEmail();
                if (commentData.isEmpty()) {
                    Toast.makeText(DiscussionScreen.this, R.string.toast_empty_fields,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Comment c = new Comment(commentData, userData);
                    myRef.child("forums").child(childKey.getKey()).child("comments").push().setValue(c);
                    commentEditText.setText("");
                    if (mAdapter != null)
                        mAdapter.clear();
                    myRef.addChildEventListener(childEventListener);
                    //hide keyboard
                    InputMethodManager inputMethodManager =
                            (InputMethodManager) DiscussionScreen.this.getSystemService(
                                    Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(
                            DiscussionScreen.this.getCurrentFocus().getWindowToken(), 0);
                }
            }
        });
        //View loadingIndicator = findViewById(R.id.home_loading_indicator);
        //loadingIndicator.setVisibility(View.GONE);

        // Comments Refresh using SwipeRefreshLayout
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //refreshContent();
                if (mAdapter != null)
                    mAdapter.clear();
                myRef.addChildEventListener(childEventListener);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
