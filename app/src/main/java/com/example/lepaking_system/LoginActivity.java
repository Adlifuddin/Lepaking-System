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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private Button loginButton; //initialize button
    private EditText inputEmail, inputPass; //initialize edittext
    private TextView signupLink; //initialize textview

    private FirebaseAuth userAuth; //initialize firebase authentication
    private FirebaseAuth.AuthStateListener userAuthListener; //initialize AuthStateListener

    //main function in LoginActivity class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userAuth = FirebaseAuth.getInstance();

        //login button function
        loginButton = (Button) findViewById(R.id.signinButton);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //get user email
                inputEmail = (EditText) findViewById(R.id.email);
                String enteredEmail = (String) inputEmail.getText().toString();

                //get user password
                inputPass = (EditText) findViewById(R.id.password);
                String enteredPass = inputPass.getText().toString();

                //validate email and password
                boolean validateEmail = emailValidation(enteredEmail);
                boolean validatePass = passValidation(enteredPass);

                //if both validation is true
                if(validateEmail && validatePass){
                    //check if user already registered in firebase
                    userAuth.signInWithEmailAndPassword(enteredEmail, enteredPass)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //when successfull
                                    if(task.isSuccessful()){
                                        openMain(); //redirect to main
                                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show(); //display sucess
                                    }
                                    //when unsuccessfull
                                    else{
                                        Toast.makeText(LoginActivity.this, "Login error", Toast.LENGTH_SHORT).show(); //display error
                                    }
                                }
                            });
                }
            }
        });

        //find user in database
        userAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser customer = userAuth.getCurrentUser();
            }
        };

        //create sign up link for new members
        signupLink = (TextView) findViewById(R.id.signupPrompt);
        signupLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openSignup();
            }
        });
    }

    //constructor to find user in database
    @Override
    protected void onStart() {
        super.onStart();
        userAuth.addAuthStateListener(userAuthListener);
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

