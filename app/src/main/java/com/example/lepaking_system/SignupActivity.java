package com.example.lepaking_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private Button nextButton; //initialize button
    private EditText inputEmail, inputPass; //initialize edittext

    FirebaseAuth userAuth; //initialize firebase authentication

    //main function in SignupActivity class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        userAuth = FirebaseAuth.getInstance(); //get firebase authentication instance

        //signup button function
        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //get user email
                inputEmail = (EditText) findViewById(R.id.newEmail);
                final String enteredEmail = (String) inputEmail.getText().toString();

                //get user password
                inputPass = (EditText) findViewById(R.id.newPassword);
                String enteredPass = inputPass.getText().toString();

                //validate email and password
                boolean validateEmail = emailValidation(enteredEmail);
                boolean validatePass = passValidation(enteredPass);

                //if both validation is true
                if(validateEmail && validatePass){
                    //create user with email and password
                    userAuth.createUserWithEmailAndPassword(enteredEmail, enteredPass)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //when successfull
                                    if(task.isSuccessful()){
                                        openProfile(enteredEmail); //go to profile
                                        Toast.makeText(SignupActivity.this, "Signup Success" + enteredEmail, Toast.LENGTH_SHORT).show();
                                    }
                                    //when unsuccessfull
                                    else{
                                        Toast.makeText(SignupActivity.this, "Email has been used", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

        });
    }

    //function to change to profile page
    public void openProfile(String email){
        Intent intent = new Intent(this, ProfileSetupActivity.class);
        startActivity(intent);
        finish();
    }

    //function for email validation
    public Boolean emailValidation(String enteredEmail){
        boolean test = true;

        if(enteredEmail.isEmpty()){
            inputEmail.setError("Email Field cannot be empty");
            test = false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(enteredEmail).matches()){
            inputEmail.setError("Please enter a valid email");
            test = false;
        }
        else{
            inputEmail.setError(null);
            test=true;
        }

        return test;
    }

    //function for password validation
    public Boolean passValidation(String enteredPass){
        boolean test = true;
        if(enteredPass.isEmpty()){
            inputPass.setError("Password Field cannot be empty");
            test = false;
        }
        else if(enteredPass.length() < 8){
            inputPass.setError("Password must be atleast 8 characters");
            test = false;
        }
        else{
            inputPass.setError(null);
            test=true;
        }

        return test;
    }
}
