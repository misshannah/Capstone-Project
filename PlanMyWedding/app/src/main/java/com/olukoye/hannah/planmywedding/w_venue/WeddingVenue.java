package com.olukoye.hannah.planmywedding.w_venue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.olukoye.hannah.planmywedding.R;
import com.olukoye.hannah.planmywedding.authentication.SignUp;

public class WeddingVenue extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding_venue);
        mAuth = FirebaseAuth.getInstance();

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        startActivity(new Intent(WeddingVenue.this, SignUp.class));

    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            ((TextView) findViewById(R.id.textSignInStatus)).setText(
                    "Welcome: " + user.getUid());
        } else {
            ((TextView) findViewById(R.id.textSignInStatus)).setText(
                    "Error: sign in failed.");
        }
    }
}
