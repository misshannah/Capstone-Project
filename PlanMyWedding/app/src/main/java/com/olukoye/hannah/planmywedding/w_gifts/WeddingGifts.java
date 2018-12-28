package com.olukoye.hannah.planmywedding.w_gifts;


import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.olukoye.hannah.planmywedding.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeddingGifts extends AppCompatActivity implements GiftRecyclerViewAdapter.ClickListener, AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth;
    private EditText inputEmail, inputPassword;
    private ProgressBar progressBar;
    GiftDatabase giftDatabase;
    RecyclerView recyclerView;
    Spinner spinner;
    GiftRecyclerViewAdapter giftRecyclerViewAdapter;
    FloatingActionButton floatingActionButton;
    private String[] categories = {
            "All",
            "Family",
            "Friends",
            "Workmates",
            "Others"
    };

    ArrayList<Gifts> giftsArrayList = new ArrayList<>();
    ArrayList<String> spinnerList = new ArrayList<>(Arrays.asList(categories));

    public static final int NEW_GUESTS_REQUEST_CODE = 200;
    public static final int UPDATE_GUESTS_REQUEST_CODE = 300;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding_gifts);
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.


        floatingActionButton = findViewById(R.id.addGuest);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        recyclerView = findViewById(R.id.guestView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        giftRecyclerViewAdapter = new GiftRecyclerViewAdapter(this);
        recyclerView.setAdapter(giftRecyclerViewAdapter);

        giftDatabase = Room.databaseBuilder(getApplicationContext(), GiftDatabase.class, GiftDatabase.DB_NAME).fallbackToDestructiveMigration().build();

        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(WeddingGifts.this, GiftDetailsActivity.class), NEW_GUESTS_REQUEST_CODE);
            }
        });

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
                            .addOnCompleteListener(WeddingGifts.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(WeddingGifts.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(WeddingGifts.this, "Done!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        alertDialog.dismiss();
                                    }
                                }
                            });

                }

            }
        });


    }


    @Override
    public void launchIntent(int id) {
        startActivityForResult(new Intent(WeddingGifts.this, GiftDetailsActivity.class).putExtra("id", id), UPDATE_GUESTS_REQUEST_CODE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (position == 0) {
            loadAllGifts();
        } else {
            String string = parent.getItemAtPosition(position).toString();
            loadFilteredGifts(string);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @SuppressLint("StaticFieldLeak")
    private void loadFilteredGifts(String category) {
        new AsyncTask<String, Void, List<Gifts>>() {
            @Override
            protected List<Gifts> doInBackground(String... params) {
                return giftDatabase.daoAccess().fetchGiftsListByCategory(params[0]);

            }

            @Override
            protected void onPostExecute(List<Gifts> giftsList) {
                giftRecyclerViewAdapter.updateGiftsList(giftsList);
            }
        }.execute(category);

    }


    @SuppressLint("StaticFieldLeak")
    private void fetchGiftsByIdAndInsert(int id) {
        new AsyncTask<Integer, Void, Gifts>() {
            @Override
            protected Gifts doInBackground(Integer... params) {
                return giftDatabase.daoAccess().fetchGiftsListById(params[0]);

            }

            @Override
            protected void onPostExecute(Gifts giftsList) {
                giftRecyclerViewAdapter.addRow(giftsList);
            }
        }.execute(id);

    }
    @SuppressLint("StaticFieldLeak")
    private void insertList(List<Gifts> giftsList) {
        new AsyncTask<List<Gifts>, Void, Void>() {
            @Override
            protected Void doInBackground(List<Gifts>... params) {
                giftDatabase.daoAccess().insertGiftsList(params[0]);

                return null;

            }

            @Override
            protected void onPostExecute(Void voids) {
                super.onPostExecute(voids);
            }
        }.execute(giftsList);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            //reset spinners
            spinner.setSelection(0);

            if (requestCode == NEW_GUESTS_REQUEST_CODE) {
                long id = data.getLongExtra("id", -1);
                Toast.makeText(getApplicationContext(), "Row inserted", Toast.LENGTH_SHORT).show();
                fetchGiftsByIdAndInsert((int) id);

            } else if (requestCode == UPDATE_GUESTS_REQUEST_CODE) {

                boolean isDeleted = data.getBooleanExtra("isDeleted", false);
                int number = data.getIntExtra("number", -1);
                if (isDeleted) {
                    Toast.makeText(getApplicationContext(), number + " rows deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), number + " rows updated", Toast.LENGTH_SHORT).show();
                }

                loadAllGifts();

            }


        } else {
            Toast.makeText(getApplicationContext(), "No action done by user", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void loadAllGifts() {
        new AsyncTask<String, Void, List<Gifts>>() {
            @Override
            protected List<Gifts> doInBackground(String... params) {
                return giftDatabase.daoAccess().fetchAllGifts();
            }

            @Override
            protected void onPostExecute(List<Gifts> giftsList) {
                giftRecyclerViewAdapter.updateGiftsList(giftsList);
            }
        }.execute();
    }

}


