package com.example.hpraj.blood_donation.Fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hpraj.blood_donation.GetDropdownLists;
import com.example.hpraj.blood_donation.Holder.BloodRequestHolder;
import com.example.hpraj.blood_donation.Holder.NotificationHolder;
import com.example.hpraj.blood_donation.Holder.UserDetailsHolder;
import com.example.hpraj.blood_donation.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class RequestBloodFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "log";
    private static final int REQUEST_CODE = 2;
    private AutoCompleteTextView blood_type;
    private EditText request_count;
    private static TextView req_date, image_name;
    private Button blood_request_date_btn, req_blood, image_choose;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private UserDetailsHolder userDetailsHolder;
    private ArrayList<String> hospital_list;
    private String current_id, encoded;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.request_blood_fragment, container, false);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());

        current_id = mFirebaseAuth.getCurrentUser().getUid();

        blood_type = v.findViewById(R.id.blood_request_group);
        request_count = v.findViewById(R.id.blood_request_count);
        req_date = v.findViewById(R.id.blood_request_date);
        blood_request_date_btn = v.findViewById(R.id.blood_request_date_btn);
        req_blood = v.findViewById(R.id.btn_request_blood);
        image_choose = v.findViewById(R.id.blood_request_image_choose);
        image_name = v.findViewById(R.id.blood_request_image_name);

        req_blood.setOnClickListener(this);
        blood_request_date_btn.setOnClickListener(this);
        image_choose.setOnClickListener(this);

        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.autocomplete_text, new GetDropdownLists().getBloodGroups());
        blood_type.setAdapter(mArrayAdapter);
        blood_type.setThreshold(0);
        showPopupDropDown(blood_type);

        hospital_list = new ArrayList<>();

        mDatabaseReference.child("Users").child(current_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userDetailsHolder = dataSnapshot.getValue(UserDetailsHolder.class);
                mDatabaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        hospital_list.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String childType = postSnapshot.child("type").getValue(String.class);
                            String childCity = postSnapshot.child("city").getValue(String.class);
                            if (childCity != null || childType != null) {
                                if ((!childType.equalsIgnoreCase("user") && userDetailsHolder.getNearest_city().equalsIgnoreCase(childCity)) | childType.equalsIgnoreCase("nbts")) {
                                    hospital_list.add(postSnapshot.getKey());
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
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
        if (v == req_blood) {
            requestBlood();
        }

        if (v == blood_request_date_btn) {
            selectDate();
        }

        if (v == image_choose) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            switch (requestCode) {

                case REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = null;
                    if (resultCode == Activity.RESULT_OK) {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();

                        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        image_name.setText("Image Selected");
                        Log.d(TAG, "onActivityResult: " + encoded);
                        break;
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Log.e(TAG, "Selecting picture cancelled");
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in onActivityResult : " + e.getMessage());
        }
    }

    private void requestBlood() {
        final String blood_group = blood_type.getText().toString().trim();
        final String paints = request_count.getText().toString().trim();
        final String request_date = req_date.getText().toString().trim();

        if (TextUtils.isEmpty(blood_group) | TextUtils.isEmpty(paints) | TextUtils.isEmpty(request_date)) {
            Toasty.error(getActivity(), "Please fill out all field", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();

        final BloodRequestHolder bloodRequestHolder = new BloodRequestHolder();
        bloodRequestHolder.setBlood_group(blood_group);
        bloodRequestHolder.setNumber_Of_Paints(paints);
        bloodRequestHolder.setReq_date(request_date);
        bloodRequestHolder.setReq_user_id(mFirebaseAuth.getCurrentUser().getUid());
        bloodRequestHolder.setStatus("Pending");
        bloodRequestHolder.setReq_send_date(new SimpleDateFormat("dd MMM yyyy").format(new Date()));
        bloodRequestHolder.setReport(encoded);

        final String body = userDetailsHolder.getFullname() + " is send a blood request, which is blood group " + blood_group + " and " + paints + " pints";

        final NotificationHolder notificationHolder = new NotificationHolder();
        notificationHolder.setHeader("Blood Request");
        notificationHolder.setBody(body);
        notificationHolder.setRead_status("unread");
        notificationHolder.setSender_id(current_id);
        notificationHolder.setSend_time(new SimpleDateFormat("HH:mm").format(new Date()));

        String key = mDatabaseReference.child("Users").child(current_id).child("Blood_Requests").push().getKey();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Users/" + current_id + "/Blood_Requests/" + key, bloodRequestHolder);
//        childUpdates.put("/Users/" + current_id + "/Notifications/" + key, notificationHolder);

        for (String ids : hospital_list) {
            childUpdates.put("/Users/" + ids + "/Blood_Requests/" + key, bloodRequestHolder);
            childUpdates.put("/Users/" + ids + "/Notifications/" + key, notificationHolder);
        }

        mDatabaseReference.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toasty.success(getActivity(), "Blood request send successfully !", Toast.LENGTH_SHORT).show();
                blood_type.setText("");
                request_count.setText("");
                req_date.setText("");
                image_name.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectDate() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            Calendar cal = new GregorianCalendar(year, month, dayOfMonth);
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
            req_date.setText(dateFormat.format(cal.getTime()));
            dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
