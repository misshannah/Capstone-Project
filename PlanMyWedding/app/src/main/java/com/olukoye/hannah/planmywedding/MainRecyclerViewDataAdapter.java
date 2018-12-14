package com.olukoye.hannah.planmywedding;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class MainRecyclerViewDataAdapter extends RecyclerView.Adapter<MainRecyclerViewItemHolder> {

    private List<MainRecyclerViewItem> catItemList;

    public MainRecyclerViewDataAdapter(List<MainRecyclerViewItem> catItemList) {
        this.catItemList = catItemList;
    }

    @Override
    public MainRecyclerViewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View carItemView = layoutInflater.inflate(R.layout.activity_main, parent, false);

        final TextView carTitleView = (TextView)carItemView.findViewById(R.id.card_view_image_title);
        final ImageView carImageView = (ImageView)carItemView.findViewById(R.id.card_view_image);
        carImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        MainRecyclerViewItemHolder ret = new MainRecyclerViewItemHolder(carItemView);
        return ret;
    }

    @Override
    public void onBindViewHolder(MainRecyclerViewItemHolder holder, int position) {
        if(catItemList!=null) {
            MainRecyclerViewItem carItem = catItemList.get(position);

            if(carItem != null) {
                holder.getCarTitleText().setText(carItem.getCatName());
                holder.getCarImageView().setImageResource(carItem.getCatImageId());
            }
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if(catItemList!=null)
        {
            ret = catItemList.size();
        }
        return ret;
    }
}

