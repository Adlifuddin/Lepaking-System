package com.example.lepaking_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class UserUpdateActivity extends AppCompatActivity {

    private EditText oldPass, newPass;
    private Button confirmButton, backButton;

    FirebaseUser cust = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpass_update);

        confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                oldPass = (EditText) findViewById(R.id.oldPassword);
                String enteredOldPass = (String) oldPass.getText().toString();

                newPass = (EditText) findViewById(R.id.newPassword);
                String enteredNewPass = (String) newPass.getText().toString();

                FirebaseAuth userAuth = FirebaseAuth.getInstance();
                userAuth.signInWithEmailAndPassword(cust.getEmail(), enteredOldPass)
                        .addOnCompleteListener(UserUpdateActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //when successfull
                                if(task.isSuccessful()){
                                    cust.updatePassword(enteredNewPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(UserUpdateActivity.this, "Password Updated", Toast.LENGTH_SHORT).show(); //display sucess
                                                openProfile();
                                            }
                                            else{
                                                Toast.makeText(UserUpdateActivity.this, "Fail Update", Toast.LENGTH_SHORT).show(); //display sucess
                                            }
                                        }
                                    });
                                }
                                //when unsuccessfull
                                else{
                                    Toast.makeText(UserUpdateActivity.this, "Wrong Old Password", Toast.LENGTH_SHORT).show(); //display error
                                }
                            }
                        });
            }
        });

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });

    }

    //function to change to main page
    public void openProfile(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("viewpager_position", 4);
        startActivity(intent);
    }
}
