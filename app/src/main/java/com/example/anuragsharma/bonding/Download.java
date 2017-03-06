package com.example.anuragsharma.bonding;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by anuragsharma on 11/2/17.
 */

public class Download extends AppCompatActivity {
    SwipeRefreshLayout mSwipeRefreshLayout;
    private DownloadAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private static final int DOWNLOAD_LOADER_ID = 1;
    private ArrayList<DownloadURLs> downloadURLses = new ArrayList<>();
    private ListView l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.download_swipe_refresh_layout);
        l = (ListView) findViewById(R.id.files_list);
        mEmptyStateTextView = (TextView) findViewById(R.id.download_empty_view);
        l.setEmptyView(mEmptyStateTextView);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference();
            //ArrayList<DownloadURLs> downloadURLses = new ArrayList<>();
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        if (postSnapshot.getKey().equals("files")) {

                            for (DataSnapshot childSnap : postSnapshot.getChildren()) {

                                //System.out.println(child);
                                String name = (String) childSnap.child("fileName").getValue();
                                String branch = (String) childSnap.child("fileBranch").getValue();
                                //String size = (String) childSnap.child("size").getValue();
                                DownloadURLs d = new DownloadURLs(name, branch);
                                downloadURLses.add(d);
                            }
                            Collections.reverse(downloadURLses);
                            mAdapter = new DownloadAdapter(Download.this, downloadURLses);
                            l.setAdapter(mAdapter);
                            View loadingIndicator = findViewById(R.id.download_loading_indicator);
                            loadingIndicator.setVisibility(View.GONE);
                        }


                    }

                    //System.out.println(downloadURLses.size());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message

                    // ...
                }
            });

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //refreshContent();
                    mAdapter.clear();

                    //reload data
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                if (postSnapshot.getKey().equals("files")) {

                                    for (DataSnapshot childSnap : postSnapshot.getChildren()) {

                                        //System.out.println(child);
                                        String name = (String) childSnap.child("fileName").getValue();
                                        String branch = (String) childSnap.child("fileBranch").getValue();
                                        DownloadURLs d = new DownloadURLs(name, branch);
                                        downloadURLses.add(d);

                                    }
                                    Collections.reverse(downloadURLses);
                                    mAdapter = new DownloadAdapter(Download.this, downloadURLses);
                                    l.setAdapter(mAdapter);
                                    View loadingIndicator = findViewById(R.id.download_loading_indicator);
                                    loadingIndicator.setVisibility(View.GONE);
                                }


                            }

                            //System.out.println(downloadURLses.size());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message

                            // ...
                        }
                    });

                    //
                    mSwipeRefreshLayout.setRefreshing(false);

                }

            });

            l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(Download.this, R.string.toast_downloading, Toast.LENGTH_LONG).show();
                    DownloadURLs d = mAdapter.getItem(position);
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    // Create a storage reference from our app
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://bonding-bad75.appspot.com");
                    StorageReference pathReference = storageRef.child(d.getUrl());
                    File localFile = null;
                    try {
                        //TextView t = (TextView) findViewById(R.id.file_title);
                        localFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), d.getUrl());
                    } catch (Exception e) {
                        System.out.print(0);
                    }
                    pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Local temp file has been created

                            Toast.makeText(Download.this, R.string.toast_downloaded,
                                    Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }
            });

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.download_loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }


    }

}
