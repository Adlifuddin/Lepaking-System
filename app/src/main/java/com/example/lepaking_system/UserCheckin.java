package com.example.lepaking_system;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class UserCheckin extends Fragment {

    ImageView qrImage;

    DatabaseReference custDb; //initialize database reference

    FirebaseUser cust = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_usercheckin, container, false);

        qrImage = v.findViewById(R.id.qrOutput);

        //get id
        String id = cust.getUid();

        //initialize database path
        custDb = FirebaseDatabase.getInstance().getReference("Customer").child(id);
        custDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String email = cust.getEmail();
                String name = dataSnapshot.child("name").getValue().toString();
                String mobileNo = dataSnapshot.child("mobileNo").getValue().toString();
                String streetName = dataSnapshot.child("streetName").getValue().toString();
                String poscode = dataSnapshot.child("poscode").getValue().toString();
                String city = dataSnapshot.child("city").getValue().toString();
                String state = dataSnapshot.child("state").getValue().toString();

                String custData = id + "," + email + "," + name + "," + mobileNo + "," + streetName + "," + poscode + ","
                        + city + "," + state;
                QRGEncoder qrgEncoder = new QRGEncoder(custData, null, QRGContents.Type.TEXT, 500);
                // Getting QR-Code as Bitmap
                Bitmap bitmap = qrgEncoder.getBitmap();
                // Setting Bitmap to ImageView
                qrImage.setImageBitmap(bitmap);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }
}
