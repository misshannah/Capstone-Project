package com.olukoye.hannah.planmywedding.w_guests;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.olukoye.hannah.planmywedding.R;

import java.util.ArrayList;
import java.util.Arrays;

public class GuestDetailsActivity extends AppCompatActivity {

    Spinner spinner;
    EditText inTitle;
    Button btnDone, btnDelete;
    boolean isNewGuests = false;

    private String[] categories = {
            "Family",
            "Friends",
            "Workmates",
            "Others"
    };

    public ArrayList<String> spinnerList = new ArrayList<>(Arrays.asList(categories));
    MyDatabase myDatabase;

    Guests updateGuests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner = findViewById(R.id.spinner);
        inTitle = findViewById(R.id.inTitle);
        btnDone = findViewById(R.id.btnDone);
        btnDelete = findViewById(R.id.btnDelete);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, MyDatabase.DB_NAME).build();

        int guests_id = getIntent().getIntExtra("id", -100);

        if (guests_id == -100)
            isNewGuests = true;

        if (!isNewGuests) {
            fetchGuestsById(guests_id);
            btnDelete.setVisibility(View.VISIBLE);
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewGuests) {
                    Guests guests = new Guests();
                    guests.name = inTitle.getText().toString();
                    guests.category = spinner.getSelectedItem().toString();

                    insertRow(guests);
                } else {

                    updateGuests.name = inTitle.getText().toString();
                    updateGuests.category = spinner.getSelectedItem().toString();

                    updateRow(updateGuests);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRow(updateGuests);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchGuestsById(final int guests_id) {
        new AsyncTask<Integer, Void, Guests>() {
            @Override
            protected Guests doInBackground(Integer... params) {

                return myDatabase.daoAccess().fetchGuestsListById(params[0]);

            }

            @Override
            protected void onPostExecute(Guests guests) {
                super.onPostExecute(guests);
                inTitle.setText(guests.name);
                spinner.setSelection(spinnerList.indexOf(guests.category));

                updateGuests = guests;
            }
        }.execute(guests_id);

    }

    @SuppressLint("StaticFieldLeak")
    private void insertRow(Guests guests) {
        new AsyncTask<Guests, Void, Long>() {
            @Override
            protected Long doInBackground(Guests... params) {
                return myDatabase.daoAccess().insertGuests(params[0]);
            }

            @Override
            protected void onPostExecute(Long id) {
                super.onPostExecute(id);

                Intent intent = getIntent();
                intent.putExtra("isNew", true).putExtra("id", id);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(guests);

    }

    @SuppressLint("StaticFieldLeak")
    private void deleteRow(Guests guests) {
        new AsyncTask<Guests, Void, Integer>() {
            @Override
            protected Integer doInBackground(Guests... params) {
                return myDatabase.daoAccess().deleteGuests(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra("isDeleted", true).putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(guests);

    }


    @SuppressLint("StaticFieldLeak")
    private void updateRow(Guests guests) {
        new AsyncTask<Guests, Void, Integer>() {
            @Override
            protected Integer doInBackground(Guests... params) {
                return myDatabase.daoAccess().updateGuests(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra("isNew", false).putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(guests);

    }

}
