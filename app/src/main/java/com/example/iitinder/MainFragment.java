package com.example.iitinder;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.iitinder.LoginSignup.First_page;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(getActivity()==null) return;
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private FirebaseAuth mAuth;
    private String current_user_id;
    private Button signout,submit;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private EditText number;
    private String phonenumber;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        current_user_id=mAuth.getUid();

        signout=view.findViewById(R.id.signout);
        db=FirebaseDatabase.getInstance();
        ref=db.getReference().child("users2");
        submit=view.findViewById(R.id.submit);
        number=view.findViewById(R.id.number);
        phonenumber=number.getText().toString();
        mAuth=FirebaseAuth.getInstance();
        submit = view.findViewById(R.id.submit);



        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), First_page.class));
                getParentFragmentManager().beginTransaction().remove(MainFragment.this).commit();
            }
        });
    }
}