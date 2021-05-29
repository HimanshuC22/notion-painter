package com.example.iitinder.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.iitinder.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginphoneActivity extends AppCompatActivity {

    private EditText phone_edit,phone_password;
    private Button phone_login_button;
    private FirebaseAuth mAuth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginphone);

        phone_edit=findViewById(R.id.phone_filled);
        phone_password=findViewById(R.id.phonepasswordfilled);

        phone_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_no= phone_edit.getText().toString();
                String phone_pass=phone_password.getText().toString();

                if(phone_edit.length()==0){
                    phone_edit.setError("Fill Email");
                }

                if(phone_password.length()==0){
                    phone_password.setError("Fill Password");
                }
                
            }
        });
    }
}