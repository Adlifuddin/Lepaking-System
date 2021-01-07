package com.example.lepaking_system;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lepaking_system.restaurant.conversion.Email;
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
    TextView displayPrice;
    Float price = null;

    DatabaseReference custDb; //initialize database reference

    FirebaseUser cust = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_userpayment, container, false);

        qrImage = v.findViewById(R.id.qrPayment);
        displayPrice = v.findViewById(R.id.displayTotal);

        //get id
        String id = cust.getUid();

        //initialize database path
        custDb = FirebaseDatabase.getInstance().getReference("CurrentOrder").child(id);
        custDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String orders = "";
                int z = 0;
                int count = (int) dataSnapshot.getChildrenCount();
                String tgk[] = new String[count];
                int x = 0;

                //to loop and store in array
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {

                    tgk[x] = childSnapshot.getKey();
                    x++;
                }

                boolean v = false;
                boolean b = false;

                while(z<count){

                    if(tgk[z].equals("Total")){

                        b = true;
                    }

                    z++;
                }

                if(!b){

                    displayPrice.setText("RM " + 0);
                }
                else{

                    for(int i = 0; i<count; i++){

                        orders = orders + tgk[i] + " " + roundTo2Decs(Float.parseFloat(String.valueOf(dataSnapshot.child(tgk[i]).getValue()))) + ";";
                        price = roundTo2Decs(Float.parseFloat(String.valueOf(dataSnapshot.child(tgk[i]).getValue())));
                    }
                    displayPrice.setText("RM " + price);
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

    private float roundTo2Decs(float value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }



}
