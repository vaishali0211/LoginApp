package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginapp.Common.Common;
import com.example.loginapp.Helper.FontType;
import com.example.loginapp.Model.Sign_Up;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    TextView openSignin;
    EditText email,number,userName,password;
    CheckBox cb;

    FrameLayout btnLogin;
    private ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    String dummyName,dummyEmail,dummyPassword,dummyNumber;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ViewGroup root = (ViewGroup)findViewById(R.id.root);
//        FontType fontType=new FontType(SignUpActivity.this,root);

        btnLogin = (FrameLayout) findViewById(R.id.login_button);
        openSignin = (TextView) findViewById(R.id.txt2);


        cb = (CheckBox) findViewById(R.id.term_condition);

        userName = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPass);
        email = (EditText) findViewById(R.id.etMail);
        number = (EditText) findViewById(R.id.etNumber);

        img = (ImageView)findViewById(R.id.ivicon);

        progressDialog = new ProgressDialog(this);

        //firebase init
        firebaseAuth = FirebaseAuth.getInstance();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        openSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    if (validate()) {
                        if(cb.isChecked()){
                            //it register to database
                            progressDialog.setMessage("Please Wait...");
                            progressDialog.show();
                            String user_Email = email.getText().toString().trim();
//                        final String user_Number = number.getText().toString().trim();
//                        final String user_Name = userName.getText().toString().trim();
                            String user_Password = password.getText().toString().trim();
                            int length = user_Password.length();
                            if(length >= 8){
                                firebaseAuth.createUserWithEmailAndPassword(user_Email, user_Password).addOnCompleteListener(SignUpActivity.this ,new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        FirebaseAuthException e = (FirebaseAuthException)task.getException();
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            sendEmailVerification();
                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Signup Failed"+e.getErrorCode(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else {
                                password.setError("Enter Strong password");
                                password.requestFocus();
                            }
                        }
                        else{
                            Toast.makeText(SignUpActivity.this,"Please click on Term and conditions",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(SignUpActivity.this,"Please check your internet Connection !!",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    public Boolean validate(){
        Boolean result = false;

        dummyEmail = email.getText().toString();
        dummyNumber = number.getText().toString();
        dummyName = userName.getText().toString();
        dummyPassword = password.getText().toString();
        if(dummyEmail.equalsIgnoreCase("")){
            email.setError("Enter Email address");
            email.requestFocus();
        }
        else if(dummyNumber.equalsIgnoreCase("")){
            number.setError("Enter Mobile number");
            number.requestFocus();
        }
        else if(dummyName.equalsIgnoreCase("")){
            userName.setError("Enter Username");
            userName.requestFocus();
        }
        else if(dummyPassword.equalsIgnoreCase("")){
            password.setError("Enter Password");
            password.requestFocus();
        }
        else{
            result = true;
        }
        return result;
    }

    private void sendEmailVerification(){
        final FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserData();
                        //Toast.makeText(Register.this,"Successfully registered , Varification mail has been sent!",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder=new AlertDialog.Builder(SignUpActivity.this);
                        builder.setMessage("Successfully signup , Varification mail has been sent!")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        firebaseAuth.signOut();
                                        finish();
                                        startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                                    }
                                }).setCancelable(false);
                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();
                    }else{
                        Toast.makeText(SignUpActivity.this,"Varification mail has'nt sent!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void sendUserData(){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef=firebaseDatabase.getReference("users").child(firebaseAuth.getUid());
        Sign_Up sign_up=new Sign_Up(dummyName,dummyNumber,dummyEmail);
        myRef.setValue(sign_up);
        //Common.currentUser = userProfile;
    }


}
