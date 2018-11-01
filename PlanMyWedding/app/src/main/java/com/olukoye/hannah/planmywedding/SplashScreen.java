package com.olukoye.hannah.planmywedding;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.splash_screen);


        new Handler().postDelayed(new Runnable() {


            @Override

            public void run() {

                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                finish();

            }

        }, 3*1000); // wait for 5 seconds
    }
}
