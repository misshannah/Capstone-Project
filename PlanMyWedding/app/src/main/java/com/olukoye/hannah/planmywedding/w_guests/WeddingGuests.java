package com.olukoye.hannah.planmywedding.w_guests;


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

public class WeddingGuests extends AppCompatActivity implements RecyclerViewAdapter.ClickListener, AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth;
    private EditText inputEmail, inputPassword;
    private ProgressBar progressBar;
    MyDatabase myDatabase;
    RecyclerView recyclerView;
    Spinner spinner;
    RecyclerViewAdapter recyclerViewAdapter;
    FloatingActionButton floatingActionButton;
    private String[] categories = {
            "All",
            "Family",
            "Friends",
            "Workmates",
            "Others"
    };

    ArrayList<Guests> guestsArrayList = new ArrayList<>();
    ArrayList<String> spinnerList = new ArrayList<>(Arrays.asList(categories));

    public static final int NEW_GUESTS_REQUEST_CODE = 200;
    public static final int UPDATE_GUESTS_REQUEST_CODE = 300;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding_guests);
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.


        floatingActionButton = findViewById(R.id.addGuest);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        recyclerView = findViewById(R.id.guestView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);

        myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, MyDatabase.DB_NAME).fallbackToDestructiveMigration().build();

        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(WeddingGuests.this, GuestDetailsActivity.class), NEW_GUESTS_REQUEST_CODE);
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
                            .addOnCompleteListener(WeddingGuests.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(WeddingGuests.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(WeddingGuests.this, "Done!", Toast.LENGTH_SHORT).show();
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
        startActivityForResult(new Intent(WeddingGuests.this, GuestDetailsActivity.class).putExtra("id", id), UPDATE_GUESTS_REQUEST_CODE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (position == 0) {
            loadAllGuests();
        } else {
            String string = parent.getItemAtPosition(position).toString();
            loadFilteredGuests(string);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @SuppressLint("StaticFieldLeak")
    private void loadFilteredGuests(String category) {
        new AsyncTask<String, Void, List<Guests>>() {
            @Override
            protected List<Guests> doInBackground(String... params) {
                return myDatabase.daoAccess().fetchGuestsListByCategory(params[0]);

            }

            @Override
            protected void onPostExecute(List<Guests> guestsList) {
                recyclerViewAdapter.updateGuestsList(guestsList);
            }
        }.execute(category);

    }


    @SuppressLint("StaticFieldLeak")
    private void fetchGuestsByIdAndInsert(int id) {
        new AsyncTask<Integer, Void, Guests>() {
            @Override
            protected Guests doInBackground(Integer... params) {
                return myDatabase.daoAccess().fetchGuestsListById(params[0]);

            }

            @Override
            protected void onPostExecute(Guests guestsList) {
                recyclerViewAdapter.addRow(guestsList);
            }
        }.execute(id);

    }
    @SuppressLint("StaticFieldLeak")
    private void insertList(List<Guests> guestsList) {
        new AsyncTask<List<Guests>, Void, Void>() {
            @Override
            protected Void doInBackground(List<Guests>... params) {
                myDatabase.daoAccess().insertGuestsList(params[0]);

                return null;

            }

            @Override
            protected void onPostExecute(Void voids) {
                super.onPostExecute(voids);
            }
        }.execute(guestsList);

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
                fetchGuestsByIdAndInsert((int) id);

            } else if (requestCode == UPDATE_GUESTS_REQUEST_CODE) {

                boolean isDeleted = data.getBooleanExtra("isDeleted", false);
                int number = data.getIntExtra("number", -1);
                if (isDeleted) {
                    Toast.makeText(getApplicationContext(), number + " rows deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), number + " rows updated", Toast.LENGTH_SHORT).show();
                }

                loadAllGuests();

            }


        } else {
            Toast.makeText(getApplicationContext(), "No action done by user", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void loadAllGuests() {
        new AsyncTask<String, Void, List<Guests>>() {
            @Override
            protected List<Guests> doInBackground(String... params) {
                return myDatabase.daoAccess().fetchAllGuests();
            }

            @Override
            protected void onPostExecute(List<Guests> guestsList) {
                recyclerViewAdapter.updateGuestsList(guestsList);
            }
        }.execute();
    }

}


