package com.olukoye.hannah.planmywedding;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<MainRecyclerViewItem> catItemList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view_item);
        checkIfAppLaunchedFirstTime();
        initializeCatItemList();

        RecyclerView catRecyclerView = (RecyclerView) findViewById(R.id.card_view_recycler_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        catRecyclerView.setLayoutManager(gridLayoutManager);

        MainRecyclerViewDataAdapter catDataAdapter = new MainRecyclerViewDataAdapter(catItemList);
        catRecyclerView.setAdapter(catDataAdapter);

    }

    private void initializeCatItemList() {
        if (catItemList == null) {
            catItemList = new ArrayList<MainRecyclerViewItem>();
            catItemList.add(new MainRecyclerViewItem("Wedding Venue", R.drawable.venue));
            catItemList.add(new MainRecyclerViewItem("Guest Registration", R.drawable.guest));
            catItemList.add(new MainRecyclerViewItem("Gift Registry", R.drawable.gift));
            catItemList.add(new MainRecyclerViewItem("Wedding Theme", R.drawable.theme));

        }
    }
    private void checkIfAppLaunchedFirstTime() {
        final String PREFS_NAME = "SharedPrefs";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("firstTime", true)) {
            settings.edit().putBoolean("firstTime", false).apply();
        }
    }
}

