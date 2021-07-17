package com.example.iitinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.airbnb.lottie.L;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * This is the activity where the fragment which holds the matches' cards is placed
 */
public class MatchesActivity extends AppCompatActivity {

    // Variables
    private String EMAIL, LDAP;
    ImageView back;
    private RecyclerView recyclerView;
    private ArrayList<String> matchesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        EMAIL = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        LDAP = EMAIL.substring(0, EMAIL.indexOf("@"));

        initUI();

        back.setOnClickListener(v -> {finish();});

        fetchFriendsData();

    }

    public void initUI()
    {
        recyclerView = findViewById(R.id.recyclerMatches);
        matchesList = new ArrayList<>();
        back = findViewById(R.id.backFromMatchesView);
    }

    public void setUpRecyclerView()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new MatchRecyclerAdapter(this, matchesList));
    }

    public void fetchFriendsData()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("data").child(LDAP).child("matches");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    if(snapshot.child(dataSnapshot.getKey()).getValue()!=null && snapshot.child(dataSnapshot.getKey()).getValue().equals("friend"))
                    {
                        matchesList.add(dataSnapshot.getKey());
                    }
                }
                setUpRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }


}