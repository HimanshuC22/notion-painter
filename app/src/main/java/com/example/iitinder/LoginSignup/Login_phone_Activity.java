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


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.app.AlertDialog;

import com.example.iitinder.MainActivity;
import com.example.iitinder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class Login_phone_Activity extends AppCompatActivity {

    private EditText phone_edit, phone_password;
    private Button phone_login_button;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone_);

        phone_edit = findViewById(R.id.phone_filled);
        phone_password = findViewById(R.id.phonepasswordfilled);
        phone_login_button = findViewById(R.id.phone_login_btn);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        phone_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_no = phone_edit.getText().toString();
                String phone_pass = phone_password.getText().toString();

                if (phone_edit.length() == 0) {
                    phone_edit.setError("Fill Phone No.");
                }

                if (phone_password.length() == 0) {
                    phone_password.setError("Fill Password");
                }

                if (!TextUtils.isEmpty(phone_no) && !TextUtils.isEmpty(phone_pass)) {
                    //connect to the database and set the data URL
                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://notion-painter-default-rtdb.firebaseio.com/");
                    DatabaseReference ref = database.getReference("/map/" + phone_no);
                    ref.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //if the database contains the LDAP id, value will be name of user
                            String user_mail = dataSnapshot.getValue(String.class);
                            if (user_mail != null) {
                                Log.d("FIREBASE", "Value is " + user_mail);
                                FirebaseDatabase database1 = FirebaseDatabase.getInstance("https://notion-painter-default-rtdb.firebaseio.com/");
                                DatabaseReference ref1 = database1.getReference("/users/" + user_mail);

                                ref1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String user_name = snapshot.getValue(String.class);
                                        Log.d("FIREBASE", "Valuuuuue is " + user_name);

                                        Log.d("FIREBASE", "Value is " + user_mail + " --" + phone_pass);
                                        String email = user_mail + "@iitb.ac.in";
                                        try {
                                            mAuth.signInWithEmailAndPassword(email, phone_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    try {
                                                        if (mAuth.getCurrentUser().isEmailVerified()) {


                                                            if (task.isSuccessful()) {

                                                                if (mAuth.getCurrentUser().isEmailVerified()) {
                                                                    startActivity(new Intent(Login_phone_Activity.this, MainActivity.class));
                                                                    finish();
                                                                } else {
                                                                    Toast.makeText(Login_phone_Activity.this, "Please verify your mail", Toast.LENGTH_SHORT).show();
                                                                }


                                                            } else {
                                                                Toast.makeText(Login_phone_Activity.this, "Error while checking password: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(Login_phone_Activity.this, "Please Enter correct registered user " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (Exception e) {
                                                        Toast.makeText(Login_phone_Activity.this, "Please Enter correct registered user " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }


                                                }
                                            });
                                        } catch (Exception e) {
                                            Toast.makeText(Login_phone_Activity.this, "Please Enter correct registered user ", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });


                } else {
                    Toast.makeText(Login_phone_Activity.this, "Please Fill Phone no. and Pass", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}