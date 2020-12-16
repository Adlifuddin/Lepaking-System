package com.example.lepaking_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    private Button loginButton; //initialize button
    private EditText inputEmail, inputPass; //initialize edittext
    private TextView signupLink; //initialize textview

    //main function in LoginActivity class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //login button function
        loginButton = (Button) findViewById(R.id.signinButton);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openMain();
            }
        });

        //create sign up link for new members
        signupLink = (TextView) findViewById(R.id.signupPrompt);
        signupLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openSignup();
            }
        });

    }

    //function to change to signup page
    public void openSignup(){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }

    //function to change to main page
    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}

