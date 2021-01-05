package com.example.lepaking_system;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lepaking_system.restaurant.model.MenuRestaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class DisplayMenuDetails<PassDataInterface> extends RecyclerView.Adapter{

    List<MenuRestaurant> menuList;

    public DisplayMenuDetails(List<MenuRestaurant> menuList) {
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_display_menu_details, parent, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return viewHolderClass;
    }

    float total = 0;

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        FirebaseUser cust = FirebaseAuth.getInstance().getCurrentUser();
        String id = cust.getUid();



        DatabaseReference reforder = FirebaseDatabase.getInstance().getReference("CurrentOrder").child(id);

        ViewHolderClass viewHolderClass = (ViewHolderClass)holder;
        MenuRestaurant restaurantMenu = menuList.get(position);
        viewHolderClass.menuName.setText(restaurantMenu.getName());
        viewHolderClass.menuPrice.setText("RM " + String.valueOf(roundTo2Decs(restaurantMenu.getPrice())));
        viewHolderClass.menuType.setText(restaurantMenu.getType());
        viewHolderClass.addCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                reforder.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        int z = 0;
                        int count = (int) snapshot.getChildrenCount();
                        String tgk[] = new String[count];
                        int x = 0;

                        //to loop and store in array
                        for (DataSnapshot childSnapshot: snapshot.getChildren()) {

                            tgk[x] = childSnapshot.getKey();
                            x++;
                        }

                        boolean v = false;
                        boolean b = false;

                        while(z<count){

                            if(tgk[z].equals("Total")){

                                b = true;
                            }

                            if (tgk[z].equals(restaurantMenu.getName())) {

                                v = true;
                            }

                            z++;
                        }

                        if(!v){

                            reforder.child(restaurantMenu.getName()).setValue(1);
                        }
                        else{

                            int naik = Integer.parseInt(String.valueOf(snapshot.child(restaurantMenu.getName()).getValue()));

                            naik++;
                            reforder.child(restaurantMenu.getName()).setValue(naik);
                        }

                        if(b == true){

                            total = Float.parseFloat(String.valueOf(snapshot.child("Total").getValue()));
                            total += restaurantMenu.getPrice();
                            reforder.child("Total").setValue(total);
                        }

                        else{

                            reforder.child("Total").setValue(restaurantMenu.getPrice());
                        }

                        System.out.println(total);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//                passDataInterface.onDataReceived("Demo Data Sending By Fragment\n");
//                System.out.println(restaurantMenu.getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{

        TextView menuName, menuPrice, menuType;
        CardView addCart;

        public ViewHolderClass(@NonNull View itemView) {

            super(itemView);

            menuName = itemView.findViewById(R.id.restaurant_show_menu_name);
            menuPrice = itemView.findViewById(R.id.restaurant_show_menu_price);
            menuType = itemView.findViewById(R.id.restaurant_show_menu_type);
            addCart= itemView.findViewById(R.id.restaurant_show_menu_update_button);

        }
    }
    private float roundTo2Decs(float value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

}