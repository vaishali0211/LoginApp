package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginapp.Common.Common;
import com.example.loginapp.Helper.FontType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    TextView forgotPass, txt8, openSignup;
    FrameLayout btnLogin;
    EditText userName, password;

    private ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    String dummyName, dummyEmail, dummyPassword, dummyNumber;

    private final static int RC_SIGN_IN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewGroup root = (ViewGroup)findViewById(R.id.root);
//        FontType fontType=new FontType(MainActivity.this,root);
        forgotPass = (TextView) findViewById(R.id.forgotpass);
        btnLogin = (FrameLayout) findViewById(R.id.login_button);
        txt8 = (TextView) findViewById(R.id.txt1);
        openSignup = (TextView) findViewById(R.id.txt2);
        userName = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPass);

        progressDialog = new ProgressDialog(this);

        //firebase init
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        openSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        //username password login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    if(userName.getText().toString().equals("") ){
                        userName.setError("Enter Email address");
                        userName.requestFocus();
                    }

                    else if(password.getText().toString().equals("")){
                        password.setError("Enter password");
                        password.requestFocus();
                    }else{
                        validate(userName.getText().toString(), password.getText().toString());
                    }

                }
                else {
                    Toast.makeText(MainActivity.this,"Please check your internet Connection !!",Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            private void validate(String s, String s1) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(s,s1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseAuthException e = (FirebaseAuthException)task.getException();
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            //Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            checkEmailVerification();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Login Failed"+e.getErrorCode(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
//                Intent intent=new Intent(MainActivity.this,ForgotPassword.class);
//                startActivity(intent);
            }
        });


    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag=firebaseUser.isEmailVerified();

        if(emailflag){
            finish();
            Intent intent=new Intent(MainActivity.this,HomeActivity.class);
            // intent.putExtra("Title","Update Profile");
            startActivity(intent);
        }else{
            //Toast.makeText(this,"Verify your email",Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("First verify your email")
                    .setPositiveButton("ok",null).setCancelable(false);
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
            firebaseAuth.signOut();
        }
    }



}

