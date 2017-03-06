package com.example.anuragsharma.bonding;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anuragsharma on 11/2/17.
 */

public class DiscussionSubmit extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion_submit);
        Button b = (Button) findViewById(R.id.discussionSubmitButton);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        final EditText title = (EditText) findViewById(R.id.discussionTopic);
        final EditText desc = (EditText) findViewById(R.id.discussionDescription);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String titleString = title.getText().toString();
                String descString = desc.getText().toString();
                if (titleString.isEmpty() || descString.isEmpty()) {
                    Toast.makeText(DiscussionSubmit.this, R.string.toast_empty_fields,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DiscussionSubmit.this, R.string.toast_sent, Toast.LENGTH_SHORT).show();
                    ForumQuestion f = new ForumQuestion(titleString, descString, "");
                    myRef.child("forums").push().setValue(f);
                    finish();
                }

            }
        });
    }
}
