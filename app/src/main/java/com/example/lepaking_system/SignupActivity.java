package com.example.lepaking_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    private Button signupButton; //initialize button
    private EditText inputEmail, inputPass; //initialize edittext

    //main function in SignupActivity class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //signup button function
        signupButton = (Button) findViewById(R.id.nextButton);
        signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openProfile();
            }
        });
    }

    //function to change to profile page
    public void openProfile(){
        Intent intent = new Intent(this, ProfileSetupActivity.class);
        startActivity(intent);
        finish();
    }
}
