package com.olukoye.hannah.planmywedding.w_venue;

import android.content.Context;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.olukoye.hannah.planmywedding.R;
import com.olukoye.hannah.planmywedding.authentication.SignUp;

public class WeddingVenue extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText inputEmail, inputPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding_venue);
        mAuth = FirebaseAuth.getInstance();
        signup();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);


    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            ((TextView) findViewById(R.id.textSignInStatus)).setText(
                    "Welcome: " + user.getDisplayName());
        } else {
            signup();
        }
    }

    private void signup() {

        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.activity_sign_up, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(prompt);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        Button signup = (Button) findViewById(R.id.sign_in_button);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setCancelable(false);
        signup.setOnClickListener(new View.OnClickListener() {
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
                                    Toast.makeText(WeddingVenue.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(WeddingVenue.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        startActivity(new Intent(WeddingVenue.this, WeddingVenue.class));
                                        finish();
                                    }
                                }
                            });

                }

            }
        });


        alertDialogBuilder.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }
        });

        alertDialogBuilder.show();
    }

}
