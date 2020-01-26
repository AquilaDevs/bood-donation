package com.example.hpraj.blood_donation;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GetDropdownLists {
    private static DatabaseReference databaseReference1, databaseReference2;
    private static ArrayList<String> group_list, district_list;

    public GetDropdownLists() {
        group_list =new ArrayList<>();
        district_list= new ArrayList<>();
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Blood_Groups");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Districts");

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    group_list.add(snapshot.child("name").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    district_list.add(snapshot.child("name").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static ArrayList<String> getBloodGroups() {
        return group_list;
    }

    public static ArrayList<String> getDistrict_list() {return district_list; }
}
