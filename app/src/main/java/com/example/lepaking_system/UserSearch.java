package com.example.lepaking_system;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lepaking_system.recommendation.Restaurant_Display;
import com.example.lepaking_system.recommendation.Restaurantdata;
import com.example.lepaking_system.recommendation.Userdata;
import com.example.lepaking_system.restaurant.model.Restaurant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserSearch extends Fragment {
    
    private EditText search_restaurant;
    private ImageButton search_btn,recommend_btn;
    private RecyclerView result_list;
    private DatabaseReference databaseReference;
    private static Context mContext;
    private TextView recommend_name, recommend_price, recommend_type, recommend_customer,recommend_capacity;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.danial_restaurant_list, container, false);

        mContext = getActivity();

        databaseReference = FirebaseDatabase.getInstance().getReference("Restaurant");

        search_restaurant = (EditText) v.findViewById(R.id.search_restaurant_txt);
        search_btn = (ImageButton) v.findViewById(R.id.search_btn);
        result_list = (RecyclerView) v.findViewById(R.id.result_list);

        recommend_name = (TextView) v.findViewById(R.id.recommend_name);
        recommend_price = (TextView) v.findViewById(R.id.recommend_price);
        recommend_type = (TextView) v.findViewById(R.id.recommend_type);
        recommend_customer = (TextView) v.findViewById(R.id.recommend_cus);
        recommend_capacity = (TextView) v.findViewById(R.id.recommend_cus2);
        recommend_btn = (ImageButton) v.findViewById(R.id.recommend_btn);
        //recommend_name.setText(databaseReference.child(Productkey).child("name").getKey());


        result_list.setHasFixedSize(true);
        result_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        //cuba utk recommend
        //String Productkey = "danial@gmail,com";
        FirebaseUser cust = FirebaseAuth.getInstance().getCurrentUser();
        String id = cust.getUid();
        recommendBased(id);

        //i just need the product key

        search_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                String searchRes = search_restaurant.getText().toString();
                search_item(searchRes);
            }
        });
        return v;
    }

    //recommending function from the matrix factorization
    public void recommendBased(String userID) {

        //set user db to user object
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference();
        reff.addListenerForSingleValueEvent(new ValueEventListener() {

            String restaurantID;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //putting the data in restaurantID in array so can randomize the output
                int count = (int) snapshot.child("recommendation_restaurant").getChildrenCount();
                String tgk[] = new String[count];
                int x = 0;
                //to loop and store in array
                for (DataSnapshot childSnapshot: snapshot.child("recommendation_restaurant").getChildren()) {
                    tgk[x] = childSnapshot.getKey();
                    x++;
                }
                int random = (int) ((Math.random()*100) % count);
                restaurantID = tgk[random];
                System.out.println(random);

                //objects for items
                Restaurantdata restaurantdata = new Restaurantdata();
                Userdata userdata = new Userdata();

                //retrieve from USER data db
                float P1 = Float.parseFloat(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("P1").child("rating").getValue()));
                float P2 = Float.parseFloat(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("P2").child("rating").getValue()));
                float P3 = Float.parseFloat(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("P3").child("rating").getValue()));
                float T1 = Float.parseFloat(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("T1").child("rating").getValue()));
                float T2 = Float.parseFloat(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("T2").child("rating").getValue()));
                float T3 = Float.parseFloat(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("T3").child("rating").getValue()));
                float T4 = Float.parseFloat(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("T4").child("rating").getValue()));
                int P1total = Integer.parseInt(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("P1").child("restaurant_number").getValue()));
                int P2total = Integer.parseInt(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("P2").child("restaurant_number").getValue()));
                int P3total = Integer.parseInt(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("P3").child("restaurant_number").getValue()));
                int T1total = Integer.parseInt(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("T1").child("restaurant_number").getValue()));
                int T2total = Integer.parseInt(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("T2").child("restaurant_number").getValue()));
                int T3total = Integer.parseInt(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("T3").child("restaurant_number").getValue()));
                int T4total = Integer.parseInt(String.valueOf(snapshot.child("Recommendation_Customer").child(userID).child("T4").child("restaurant_number").getValue()));

                userdata.setP1(P1);
                userdata.setP2(P2);
                userdata.setP3(P3);
                userdata.setT1(T1);
                userdata.setT2(T2);
                userdata.setT3(T3);
                userdata.setT4(T4);

                userdata.setP1total(P1total);
                userdata.setP2total(P2total);
                userdata.setP3total(P3total);
                userdata.setT1total(T1total);
                userdata.setT2total(T2total);
                userdata.setT3total(T3total);
                userdata.setT4total(T4total);

                //retrieve from RESTAURANT data db
                int Pp1 = Integer.parseInt(String.valueOf(snapshot.child("recommendation_restaurant").child(restaurantID).child("P1").child("rating").getValue()));
                int Pp2 = Integer.parseInt(String.valueOf(snapshot.child("recommendation_restaurant").child(restaurantID).child("P2").child("rating").getValue()));
                int Pp3 = Integer.parseInt(String.valueOf(snapshot.child("recommendation_restaurant").child(restaurantID).child("P3").child("rating").getValue()));
                int Tp1 = Integer.parseInt(String.valueOf(snapshot.child("recommendation_restaurant").child(restaurantID).child("T1").child("rating").getValue()));
                int Tp2 = Integer.parseInt(String.valueOf(snapshot.child("recommendation_restaurant").child(restaurantID).child("T2").child("rating").getValue()));
                int Tp3 = Integer.parseInt(String.valueOf(snapshot.child("recommendation_restaurant").child(restaurantID).child("T3").child("rating").getValue()));
                int Tp4 = Integer.parseInt(String.valueOf(snapshot.child("recommendation_restaurant").child(restaurantID).child("T4").child("rating").getValue()));
                int total = Integer.parseInt(String.valueOf(snapshot.child("recommendation_restaurant").child(restaurantID).child("Total").child("value").getValue()));
                float rating = Float.parseFloat(String.valueOf(snapshot.child("recommendation_restaurant").child(restaurantID).child("R").child("value").getValue()));

                restaurantdata.setP1(Pp1);
                restaurantdata.setP2(Pp2);
                restaurantdata.setP3(Pp3);
                restaurantdata.setT1(Tp1);
                restaurantdata.setT2(Tp2);
                restaurantdata.setT3(Tp3);
                restaurantdata.setT4(Tp4);
                restaurantdata.setTotal(total);
                restaurantdata.setRating(rating);

                if (restaurantdata.getTotal() == 0 ||
                        (restaurantdata.P1 == 1 && userdata.P1 == 0) ||
                        (restaurantdata.P2 == 1 && userdata.P2 == 0) ||
                        (restaurantdata.P3 == 1 && userdata.P3 == 0) ||
                        (restaurantdata.T1 == 1 && userdata.T1 == 0) ||
                        (restaurantdata.T2 == 1 && userdata.T2 == 0) ||
                        (restaurantdata.T3 == 1 && userdata.T3 == 0) ||
                        (restaurantdata.T4 == 1 && userdata.T4 == 0)) {
                    //kena recommend this restaurant
                    DatabaseReference recommend_ref = databaseReference.child(restaurantID);
                    recommend_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            recommend_name.setText(String.valueOf(snapshot.child("name").getValue()));
                            recommend_price.setText(String.valueOf(snapshot.child("menuPriceRange").getValue()));
                            recommend_type.setText(String.valueOf(snapshot.child("type").getValue()));
                            recommend_customer.setText(String.valueOf(snapshot.child("customerCounter").getValue()));
                            recommend_capacity.setText("/ " + String.valueOf(snapshot.child("restaurantCapacity").getValue()));
                            recommend_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //bukak restaurant tu
                                    System.out.println(restaurantID);
                                    gotoRestaurant(restaurantID,mContext);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    System.out.println("1. WE RECOMMEND THIS RESTAURANT: " + restaurantID);
                }
                else {

                    float ratingtester = restaurantdata.P1 * userdata.P1 + restaurantdata.P2 * userdata.P2 + restaurantdata.P3 * userdata.P3 +
                            restaurantdata.T1 * userdata.T1 + restaurantdata.T2 * userdata.T2 + restaurantdata.T3 * userdata.T3 + restaurantdata.T4 * userdata.T4;
                    ratingtester = ratingtester / (restaurantdata.P1 + restaurantdata.P2 + restaurantdata.P3 + restaurantdata.T1 + restaurantdata.T2 + restaurantdata.T3 + restaurantdata.T4);

                    if (ratingtester >= 3) {
                        //kena recommend this restaurant
                        DatabaseReference recommend_ref = databaseReference.child(restaurantID);
                        recommend_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                recommend_name.setText(String.valueOf(snapshot.child("name").getValue()));
                                recommend_price.setText(String.valueOf(snapshot.child("menuPriceRange").getValue()));
                                recommend_type.setText(String.valueOf(snapshot.child("type").getValue()));
                                recommend_customer.setText(String.valueOf(snapshot.child("customerCounter").getValue()));
                                recommend_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //bukak restaurant tu
                                        System.out.println(restaurantID);
                                        gotoRestaurant(restaurantID,mContext);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        //kena recommend this restaurant
                        System.out.println("2. WE RECOMMEND THIS RESTAURANT: " + restaurantID + "WITH RATING OF " + ratingtester);
                    } else {
                        //kena recommend this restaurant
                        DatabaseReference recommend_ref = databaseReference.child(restaurantID);
                        recommend_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                recommend_name.setText("No Recommendation yet");
                                recommend_price.setText("0");
                                recommend_type.setText("");
                                recommend_customer.setText("");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        System.out.println("3. WE DO NOT RECOMMEND THIS RESTAURANT: " + restaurantID + "WITH RATING OF " + ratingtester);
                        recommendBased(userID);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void search_item(String searchRes) {

        Query firebaseSearchQuery = databaseReference.orderByChild("name").startAt(searchRes).endAt(searchRes + "\uf8ff");

        FirebaseRecyclerAdapter<Restaurant, UserSearch.RestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Restaurant, UserSearch.RestViewHolder>(
                Restaurant.class,
                R.layout.danial_restaurant_list1,
                UserSearch.RestViewHolder.class,
                firebaseSearchQuery
        ) {
            @Override
            protected void populateViewHolder(UserSearch.RestViewHolder restViewHolder, Restaurant restaurant, int i) {

                restViewHolder.setDetails(restaurant.getRestaurantCapacity(), getActivity().getApplicationContext(), restaurant.getEmail(), restaurant.getName(), restaurant.getType(), restaurant.getMenuPriceRange(), String.valueOf(restaurant.getCustomerCounter()));
            }
        };

        result_list.setAdapter(firebaseRecyclerAdapter);
    }

    public static class RestViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public RestViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setDetails(int capacity, Context ctx, String restauEmail, String restauname, String restautype, String restauprice, String restaucustomer){

            TextView restau_name = (TextView) mView.findViewById(R.id.restaurant_name1_txt);
            TextView restau_type = (TextView) mView.findViewById(R.id.type1_txt);
            TextView restau_price = (TextView) mView.findViewById(R.id.price1_txt);
            TextView restau_customer = (TextView) mView.findViewById(R.id.customerNo1_txt);
            ImageButton restau_link = (ImageButton) mView.findViewById(R.id.restaurant_link);
            TextView restau_capacity = (TextView) mView.findViewById(R.id.customerNo1_txt2);

            restau_name.setText(restauname);
            restau_type.setText(restautype);
            restau_price.setText(restauprice);
            restau_customer.setText(restaucustomer);
            restau_capacity.setText("/ " + capacity);

            restau_link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //bukak restaurant tu
                    System.out.println(restauEmail);
                    gotoRestaurant(restauEmail,mContext);
                }
            });
        }

    }

    //function to change to main page
    public static void gotoRestaurant(String productkey, Context ctx){
        Intent intent = new Intent(ctx, Restaurant_Display.class);
        intent.putExtra("the product key", productkey);
        ctx.startActivity(intent);
    }

}