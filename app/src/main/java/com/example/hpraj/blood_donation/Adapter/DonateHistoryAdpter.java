package com.example.hpraj.blood_donation.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hpraj.blood_donation.Holder.DonationHistoryHolder;
import com.example.hpraj.blood_donation.R;

import java.util.ArrayList;

public class DonateHistoryAdpter extends RecyclerView.Adapter<DonateHistoryAdpter.HistoryViewHolder> {

    Context mContext;
    ArrayList<DonationHistoryHolder> historyList;

    public DonateHistoryAdpter(Context mContext, ArrayList<DonationHistoryHolder> historyList) {
        this.mContext = mContext;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = null;

        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        mView = mLayoutInflater.inflate(R.layout.donate_history_cardview, viewGroup, false);

        return new HistoryViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder historyViewHolder, int i) {
        historyViewHolder.date.setText(historyList.get(i).getDate());
        historyViewHolder.location.setText(historyList.get(i).getLocation());
        historyViewHolder.numberOfPaits.setText("Paints "+historyList.get(i).getNumber_of_paints());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }


    static class HistoryViewHolder extends RecyclerView.ViewHolder{
        TextView date, location, numberOfPaits;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.donate_date);
            location = (TextView) itemView.findViewById(R.id.donate_location);
            numberOfPaits = (TextView) itemView.findViewById(R.id.donate_paints);
        }
    }


}
