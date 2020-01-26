package com.example.hpraj.blood_donation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hpraj.blood_donation.Maps.MapsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationViewDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private ImageView close_image;
    private TextView header, body;
    private String not_header, not_body, id, location;
    private Button show_nearest;

    public NotificationViewDialog(Context context, String not_header, String not_body, String id, String location) {
        super(context);
        this.mContext= context;
        this.not_header = not_header;
        this.not_body = not_body;
        this.id = id;
        this.location = location;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.notification_popup);

        header = findViewById(R.id.notification_view_header);
        body = findViewById(R.id.notification_view_body);
        close_image = findViewById(R.id.notification_view_close);
        show_nearest = findViewById(R.id.show_nearest);

        header.setText(not_header);
        body.setText(not_body);

        close_image.setOnClickListener(this);
        show_nearest.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == close_image) {
            FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
            DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
            mDatabaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("Notifications").child(id).child("read_status").setValue("read");
            dismiss();
        } else if (v == show_nearest) {

            Intent intent = new Intent(mContext, MapsActivity.class);
            intent.putExtra("location",location);
            mContext.startActivity(intent);

        }
    }
}
