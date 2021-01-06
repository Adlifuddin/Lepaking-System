package com.example.lepaking_system;

import android.graphics.Bitmap;
import android.os.Bundle;
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

import java.math.BigDecimal;
import java.math.RoundingMode;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class UserPayment extends Fragment {

    ImageView qrImage;

    DatabaseReference custDb; //initialize database reference

    FirebaseUser cust = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_userpayment, container, false);

        qrImage = v.findViewById(R.id.qrPayment);

        //get id
        String id = cust.getUid();

        //initialize database path
        custDb = FirebaseDatabase.getInstance().getReference("CurrentOrder").child(id);
        custDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String orders = "";
                int count = (int) dataSnapshot.getChildrenCount();
                String tgk[] = new String[count];
                int x = 0;
                //to loop and store in array
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    tgk[x] = childSnapshot.getKey();
                    x++;
                }

                for(int i = 0; i<count; i++){

                    orders = orders + tgk[i] + " " + dataSnapshot.child(tgk[i]).getValue() + ";";
                }

                QRGEncoder qrgEncoder = new QRGEncoder(orders, null, QRGContents.Type.TEXT, 500);
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
