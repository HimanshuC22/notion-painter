package com.example.iitinder.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.iitinder.R;
import com.example.iitinder.RegistrationStartActivity;

public class First_page extends AppCompatActivity {

    private Button loginemail, loginphone, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        loginemail = findViewById(R.id.loginemail);
        loginphone = findViewById(R.id.loginphone);
        signup = findViewById(R.id.signup);

        loginemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(First_page.this, LoginActivity.class));
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(First_page.this, RegistrationStartActivity.class));
                finish();
            }
        });

        loginphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(First_page.this, Login_phone_Activity.class));
                finish();
            }
        });
    }
}