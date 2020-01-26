package com.example.hpraj.blood_donation.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hpraj.blood_donation.Holder.NotificationHolder;
import com.example.hpraj.blood_donation.NotificationViewDialog;
import com.example.hpraj.blood_donation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context mContext;
    ArrayList<NotificationHolder> notification_list;

    public NotificationAdapter(Context mContext, ArrayList<NotificationHolder> notification_list) {
        this.mContext = mContext;
        this.notification_list = notification_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = null;

        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        mView = mLayoutInflater.inflate(R.layout.notification_cardview, viewGroup, false);

        return new NotificationAdapter.ViewHolder(mView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        if (notification_list.get(i).getRead_status().equals("read")){
            viewHolder.notification_card.setBackgroundColor(R.color.colorLightBlueLight);
        }

        viewHolder.header.setText(notification_list.get(i).getHeader());
        viewHolder.body.setText(notification_list.get(i).getBody());
        viewHolder.time.setText(notification_list.get(i).getSend_time());

        viewHolder.notification_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationViewDialog notificationViewDialog = new NotificationViewDialog(mContext, notification_list.get(i).getHeader(),notification_list.get(i).getBody(),notification_list.get(i).getNot_id(), null);
                notificationViewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                notificationViewDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notification_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView header, body, time;
        CardView notification_card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.notification_header);
            body = itemView.findViewById(R.id.notification_body);
            time = itemView.findViewById(R.id.notification_time);

            notification_card = itemView.findViewById(R.id.notification_cardview);
        }
    }
}
