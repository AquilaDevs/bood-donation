package com.example.hpraj.blood_donation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText registerEmail, registerPassword, regsiterCnfPassword;
    private Button registerBtn;
    private TextView loginText;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        regsiterCnfPassword = findViewById(R.id.register_confpassword);
        registerBtn = findViewById(R.id.register_button);
        loginText = findViewById(R.id.already_register_link);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
        mFirebaseAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(this);
        loginText.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == loginText) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (v == registerBtn) {
            String email = registerEmail.getText().toString().trim();
            String password = registerPassword.getText().toString().trim();
            String confpassword = regsiterCnfPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toasty.info(this, "Please enter username", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password) | TextUtils.isEmpty(confpassword)) {
                Toasty.info(this, "Please fill password fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!TextUtils.equals(password, confpassword)) {
                Toasty.info(this, "Password not match", Toast.LENGTH_SHORT).show();
                return;
            }

            mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toasty.success(RegisterActivity.this, "Registration Success!", Toast.LENGTH_SHORT, true).show();

                        finish();
                        Intent intent = new Intent(RegisterActivity.this, RegisterUserDetailsActivity.class);
                        startActivity(intent);
                    } else {
                        Toasty.error(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT, true).show();
                    }
                }
            });
        }
    }


}
