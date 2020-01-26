package com.example.hpraj.blood_donation;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText loginEmail, loginPassoword;
    private Button loginBtn;
    private TextView register_link;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");

        loginBtn = findViewById(R.id.login_button);
        loginEmail = findViewById(R.id.login_email);
        loginPassoword = findViewById(R.id.login_password);
        register_link = findViewById(R.id.register_link);

        loginBtn.setOnClickListener(this);
        register_link.setOnClickListener(this);

        ConnectivityManager connectivityManager =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null && !networkInfo.isConnected()) {
            Toasty.error(this, "Network connection unavailable.", Toast.LENGTH_SHORT,true).show();
        }

    }

    @Override
    public void onClick(View v) {
        if (v == loginBtn) {
            loginBtn.setEnabled(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loginBtn.setEnabled(true);
                }
            }, 2000);

            String email = loginEmail.getText().toString().trim();
            String password = loginPassoword.getText().toString().trim();

            if (TextUtils.isEmpty(email) | TextUtils.isEmpty(password)) {
                Toasty.info(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        String token_id = FirebaseInstanceId.getInstance().getToken();

                        String current_id = mFirebaseAuth.getCurrentUser().getUid();

                        mDatabaseReference.child(current_id).child("user_token").setValue(token_id).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toasty.success(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                            }
                        });


                    } else {
                        Toasty.error(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (v == register_link) {
            finish();
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }
    }
}
