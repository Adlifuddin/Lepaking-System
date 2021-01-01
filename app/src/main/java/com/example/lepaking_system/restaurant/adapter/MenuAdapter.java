package com.example.lepaking_system.restaurant.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.lepaking_system.R;
import com.example.lepaking_system.restaurant.activity.UpdateMenuRestaurant;
import com.example.lepaking_system.restaurant.conversion.Email;
import com.example.lepaking_system.restaurant.fragment.ShowMenuRestaurant;
import com.example.lepaking_system.restaurant.model.MenuRestaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter{

    List<MenuRestaurant> menuList;

    int restaurant_noOfMenu;

    public MenuAdapter(List<MenuRestaurant> menuList) {
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_restaurant_menu, parent, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolderClass viewHolderClass = (ViewHolderClass)holder;
        MenuRestaurant restaurantMenu = menuList.get(position);
        viewHolderClass.menuName.setText(restaurantMenu.getName());
        viewHolderClass.menuPrice.setText("RM " + String.valueOf(roundTo2Decs(restaurantMenu.getPrice())));
        viewHolderClass.menuType.setText(restaurantMenu.getType());
        viewHolderClass.menuID.setText(restaurantMenu.getId());

        viewHolderClass.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context;
                context = v.getContext();
                Intent intent = new Intent(context, UpdateMenuRestaurant.class);
                intent.putExtra("RESTAURANTMENU", restaurantMenu);
                context.startActivity(intent);
            }
        });

        viewHolderClass.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create alert for delete confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Confirmation !!!");
                builder.setMessage("Are you sure to delete " + restaurantMenu.getName() + " ?");
                builder.setIcon(android.R.drawable.ic_menu_delete);
                builder.setCancelable(false);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference referenceMenu = FirebaseDatabase.getInstance().getReference("Menu").child(Email.encodeEmail(restaurantMenu.getEmail()));
                        referenceMenu.child(restaurantMenu.getId()).removeValue();

                        //toast messaged saying deleted
                        Toast.makeText(v.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        menuList.remove(restaurantMenu);
                        notifyDataSetChanged();

                        DatabaseReference referenceRestaurant = FirebaseDatabase.getInstance().getReference("Restaurant");
                        Query incrementMenuCounter = referenceRestaurant.orderByChild("email").equalTo(Email.encodeEmail(restaurantMenu.getEmail()));

                        incrementMenuCounter.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    restaurant_noOfMenu = snapshot.child(Email.encodeEmail(restaurantMenu.getEmail())).child("menuCounter").getValue(int.class);
                                    restaurant_noOfMenu -= 1;
                                    referenceRestaurant.child(Email.encodeEmail(restaurantMenu.getEmail())).child("menuCounter").setValue(restaurant_noOfMenu);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        ShowMenuRestaurant showMenuRestaurant = new ShowMenuRestaurant();
                        Bundle bundle = new Bundle();
                        bundle.putString("email", Email.encodeEmail(restaurantMenu.getEmail()));
                        showMenuRestaurant.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, showMenuRestaurant).commit();

                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{

        TextView menuName, menuPrice, menuType, menuID;
        CardView updateButton, deleteButton;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.restaurant_show_menu_name);
            menuPrice = itemView.findViewById(R.id.restaurant_show_menu_price);
            menuType = itemView.findViewById(R.id.restaurant_show_menu_type);
            menuID = itemView.findViewById(R.id.restaurant_show_menu_id);
            updateButton = itemView.findViewById(R.id.restaurant_show_menu_update_button);
            deleteButton = itemView.findViewById(R.id.restaurant_show_menu_delete_button);
        }
    }

    private float roundTo2Decs(float value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}
