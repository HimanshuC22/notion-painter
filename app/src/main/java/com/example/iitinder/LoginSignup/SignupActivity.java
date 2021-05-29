package com.example.iitinder.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iitinder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

   private EditText emailadd,password1,passwordconfirm;
    private Button create,login3;
    private FirebaseAuth mAuth;
    private boolean poss;
    private String verify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailadd=findViewById(R.id.number);
        password1=findViewById(R.id.password1);
        passwordconfirm=findViewById(R.id.passwordconfirm);

        create=findViewById(R.id.register);
        login3 = findViewById(R.id.login3);
        mAuth=FirebaseAuth.getInstance();
        verify="@iitb.ac.in";





        login3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,First_page.class));
                finish();
            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailstr =emailadd.getText().toString();
                String passwordstr = password1.getText().toString();
                String repasswordstr = passwordconfirm.getText().toString();
                poss=emailstr.contains(verify);


                if(emailadd.length()==0){
                    emailadd.setError("Fill Email");
                }

                if(password1.length()==0){
                    password1.setError("Fill Password");
                }

                if(passwordconfirm.length()==0){
                    passwordconfirm.setError("Fill ConfirmPassword ");
                }




                if(poss) {
                    if(!TextUtils.isEmpty(emailstr)&&!TextUtils.isEmpty(passwordstr)&&!TextUtils.isEmpty(repasswordstr)){
                        if(passwordstr.equals(repasswordstr)){

                            mAuth.createUserWithEmailAndPassword(emailstr,passwordstr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){


                                        mAuth.getCurrentUser().sendEmailVerification();




                                        Toast.makeText(SignupActivity.this,"Please verify your email and then login",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignupActivity.this,First_page.class));
                                        finish();
                                    }else{
                                        Toast.makeText(SignupActivity.this,"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(SignupActivity.this,"Password does not match",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(SignupActivity.this,"Fields should not be empty",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(SignupActivity.this,"User your IITB mail",Toast.LENGTH_SHORT).show();
                }




















            }
        });


        }

    }
