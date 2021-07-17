package com.example.iitinder.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.iitinder.InterestPickerActivity;
import com.example.iitinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileInterestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileInterestFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileInterestFragment() {
        // Required empty public constructor
    }

    public static ProfileInterestFragment newInstance() {
        ProfileInterestFragment fragment = new ProfileInterestFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_interest, container, false);
    }

    ExpandableListView expandableListView;
    ArrayList<String> main_interests;
    HashMap<String, List<String>> interests_map;
    Button interestChangeButton;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expandableListView = view.findViewById(R.id.expandableListviewBroad);
        interestChangeButton = view.findViewById(R.id.buttonInterestChange);
        main_interests = new ArrayList<>();
        interests_map = new HashMap<>();
        String EMAIL = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String ldap = EMAIL.substring(0, EMAIL.indexOf("@"));
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference interestReference = firebaseDatabase.getReference().child("interests").child(ldap);

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
//                        interests_map.get(key).add(dataSnapshot1.getValue().toString());
                        interests_map.get(key).add(dataSnapshot1.getValue().toString());
//                        finalStr += dataSnapshot1.getValue().toString() + " ";
                    }
                }
//                TextView textView = view.findViewById(R.id.textView8);
//                textView.setText(finalStr);

                ExpandableListAdapter expandableListAdapter = new com.example.iitinder.profile.ExpandableInterestCustomized(getContext(), main_interests, interests_map);
                expandableListView.setAdapter(expandableListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        ArrayList<ImageView> imageViews = new ArrayList<>();


        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.d("COLLAPSED", Integer.toString(groupPosition));
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d("CLICKED", Integer.toString(groupPosition)+Integer.toString(childPosition));
                return false;
            }
        });

        interestChangeButton.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogInterest = new AlertDialog.Builder(getContext());
            alertDialogInterest.setMessage("Do you want to change your selected interests?")
                    .setTitle("Confirmation")
                    .setPositiveButton("YES", (dialog, which) -> {
                        // Maybe move to activity
                        Intent intent = new Intent(getActivity(), InterestPickerActivity.class);
                        intent.putExtra("state", "second");
                        startActivity(intent);
                    })
                    .setNegativeButton("NO", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setCancelable(true)
                    .show();
        });

    }
}