package com.example.iitinder.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iitinder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_password extends AppCompatActivity {


    private TextView mail;
    private Button send,back;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mail=findViewById(R.id.mailforgotpwd);
        send=findViewById(R.id.reset);
        back=findViewById(R.id.back);

        mAuth=FirebaseAuth.getInstance();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mailstr;
                mailstr=mail.getText().toString();

                if(mail.length()==0){
                    mail.setError("Enter Email");
                }

                if(!TextUtils.isEmpty(mailstr)){
                    mAuth.sendPasswordResetEmail(mailstr).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Forgot_password.this,"password reset link has been sent to your mail",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Forgot_password.this,First_page.class));
                                finish();
                            }else{
                                Toast.makeText(Forgot_password.this,"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }else{
                    Toast.makeText(Forgot_password.this,"Please fill your Email",Toast.LENGTH_SHORT).show();
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Forgot_password.this,First_page.class));
                finish();
            }
        });


    }
}