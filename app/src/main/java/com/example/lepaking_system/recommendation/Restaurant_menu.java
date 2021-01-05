package com.example.lepaking_system.recommendation;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lepaking_system.R;
import com.example.lepaking_system.restaurant.adapter.MenuAdapter;
import com.example.lepaking_system.restaurant.conversion.Email;
import com.example.lepaking_system.restaurant.model.MenuRestaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Restaurant_menu extends Fragment {

    List<MenuRestaurant> menuList;
    RecyclerView recyclerView;
    TextView totalMenu;
    MenuAdapter menuAdapter;
    DatabaseReference reference;
    String restaurant_email;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        restaurant_email = this.getArguments().getString("email");

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_show_menu_restaurant, container, false);

        //Hooks
        recyclerView = root.findViewById(R.id.restaurant_show_menu_recycler_view);
        totalMenu = root.findViewById(R.id.restaurant_show_menu_total_menu);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        menuList = new ArrayList<>();
        restaurant_email = this.getArguments().getString("email");

        reference = FirebaseDatabase.getInstance().getReference("Menu").child(Email.encodeEmail(restaurant_email));
        Query checkMenu = reference.orderByChild("id");

        checkMenu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        MenuRestaurant menu = ds.getValue(MenuRestaurant.class);
                        menuList.add(menu);
                    }
                    menuAdapter = new MenuAdapter(menuList);
                    recyclerView.setAdapter(menuAdapter);
                    totalMenu.setText("Total Menu(s): " + snapshot.getChildrenCount());

                } else {
                    totalMenu.setText("Total Menu(s): " + 0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
