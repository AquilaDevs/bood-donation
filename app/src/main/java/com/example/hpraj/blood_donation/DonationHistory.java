package com.example.hpraj.blood_donation;


import android.app.MediaRouteButton;
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

import com.example.hpraj.blood_donation.Adapter.DonateHistoryAdpter;
import com.example.hpraj.blood_donation.Holder.DonationHistoryHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class DonationHistory extends Fragment {

    private View v;
    private DonateHistoryAdpter donateHistoryAdpter;
    private RecyclerView mListView;
    private ArrayList<DonationHistoryHolder> historyList;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private TextView note_txt,donation_history_count;


    public DonationHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donation_history, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.v = view;
        mListView = this.v.findViewById(R.id.user_profile_listview);
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListView.setNestedScrollingEnabled(false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");

        historyList=new ArrayList<>();
        note_txt = this.v.findViewById(R.id.note_txt);
//        donation_history_count =this.v.findViewById(R.id.donation_history_count);

        donateHistoryAdpter=new DonateHistoryAdpter(getActivity(), historyList);
        mListView.setAdapter(donateHistoryAdpter);

        String user_id = mFirebaseAuth.getCurrentUser().getUid();

        mDatabaseReference.child(user_id).child("Donation_history").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                historyList.clear();

                for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                    historyList.add(new DonationHistoryHolder(dataSnapshot2.child("date").getValue().toString(),dataSnapshot2.child("location").getValue().toString(),dataSnapshot2.child("number_of_pints").getValue().toString()));
                }

                if (historyList.size() > 0) {
                    note_txt.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    donation_history_count.setVisibility(View.VISIBLE);
                    donation_history_count.setText(dataSnapshot.getChildrenCount()+"");

                    Collections.reverse(historyList);
                    donateHistoryAdpter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
