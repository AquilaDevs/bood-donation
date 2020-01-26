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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hpraj.blood_donation.Adapter.DonateHistoryAdpter;
import com.example.hpraj.blood_donation.Holder.DonationHistoryHolder;
import com.example.hpraj.blood_donation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import es.dmoral.toasty.Toasty;

public class UserProfileFragment extends Fragment {

    private RecyclerView mListView;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private TextView user, usertype, userstatus, note_txt, donation_history_count;
    private ArrayList<DonationHistoryHolder> historyList;
    private String user_id;
    private DonateHistoryAdpter donateHistoryAdpter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.user_profile_fragment, container, false);

        mListView = view.findViewById(R.id.user_profile_listview);
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListView.setNestedScrollingEnabled(false);

        user = view.findViewById(R.id.user_profile_user_name);
        usertype = view.findViewById(R.id.user_profile_user_type);
        userstatus = view.findViewById(R.id.user_profile_user_status);
        note_txt = view.findViewById(R.id.note_txt);
        donation_history_count =view.findViewById(R.id.donation_history_count);

        historyList=new ArrayList<>();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");

        user_id = mFirebaseAuth.getCurrentUser().getUid();

        donateHistoryAdpter=new DonateHistoryAdpter(getActivity(), historyList);
        mListView.setAdapter(donateHistoryAdpter);

        mDatabaseReference.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String user_name = dataSnapshot.child("fullname").getValue(String.class);
                String user_type = dataSnapshot.child("blood_type").getValue(String.class);
                String status = dataSnapshot.child("status").getValue(String.class);

                user.setText(user_name);
                usertype.setText("Blood Group: "+user_type);
                userstatus.setText("Account Status: "+status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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

        return view;
    }
}
