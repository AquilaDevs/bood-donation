package com.example.hpraj.blood_donation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hpraj.blood_donation.Holder.UserDetailsHolder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import es.dmoral.toasty.Toasty;

public class RegisterUserDetailsActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private EditText register_fullname, register_nic, register_mobile, register_address;
    private TextView bd_text;
    private Button select_bd, save;
    private AutoCompleteTextView city, nearest_city, blood_group;
    private String fullname, nic, address, mobile, bd, user_city, nearestCity, blood_type;
    private DatabaseReference mDatabaseReference;
    private DatePickerFragment mDatePickerFragment;
    private FirebaseAuth mFirebaseAuth;
    private RadioButton all_island, home_town;
    private RadioGroup radio_group;
    private DatabaseReference databaseReference2;
    private ArrayList<String> district_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_details);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Districts");
        district_list= new ArrayList<>();
        mFirebaseAuth = FirebaseAuth.getInstance();

        register_fullname = findViewById(R.id.register_fullname);
        register_nic = findViewById(R.id.register_nic);
        register_mobile = findViewById(R.id.register_mobile);
        register_address = findViewById(R.id.register_address);

        select_bd = findViewById(R.id.btn_select_bd);
        save = findViewById(R.id.btn_save_detatils);
        bd_text = findViewById(R.id.register_bd_txt);

        blood_group = (AutoCompleteTextView) findViewById(R.id.register_blood_group);
        city = (AutoCompleteTextView) findViewById(R.id.register_city);
        nearest_city = (AutoCompleteTextView) findViewById(R.id.register_nearest_location);

        all_island = findViewById(R.id.all_island);
        home_town = findViewById(R.id.home_town);
        radio_group = findViewById(R.id.radio_group);


        save.setOnClickListener(this);
        select_bd.setOnClickListener(this);

        ArrayAdapter<String> mArrayAdapter1 = new ArrayAdapter<>(getApplicationContext(), R.layout.autocomplete_text, new GetDropdownLists().getBloodGroups());
        blood_group.setAdapter(mArrayAdapter1);
        blood_group.setThreshold(0);
        showPopupDropDown(blood_group);

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    district_list.add(snapshot.child("name").getValue(String.class));
                }
                ArrayAdapter<String> mArrayAdapter1 = new ArrayAdapter<>(getApplicationContext(), R.layout.autocomplete_text, district_list);
                city.setAdapter(mArrayAdapter1);
                city.setThreshold(0);
                showPopupDropDown(city);
                nearest_city.setAdapter(mArrayAdapter1);
                nearest_city.setThreshold(0);
                showPopupDropDown(nearest_city);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showPopupDropDown(final AutoCompleteTextView txt) {
        txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txt.showDropDown();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == save) {
            saveDetatils();
        }

        if (v == select_bd) {
            selectDate();
        }
    }

    private void selectDate() {
        mDatePickerFragment = new DatePickerFragment();
        mDatePickerFragment.show(getFragmentManager(), "Date");
    }

    private void saveDetatils() {

        fullname = register_fullname.getText().toString().trim();
        nic = register_nic.getText().toString().trim();
        address = register_address.getText().toString().trim();
        mobile = register_mobile.getText().toString().trim();
        bd = bd_text.getText().toString().trim();
        user_city = city.getText().toString().trim();
        nearestCity = nearest_city.getText().toString().trim();
        blood_type = blood_group.getText().toString().trim();

        if (TextUtils.isEmpty(fullname) | TextUtils.isEmpty(nic) | TextUtils.isEmpty(address) | TextUtils.isEmpty(bd) | TextUtils.isEmpty(mobile) | TextUtils.isEmpty(user_city) | TextUtils.isEmpty(nearestCity)) {
            Toasty.info(this, "Please fill all fields", Toast.LENGTH_SHORT,true).show();
            return;
        }

        String selected = null;
        if (all_island.isSelected()) {
            selected = "all_island";
        }else {
            selected = "home_town";
        }

        UserDetailsHolder userDetailsHolder = new UserDetailsHolder();
        userDetailsHolder.setFullname(fullname);
        userDetailsHolder.setNic(nic);
        userDetailsHolder.setAddress(address);
        userDetailsHolder.setMobile(mobile);
        userDetailsHolder.setDob(bd);
        userDetailsHolder.setCity(user_city);
        userDetailsHolder.setNearest_city(nearestCity);
        userDetailsHolder.setBlood_type(blood_type);
        userDetailsHolder.setStatus("Deactive");
        userDetailsHolder.setType("user");
        userDetailsHolder.setNotifiction_allowed(selected);

        String user_id = mFirebaseAuth.getCurrentUser().getUid();

        mDatabaseReference.child(user_id).setValue(userDetailsHolder).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toasty.info(RegisterUserDetailsActivity.this, "Registration Success!", Toast.LENGTH_SHORT,true).show();

                finish();
                startActivity(new Intent(RegisterUserDetailsActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = new GregorianCalendar(year, month, dayOfMonth);
        setDate(cal);
    }

    private void setDate(Calendar calendar) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        bd_text.setText(dateFormat.format(calendar.getTime()));
        mDatePickerFragment.dismiss();
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(cal.YEAR);
            int month = cal.get(cal.MONTH);
            int date = cal.get(cal.DATE);

            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, date);
        }
    }
}

