package com.olukoye.hannah.planmywedding;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.olukoye.hannah.planmywedding.w_gifts.WeddingGifts;
import com.olukoye.hannah.planmywedding.w_guests.WeddingGuests;
import com.olukoye.hannah.planmywedding.w_theme.WeddingTheme;
import com.olukoye.hannah.planmywedding.w_venue.WeddingVenue;

import java.util.List;


public class MainRecyclerViewDataAdapter extends RecyclerView.Adapter<MainRecyclerViewItemHolder> {

    private List<MainRecyclerViewItem> catItemList;

    public MainRecyclerViewDataAdapter(List<MainRecyclerViewItem> catItemList) {
        this.catItemList = catItemList;
    }

    @Override
    public MainRecyclerViewItemHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View catItemView = layoutInflater.inflate(R.layout.activity_main, parent, false);

        final TextView catTitleView = (TextView)catItemView.findViewById(R.id.card_view_image_title);
        final ImageView catImageView = (ImageView)catItemView.findViewById(R.id.card_view_image);
        catImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = catTitleView.getText().toString();
                if (title.contains("Venue")) {
                    Intent venue = new Intent(v.getContext(), WeddingVenue.class);
                    v.getContext().startActivity(venue);
                } else if (title.contains("Gift")) {
                    Intent gift = new Intent(v.getContext(), WeddingGifts.class);
                    v.getContext().startActivity(gift);
                } else if (title.contains("Guest")) {
                    Intent guest = new Intent(v.getContext(), WeddingGuests.class);
                    v.getContext().startActivity(guest);
                } else if (title.contains("Theme")) {
                    Intent theme = new Intent(v.getContext(), WeddingTheme.class);
                    v.getContext().startActivity(theme);
                }
            }
        });

        MainRecyclerViewItemHolder ret = new MainRecyclerViewItemHolder(catItemView);
        return ret;
    }

    @Override
    public void onBindViewHolder(MainRecyclerViewItemHolder holder, int position) {
        if(catItemList!=null) {
            MainRecyclerViewItem catItem = catItemList.get(position);

            if(catItem != null) {
                holder.getcatTitleText().setText(catItem.getCatName());
                holder.getcatImageView().setImageResource(catItem.getCatImageId());
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

