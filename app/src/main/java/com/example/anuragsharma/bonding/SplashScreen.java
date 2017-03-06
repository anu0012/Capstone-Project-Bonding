package com.example.anuragsharma.bonding;

/**
 * Created by anuragsharma on 10/2/17.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Thread t = new Thread() {
            public void run() {

                try {
                    sleep(2000);

                    Intent intent = new Intent(SplashScreen.this, Register.class);
                    startActivity(intent);

                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        t.start();
    }
}
