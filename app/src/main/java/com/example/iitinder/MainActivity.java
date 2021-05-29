package com.example.iitinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.iitinder.LoginSignup.First_page;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String current_user_id;
    private Button signout,submit;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private EditText number;
    private String phonenumber;



    @Override
    protected void onStart() {
        super.onStart();
        if(current_user_id==null || !(mAuth.getCurrentUser().isEmailVerified())){
            startActivity(new Intent(MainActivity.this, First_page.class));
            finish();;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        current_user_id=mAuth.getUid();

        signout=findViewById(R.id.signout);
        db=FirebaseDatabase.getInstance();
        ref=db.getReference().child("users2");
        submit=findViewById(R.id.submit);
        number=findViewById(R.id.number);
        phonenumber=number.getText().toString();
        mAuth=FirebaseAuth.getInstance();



        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this,First_page.class));
                finish();
            }
        });


    }
}