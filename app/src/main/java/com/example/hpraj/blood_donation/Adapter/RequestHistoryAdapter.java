package com.example.hpraj.blood_donation.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hpraj.blood_donation.Holder.BloodRequestHolder;
import com.example.hpraj.blood_donation.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class RequestHistoryAdapter extends RecyclerView.Adapter<RequestHistoryAdapter.HistoryViewHolder> {

    Context mContext;
    ArrayList<BloodRequestHolder> historyList;
    private DatabaseReference mDatabaseReference;

    public RequestHistoryAdapter(Context mContext, ArrayList<BloodRequestHolder> historyList) {
        this.mContext = mContext;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = null;

        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        mView = mLayoutInflater.inflate(R.layout.request_history_cardview, viewGroup, false);

        return new HistoryViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryViewHolder historyViewHolder, final int i) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");

        if (!historyList.get(i).getStatus().equals("Pending")){
            historyViewHolder.cancel_req.setVisibility(View.GONE);
        }

        historyViewHolder.req_status.setText(historyList.get(i).getStatus());
        historyViewHolder.req_send_date.setText("Req Send: " + historyList.get(i).getReq_send_date());
        historyViewHolder.req_blood_group.setText("Blood Group: " + historyList.get(i).getBlood_group());
        historyViewHolder.req_paint_count.setText("Paints: " + historyList.get(i).getNumber_Of_Paints());
        historyViewHolder.req_date.setText("Req Date: " + historyList.get(i).getReq_date());

        historyViewHolder.cancel_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Blood_Requests").child(historyList.get(i).getId()).child("status").setValue("Canceled").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toasty.success(mContext, "Request canceled!", Toast.LENGTH_SHORT).show();
                            historyViewHolder.cancel_req.setVisibility(View.GONE);
                            historyViewHolder.req_status.setText("Canceled");
                        } else {
                            Toasty.success(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView req_date, req_blood_group, req_paint_count, req_send_date, req_status, cancel_req;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            req_date = (TextView) itemView.findViewById(R.id.req_date);
            req_blood_group = (TextView) itemView.findViewById(R.id.req_blood_group);
            req_paint_count = (TextView) itemView.findViewById(R.id.req_paint_count);
            req_send_date = (TextView) itemView.findViewById(R.id.req_send_date);
            req_status = (TextView) itemView.findViewById(R.id.req_status);
            cancel_req = itemView.findViewById(R.id.cancel_req_btn);
        }
    }
}
