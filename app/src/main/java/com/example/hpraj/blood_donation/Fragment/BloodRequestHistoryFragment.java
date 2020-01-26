package com.example.hpraj.blood_donation.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hpraj.blood_donation.Adapter.RequestHistoryAdapter;
import com.example.hpraj.blood_donation.Holder.BloodRequestHolder;
import com.example.hpraj.blood_donation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collections;

public class BloodRequestHistoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private ArrayList<BloodRequestHolder> historyList;
    private TextView note_txt;
    private RequestHistoryAdapter requestHistoryAdapter;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.blood_request_history_fragment, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");

        mRecyclerView = this.view.findViewById(R.id.request_history_recyclerview);
        note_txt = this.view.findViewById(R.id.note_txt);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        historyList = new ArrayList<>();

        requestHistoryAdapter = new RequestHistoryAdapter(getActivity(), historyList);
        mRecyclerView.setAdapter(requestHistoryAdapter);

        mDatabaseReference.child(mFirebaseAuth.getCurrentUser().getUid()).child("Blood_Requests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                historyList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    BloodRequestHolder bloodRequestHolder = postSnapshot.getValue(BloodRequestHolder.class);
                    historyList.add(new BloodRequestHolder(postSnapshot.getKey(), bloodRequestHolder.getBlood_group(), bloodRequestHolder.getNumber_Of_Paints(), bloodRequestHolder.getReq_date(), bloodRequestHolder.getStatus(), bloodRequestHolder.getReq_user_id(), bloodRequestHolder.getReq_send_date(), bloodRequestHolder.getReport()));
                }

                if (historyList.size() > 0) {
                    note_txt.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    Collections.reverse(historyList);
                    requestHistoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
