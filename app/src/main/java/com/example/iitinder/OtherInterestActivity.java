package com.example.iitinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OtherInterestActivity extends AppCompatActivity {

    String LDAP;
    ImageView back;
    ExpandableListView expandableListView;
    ArrayList<String> main_interests;
    HashMap<String, List<String>> interests_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_interest);

        LDAP = getIntent().getExtras().getString("ldap_interest");

        setUp();

        back.setOnClickListener(v -> {finish();});

        setUpRecycler();
    }

    public void setUp()
    {
        expandableListView = findViewById(R.id.expandableListviewBroadOther);
        main_interests = new ArrayList<>();
        interests_map = new HashMap<>();
        back = findViewById(R.id.backFromInterestView);
    }

    public void setUpRecycler()
    {
        DatabaseReference interestReference = FirebaseDatabase.getInstance().getReference().child("interests").child(LDAP);
        interestReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String key = dataSnapshot.getKey();
                    main_interests.add(key);
                    interests_map.put(key, new ArrayList<>());
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        interests_map.get(key).add(dataSnapshot1.getValue().toString());
                    }
                }
                ExpandableListAdapter expandableListAdapter = new com.example.iitinder.profile.ExpandableInterestCustomized(getApplicationContext(), main_interests, interests_map);
                expandableListView.setAdapter(expandableListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}