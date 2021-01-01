package com.example.lepaking_system.restaurant.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lepaking_system.R;
import com.example.lepaking_system.restaurant.activity.CustomerTracing;
import com.example.lepaking_system.restaurant.conversion.Email;
import com.example.lepaking_system.restaurant.model.Validation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CustomerTracingAdapter extends RecyclerView.Adapter {
    List<Validation> validationList;

    public CustomerTracingAdapter(List<Validation> validationList){
        this.validationList = validationList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customer_tracing_data, parent, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolderClass viewHolderClass = (ViewHolderClass)holder;
        Validation validation = validationList.get(position);
        viewHolderClass.customerName.setText(validation.getName());
        viewHolderClass.customerEmail.setText(validation.getEmail());
        viewHolderClass.customerPhoneNumber.setText(validation.getPhoneNumber());
        viewHolderClass.customerID.setText(validation.getId());
        viewHolderClass.customerCheckInDate.setText(validation.getCheckInDate());
        viewHolderClass.customerCheckInTime.setText(validation.getCheckInTime());
        viewHolderClass.customerCheckOutDate.setText(validation.getCheckOutDate());
        viewHolderClass.customerCheckOutTime.setText(validation.getCheckOutTime());

        viewHolderClass.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Confirmation !!!");
                builder.setMessage("Are you sure to delete " + validation.getName() + " ?");
                builder.setIcon(android.R.drawable.ic_menu_delete);
                builder.setCancelable(false);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference referenceCustomer = FirebaseDatabase.getInstance().getReference("Customer_Tracing").child(Email.encodeEmail(validation.getRestaurantEmail())).child(validation.getCheckInDate());
                        referenceCustomer.child(validation.getId()).removeValue();

                        //toast messaged saying deleted
                        Toast.makeText(v.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        validationList.remove(validation);
                        notifyDataSetChanged();

                        Intent intent = new Intent(v.getContext(), CustomerTracing.class);
                        intent.putExtra("email", validation.getRestaurantEmail());
                        intent.putExtra("date", validation.getCheckInDate());
                        v.getContext().startActivity(intent);
                        ((Activity)v.getContext()).finish();

                    }
                });

                builder.setNegativeButton("No", null);
                builder.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return validationList.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{

        TextView customerName, customerEmail, customerPhoneNumber, customerCheckInDate, customerCheckInTime, customerCheckOutDate, customerCheckOutTime, customerID;

        CardView deleteButton;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customer_tracing_name);
            customerEmail = itemView.findViewById(R.id.customer_tracing_email);
            customerPhoneNumber = itemView.findViewById(R.id.customer_tracing_phone_number);
            customerCheckInDate = itemView.findViewById(R.id.customer_tracing_check_in_date);
            customerCheckInTime = itemView.findViewById(R.id.customer_tracing_check_in_time);
            customerCheckOutDate = itemView.findViewById(R.id.customer_tracing_check_out_date);
            customerCheckOutTime = itemView.findViewById(R.id.customer_tracing_check_out_time);
            customerID = itemView.findViewById(R.id.customer_tracing_id);
            deleteButton = itemView.findViewById(R.id.customer_tracing_delete_button);
        }
    }
}
