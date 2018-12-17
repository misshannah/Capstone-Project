package com.olukoye.hannah.planmywedding.w_venue;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.olukoye.hannah.planmywedding.R;

public class WeddingVenue extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText inputEmail, inputPassword;
    private TextView venueText;
    private ProgressBar progressBar;
    int PLACE_PICKER_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding_venue);
        mAuth = FirebaseAuth.getInstance();
        venueText = (TextView) findViewById(R.id.venue_text);
        // Check if user is signed in (non-null) and update UI accordingly.
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(WeddingVenue.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.getCurrentUser() == null) {
            signup();
        }


    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    private void signup() {

        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.activity_sign_up, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(prompt);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        inputEmail = (EditText) prompt.findViewById(R.id.email);
        inputPassword = (EditText) prompt.findViewById(R.id.password);
        progressBar = (ProgressBar) prompt.findViewById(R.id.progressBar);
        Button signupbtn = (Button) prompt.findViewById(R.id.sign_up_button);
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(WeddingVenue.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(WeddingVenue.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(WeddingVenue.this, "Done!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        alertDialog.dismiss();
                                    }
                                }
                            });

                }

            }
        });


    }

}
