package com.example.iitinder.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iitinder.MainActivity;
import com.example.iitinder.R;
import com.example.iitinder.RegistrationStartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private CheckBox showpassword;
    private Button Forgotpassword, login2;
    TextView signup2;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("DETAILS", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        email = findViewById(R.id.emailfiled);
        password = findViewById(R.id.phonepasswordfilled);
        showpassword = findViewById(R.id.checkBox);
        Forgotpassword = findViewById(R.id.forgot);
        login2 = findViewById(R.id.phone_login_btn);
        signup2 = findViewById(R.id.signUp2);
        mAuth = FirebaseAuth.getInstance();

        signup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationStartActivity.class));
            }
        });

        showpassword.setSelected(false);
        showpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password.setTransformationMethod(null);
                } else {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });


        Forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Forgot_password.class));
                finish();
            }
        });

        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailstr = email.getText().toString();
                String passwordstr = password.getText().toString();

                if (email.length() == 0) {
                    email.setError("Fill Email");
                }

                if (password.length() == 0) {
                    password.setError("Fill Password");
                }

                if (!TextUtils.isEmpty(emailstr) && !TextUtils.isEmpty(passwordstr)) {
                    mAuth.signInWithEmailAndPassword(emailstr, passwordstr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                if (task.isSuccessful()) {

                                    if (mAuth.getCurrentUser().isEmailVerified()) {
                                        editor.putString("EMAIL", emailstr).apply();
                                        editor.putString("PASSWORD", passwordstr).apply();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Please verify your mail", Toast.LENGTH_SHORT).show();
                                    }


                                } else {
                                    Toast.makeText(LoginActivity.this, "Erroe: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Toast.makeText(LoginActivity.this, "Verify youraccount by mail sent to your email adress", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "fill all fields", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                }
            }
        });


    }
}