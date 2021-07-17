package com.example.iitinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Activity to show the user profile details of other users
 */

public class OtherProfileActivity extends AppCompatActivity {

    String LDAP;
    ProgressBar progressBar;
    ImageView profilePic, interestNext, back;
    ArrayList<String> main_interests;
    HashMap<String, List<String>> interests_map;
    TextView name, email, mobile, bio, gender, place, birthday;
    ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        LDAP = getIntent().getExtras().getString("ldap");

        setUp();

        back.setOnClickListener(v -> {finish();});

        interestNext.setOnClickListener(v -> {
            Intent intent = new Intent(this, OtherInterestActivity.class);
            intent.putExtra("ldap_interest", LDAP);
            startActivity(intent);
        });

        getPhoto();

        getDetails();

        setUpRecycler();

    }

    public void setUp()
    {
        main_interests = new ArrayList<>();
        interests_map = new HashMap<>();
        back = findViewById(R.id.backFromProfileView);
        name = findViewById(R.id.textNamePerson);
        email = findViewById(R.id.textLDAPPerson);
        gender = findViewById(R.id.textGenderPerson);
        bio = findViewById(R.id.textBioPerson);
        mobile = findViewById(R.id.mobileTextPerson);
        place = findViewById(R.id.placeFromTextPerson);
        birthday = findViewById(R.id.birthdayTextPerson);
        profilePic = findViewById(R.id.profilePhotoPerson);
        profilePic.setVisibility(View.INVISIBLE);
        interestNext = findViewById(R.id.interestForwardPerson);
        progressBar = findViewById(R.id.progressBar4);
        expandableListView = findViewById(R.id.expandableListViewPerson);
    }

    public void getPhoto()
    {
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("photos").child(LDAP).child("profile.jpg");
        reference.getBytes(9*1024*1024).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Glide.with(this).load(bitmap).circleCrop().into(profilePic);
            progressBar.setVisibility(View.GONE);
            profilePic.setVisibility(View.VISIBLE);
        });
    }

    public void getDetails()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("data").child(LDAP).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                name.setText(snapshot.child("name").getValue().toString());
                gender.setText(snapshot.child("gender").getValue().toString().equals("m") ? "Male" : "Female");
                email.setText(LDAP+"@iitb.ac.in");
                mobile.setText(snapshot.child("mobile").getValue().toString());
                place.setText(snapshot.child("place").getValue().toString());
                bio.setText(snapshot.child("bio").getValue().toString());

                DateFormat dateFormat = new SimpleDateFormat("yyyy MM dd", Locale.ENGLISH);
                Date date;
                try {
                    date = dateFormat.parse(snapshot.child("dob").getValue().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                    date = new Date(2001, 1, 1);
                }
                DateFormat printFormat = new SimpleDateFormat("dd/MM/yyyy");
                birthday.setText(printFormat.format(date).toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }


    public void setUpRecycler() {
        DatabaseReference interestReference = FirebaseDatabase.getInstance().getReference().child("interests").child(LDAP);

        interestReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    main_interests.add(key);
                    interests_map.put(key, new ArrayList<>());
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        interests_map.get(key).add(dataSnapshot1.getValue().toString());
                    }
                }
                ExpandableListAdapter expandableListAdapter = new com.example.iitinder.profile.ExpandableInterestCustomized(getApplicationContext(), main_interests, interests_map);
                expandableListView.setAdapter(expandableListAdapter);
//                progressDialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.d("COLLAPSED", Integer.toString(groupPosition));
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d("CLICKED", Integer.toString(groupPosition) + Integer.toString(childPosition));
                return false;
            }
        });
    }
}