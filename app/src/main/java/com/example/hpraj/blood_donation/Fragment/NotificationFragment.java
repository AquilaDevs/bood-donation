package com.example.hpraj.blood_donation.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hpraj.blood_donation.Adapter.NotificationAdapter;
import com.example.hpraj.blood_donation.Holder.NotificationHolder;
import com.example.hpraj.blood_donation.Maps.MapsActivity;
import com.example.hpraj.blood_donation.NotificationViewDialog;
import com.example.hpraj.blood_donation.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class NotificationFragment extends Fragment {

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private RecyclerView mRecyclerView;
    private ArrayList<NotificationHolder> notification_list;
    private Context mContext;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private View view;
    private String current_user_id, current_lat,current_long;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.notification_fragment, container, false);

        mContext = getContext();
        mRecyclerView = view.findViewById(R.id.notification_recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        notification_list = new ArrayList<>();

        mFirebaseAuth = FirebaseAuth.getInstance();

        current_user_id = mFirebaseAuth.getCurrentUser().getUid();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(current_user_id).child("Notifications");

        requestPermission();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        getLocatoin();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions firebaseRecyclerOptions = new FirebaseRecyclerOptions
                .Builder<NotificationHolder>()
                .setQuery(mDatabaseReference, NotificationHolder.class)
                .build();

        FirebaseRecyclerAdapter<NotificationHolder, ViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<NotificationHolder, ViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull final NotificationHolder model) {
                if (model.getRead_status().equals("read")) {
                    holder.notification_card.setBackgroundColor(getResources().getColor(R.color.colorLightBlueLight));
                }

                holder.header.setText(model.getHeader());
                holder.body.setText(model.getBody());
                holder.time.setText(model.getSend_time());

                holder.notification_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String location = current_lat+","+current_long;
                        NotificationViewDialog notificationViewDialog = new NotificationViewDialog(getContext(), model.getHeader(), model.getBody(), getRef(position).getKey(),location);
                        notificationViewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        notificationViewDialog.show();
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_cardview, viewGroup, false);
                ViewHolder viewHolder = new ViewHolder(mView);

                return viewHolder;
            }
        };

        mRecyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.startListening();
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

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    private void getLocatoin() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    current_lat = String.valueOf(location.getLatitude());
                    current_long = String.valueOf(location.getLongitude());
                }
            }
        });
    }
}


// this.view = view;
//
//    mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
//    mFirebaseAuth = FirebaseAuth.getInstance();
//    mContext = getContext();
//
//    mRecyclerView = view.findViewById(R.id.notification_recyclerview);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        mRecyclerView.setNestedScrollingEnabled(false);
//
//    notification_list = new ArrayList<>();
//    notificationAdapter = new NotificationAdapter(mContext, notification_list);
//        mRecyclerView.setAdapter(notificationAdapter);
//
//        mDatabaseReference.child(mFirebaseAuth.getCurrentUser().getUid()).child("Notifications").addListenerForSingleValueEvent(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//            notification_list.clear();
//            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                NotificationHolder notificationHolder = postSnapshot.getValue(NotificationHolder.class);
//
//                notification_list.add(new NotificationHolder(postSnapshot.getKey(), notificationHolder.getHeader(), notificationHolder.getBody(), notificationHolder.getSend_time(), notificationHolder.getRead_status(), mFirebaseAuth.getCurrentUser().getUid()));
//            }
//            Collections.reverse(notification_list);
//            notificationAdapter.notifyDataSetChanged();
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//        }
//    });
//}