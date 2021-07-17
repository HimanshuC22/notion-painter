package com.example.iitinder.chatting;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.iitinder.LDAPEntryFragment;
import com.example.iitinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.ls.LSException;

import java.util.ArrayList;

/**
 * This fragment shows the list of all available chat contacts
 * uses a RecyclerView and {@link UsersAdapter} as its adapter
 */

public class MainChatFragment extends Fragment {


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainChatFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MainChatFragment newInstance() {
        MainChatFragment fragment = new MainChatFragment();
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
        return inflater.inflate(R.layout.fragment_main_chat, container, false);
    }

    String LDAP, EMAIL;
    DatabaseReference database;
    FirebaseAuth mAuth;
    ArrayList<User> users;
    ArrayList<Integer> times;
    RecyclerView recyclerView;
    UsersAdapter usersAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        EMAIL = mAuth.getCurrentUser().getEmail();
        LDAP = EMAIL.substring(0, EMAIL.indexOf("@"));
        database = FirebaseDatabase.getInstance().getReference();
        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(getContext(), users);
        recyclerView = view.findViewById(R.id.recyclerviewChat);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(usersAdapter);
        database.child("data").child(LDAP).child("matches").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String ldap = snapshot1.getKey();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("data").child(ldap);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            User user = new User(snapshot1.getKey());
                            user.setName(snapshot.child("name").getValue().toString());
                            user.setUid(snapshot.child("uid").getValue().toString());
                            user.setProfileImage("");
                            user.setEmail(ldap+"@iitb.ac.in");
                            users.add(user);
                            usersAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}