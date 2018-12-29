package com.olukoye.hannah.planmywedding.w_gifts;

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

public class GiftDetailsActivity extends AppCompatActivity {

    Spinner spinner;
    EditText inTitle;
    Button btnDone, btnDelete;
    boolean isNewGifts = false;

    private String[] categories = {
            "Kitchen",
            "Bathroom",
            "Furniture",
            "Electronics",
            "Other"
    };

    public ArrayList<String> spinnerList = new ArrayList<>(Arrays.asList(categories));
    GiftDatabase giftDatabase;

    Gifts updateGifts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner = findViewById(R.id.spinner);
        inTitle = findViewById(R.id.inTitle);
        btnDone = findViewById(R.id.btnDone);
        btnDelete = findViewById(R.id.btnDelete);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        giftDatabase = Room.databaseBuilder(getApplicationContext(), GiftDatabase.class, GiftDatabase.DB_NAME).build();

        int gifts_id = getIntent().getIntExtra("id", -100);

        if (gifts_id == -100)
            isNewGifts = true;

        if (!isNewGifts) {
            fetchGiftsById(gifts_id);
            btnDelete.setVisibility(View.VISIBLE);
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewGifts) {
                    Gifts gifts = new Gifts();
                    gifts.name = inTitle.getText().toString();
                    gifts.category = spinner.getSelectedItem().toString();

                    insertRow(gifts);
                } else {

                    updateGifts.name = inTitle.getText().toString();
                    updateGifts.category = spinner.getSelectedItem().toString();

                    updateRow(updateGifts);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRow(updateGifts);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchGiftsById(final int gifts_id) {
        new AsyncTask<Integer, Void, Gifts>() {
            @Override
            protected Gifts doInBackground(Integer... params) {

                return giftDatabase.daoAccess().fetchGiftsListById(params[0]);

            }

            @Override
            protected void onPostExecute(Gifts gifts) {
                super.onPostExecute(gifts);
                inTitle.setText(gifts.name);
                spinner.setSelection(spinnerList.indexOf(gifts.category));

                updateGifts = gifts;
            }
        }.execute(gifts_id);

    }

    @SuppressLint("StaticFieldLeak")
    private void insertRow(Gifts gifts) {
        new AsyncTask<Gifts, Void, Long>() {
            @Override
            protected Long doInBackground(Gifts... params) {
                return giftDatabase.daoAccess().insertGifts(params[0]);
            }

            @Override
            protected void onPostExecute(Long id) {
                super.onPostExecute(id);

                Intent intent = getIntent();
                intent.putExtra("isNew", true).putExtra("id", id);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(gifts);

    }

    @SuppressLint("StaticFieldLeak")
    private void deleteRow(Gifts gifts) {
        new AsyncTask<Gifts, Void, Integer>() {
            @Override
            protected Integer doInBackground(Gifts... params) {
                return giftDatabase.daoAccess().deleteGifts(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra("isDeleted", true).putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(gifts);

    }


    @SuppressLint("StaticFieldLeak")
    private void updateRow(Gifts gifts) {
        new AsyncTask<Gifts, Void, Integer>() {
            @Override
            protected Integer doInBackground(Gifts... params) {
                return giftDatabase.daoAccess().updateGifts(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra("isNew", false).putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(gifts);

    }

}
