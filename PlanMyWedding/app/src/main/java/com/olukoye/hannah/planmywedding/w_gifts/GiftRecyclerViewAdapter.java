package com.olukoye.hannah.planmywedding.w_gifts;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olukoye.hannah.planmywedding.R;

import java.util.ArrayList;
import java.util.List;

public class GiftRecyclerViewAdapter extends RecyclerView.Adapter<GiftRecyclerViewAdapter.ViewHolder> {

    private List<Gifts> giftsList;
    private GiftRecyclerViewAdapter.ClickListener clickListener;

    public GiftRecyclerViewAdapter(ClickListener clickListener) {
        this.clickListener = clickListener;
        giftsList = new ArrayList<>();
    }

    @Override
    public GiftRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_recyclerview_item_layout, parent, false);
        GiftRecyclerViewAdapter.ViewHolder viewHolder = new GiftRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GiftRecyclerViewAdapter.ViewHolder holder, int position) {
        Gifts gifts = giftsList.get(position);
        holder.txtName.setText(gifts.name);
        holder.txtNo.setText("#" + String.valueOf(gifts.gifts_id));
        holder.txtCategory.setText(gifts.category);

    }

    @Override
    public int getItemCount() {
        return giftsList.size();
    }


    public void updateGiftsList(List<Gifts> data) {
        giftsList.clear();
        giftsList.addAll(data);
        notifyDataSetChanged();
    }

    public void addRow(Gifts data) {
        giftsList.add(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public TextView txtNo;
        public TextView txtCategory;
        public CardView cardView;

        public ViewHolder(View view) {
            super(view);

            txtNo = view.findViewById(R.id.txtNo);
            txtName = view.findViewById(R.id.txtName);
            txtCategory = view.findViewById(R.id.txtCategory);
            cardView = view.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.launchIntent(giftsList.get(getAdapterPosition()).gifts_id);
                }
            });
        }
    }

    public interface ClickListener {
        void launchIntent(int id);
    }
}
