package com.example.anuragsharma.bonding;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.anuragsharma.bonding.News.NewsMainActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ForumAdapter mAdapter;
    private ListView l;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String email = "";
    private String name = "";

    InterstitialAd mInterstitialAd;
    Boolean mAdsOnScreen;
    private FirebaseAnalytics mFirebaseAnalytics;
    private TextView mEmptyStateTextView;

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    Toast.makeText(Home.this, R.string.toast_signout,
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Home.this, Login.class));
                    finish();
                }
            }
        };

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName();
            email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }
        setContentView(R.layout.activity_home);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_home_swipe_refresh_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, DiscussionSubmit.class);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "fab");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "ADD CONTENT BUTTON");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //forum

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        l = (ListView) findViewById(R.id.home_list);

        final List<ForumQuestion> questions = new ArrayList<>();

        final ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                // A new comment has been added, add it to the displayed list
                //Toast.makeText(Forum.this,previousChildName,Toast.LENGTH_LONG).show();
                if (dataSnapshot.getKey().equals("forums")) {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {

                        String name = (String) messageSnapshot.child("title").getValue();
                        String desc = (String) messageSnapshot.child("description").getValue();
                        TextDrawable drawable = null;
                        //if(name.charAt(0) == 'A')
                        ColorGenerator generator = ColorGenerator.MATERIAL;
                        int customColor = generator.getColor("user@gmail.com");
                        int customColor2 = generator.getColor("#FFA500");
                        char ch = Character.toUpperCase(name.charAt(0));
                        if (ch == 'a' || ch == 'A' || ch == 'd' || ch == 'D' || ch == 'F' || ch == 'f')
                            drawable = TextDrawable.builder()
                                    .buildRound(Character.toString(ch), 0xffffd54f); //yellow
                        else if (ch == 'b' || ch == 'B' || ch == 'c' || ch == 'C' || ch == 'e' || ch == 'E')
                            drawable = TextDrawable.builder()
                                    .buildRound(Character.toString(ch), 0xffba68c8);
                        else if (ch == 'g' || ch == 'G' || ch == 'x' || ch == 'X' || ch == 'z' || ch == 'Z')
                            drawable = TextDrawable.builder()
                                    .buildRound(Character.toString(ch), 0xffffb74d);
                        else if (ch == 'y' || ch == 'Y' || ch == 'o' || ch == 'O' || ch == 'h' || ch == 'H')
                            drawable = TextDrawable.builder()
                                    .buildRound(Character.toString(ch), 0xff81c784); //green
                        else if (ch == 'm' || ch == 'M' || ch == 'n' || ch == 'N' || ch == 'j' || ch == 'J')
                            drawable = TextDrawable.builder()
                                    .buildRound(Character.toString(ch), 0xffff8a65);
                        else if (ch == 'p' || ch == 'P' || ch == 'u' || ch == 'U' || ch == 'w' || ch == 'W')
                            drawable = TextDrawable.builder()
                                    .buildRound(Character.toString(ch), 0xff64b5f6); //light blue
                        else if (ch == 'i' || ch == 'I' || ch == 'k' || ch == 'K' || ch == 'l' || ch == 'L')
                            drawable = TextDrawable.builder()
                                    .buildRound(Character.toString(ch), 0xffa1887f); //gray
                        else if (ch == 'q' || ch == 'Q' || ch == 't' || ch == 'T' || ch == 's' || ch == 'S')
                            drawable = TextDrawable.builder()
                                    .buildRound(Character.toString(ch), 0xffe57373); //salmon
                        else if (ch == 'r' || ch == 'R' || ch == 'v' || ch == 'V')
                            drawable = TextDrawable.builder()
                                    .buildRound(Character.toString(ch), 0xff90a4ae); //brown

                        ForumQuestion d = new ForumQuestion(name, desc, messageSnapshot.getKey(), drawable);
                        questions.add(d);

                    }
                    Collections.reverse(questions);
                    mAdapter = new ForumAdapter(Home.this, questions);
                    l.setAdapter(mAdapter);
                    View loadingIndicator = findViewById(R.id.home_loading_indicator);
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

                Toast.makeText(Home.this, R.string.toast_files_failed_to_load,
                        Toast.LENGTH_SHORT).show();
            }
        };
        myRef.addChildEventListener(childEventListener);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //refreshContent();
                mAdapter.clear();
                myRef.addChildEventListener(childEventListener);
                mSwipeRefreshLayout.setRefreshing(false);

            }

        });

        Toast.makeText(Home.this, R.string.toast_loading,
                Toast.LENGTH_SHORT).show();

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            Toast.makeText(Home.this, R.string.toast_loading,
                    Toast.LENGTH_SHORT).show();

            //View loadingIndicator = findViewById(R.id.home_loading_indicator);
            //loadingIndicator.setVisibility(View.VISIBLE);

            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            mAdView.loadAd(adRequest);

            // Instantiate the InterstitialAd object
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

            // Create the AdListener
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    requestNewInterstitial();
                    mAdsOnScreen = false;
                }
            });

            requestNewInterstitial();

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.home_loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        mAdapter = new ForumAdapter(this, questions);
        l.setAdapter(mAdapter);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ForumQuestion c = mAdapter.getItem(position);
                ChildKey key = new ChildKey(c.getKey());
                Intent intent = new Intent(Home.this, DiscussionScreen.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });

        mAdapter.clear();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        TextView t = (TextView) findViewById(R.id.current_user_email);
        TextView t1 = (TextView) findViewById(R.id.current_user);
        //t1.setText(name);
        if (t != null)
            t.setText(email);
        else
            Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();

        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = getIntent();
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_forum) {
            // invisible (no need)
            //Intent intent = new Intent(Home.this,Forum.class);
            //startActivity(intent);
        } else if (id == R.id.nav_upload) {
            Intent intent = new Intent(Home.this, Upload.class);
            startActivity(intent);
        } else if (id == R.id.nav_download) {
            Intent intent = new Intent(Home.this, Download.class);
            startActivity(intent);
        } else if (id == R.id.nav_chat) {
            // invisible (coming soon)
            //Toast.makeText(Home.this, "Coming Soon",
            //Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Share the books and knowledge with your fellow students. Download Bonding App";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (id == R.id.nav_exit) {
            System.exit(0);
        } else if (id == R.id.nav_book) {
            Intent intent = new Intent(Home.this, ShareBook.class);
            startActivity(intent);
        } else if (id == R.id.nav_news) {
            Intent intent = new Intent(Home.this, NewsMainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_signout) {
            //mAuth.signOut();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Home.this, Login.class));
            finish();
        } else if (id == R.id.nav_booksList) {

            Intent intent = new Intent(Home.this, BooksCategory.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        this.finish();
        Intent intent = new Intent(Home.this, Home.class);
        startActivity(intent);
    }
}
