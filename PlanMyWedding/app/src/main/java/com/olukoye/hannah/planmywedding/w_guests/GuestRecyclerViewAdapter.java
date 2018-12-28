package com.olukoye.hannah.planmywedding.w_guests;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olukoye.hannah.planmywedding.R;

import java.util.ArrayList;
import java.util.List;

public class GuestRecyclerViewAdapter extends RecyclerView.Adapter<GuestRecyclerViewAdapter.ViewHolder> {

    private List<Guests> guestsList;
    private GuestRecyclerViewAdapter.ClickListener clickListener;

    public GuestRecyclerViewAdapter(ClickListener clickListener) {
        this.clickListener = clickListener;
        guestsList = new ArrayList<>();
    }

    @Override
    public GuestRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guest_recyclerview_item_layout, parent, false);
        GuestRecyclerViewAdapter.ViewHolder viewHolder = new GuestRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GuestRecyclerViewAdapter.ViewHolder holder, int position) {
        Guests guests = guestsList.get(position);
        holder.txtName.setText(guests.name);
        holder.txtNo.setText("#" + String.valueOf(guests.guests_id));
        holder.txtCategory.setText(guests.category);

    }

    @Override
    public int getItemCount() {
        return guestsList.size();
    }


    public void updateGuestsList(List<Guests> data) {
        guestsList.clear();
        guestsList.addAll(data);
        notifyDataSetChanged();
    }

    public void addRow(Guests data) {
        guestsList.add(data);
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
                    clickListener.launchIntent(guestsList.get(getAdapterPosition()).guests_id);
                }
            });
        }
    }

    public interface ClickListener {
        void launchIntent(int id);
    }
}
