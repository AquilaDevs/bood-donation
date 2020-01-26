package com.example.hpraj.blood_donation;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

public class FirebaseInstantIdService extends  FirebaseMessagingService{
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d("Firebase", "token "+ FirebaseInstanceId.getInstance().getToken());

    }

}
