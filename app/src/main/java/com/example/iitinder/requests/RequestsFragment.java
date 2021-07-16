package com.example.iitinder.requests;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.iitinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RequestsFragment extends Fragment {


    public RequestsFragment() {
        // Required empty public constructor
    }

    public static RequestsFragment newInstance() {
        RequestsFragment fragment = new RequestsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(getActivity()==null) return;
        super.onCreate(savedInstanceState);
    }

    RecyclerView recyclerView;
    String EMAIL, LDAP;
    ArrayList<String> to_match;
    ConstraintLayout empty;
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        EMAIL = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        LDAP = EMAIL.substring(0, EMAIL.indexOf("@"));

        setUpUI(view);

        getInterestMatchList();


    }

    public void setUpUI(View view)
    {
        to_match = new ArrayList<>();
        recyclerView = view.findViewById(R.id.requests_recycler_view);
        empty = view.findViewById(R.id.emptyView);
    }

    public void getInterestMatchList()
    {
        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("data").child(LDAP).child("interest_matches");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        String L = dataSnapshot.getKey();
                        if(dataSnapshot.getValue().toString().equals("match")) to_match.add(L);
                    }
                    setUpRecyclerView();
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        } catch (Exception e)
        {
            recyclerView.setAdapter(new RequestAdapter(getContext(), new ArrayList<>()));
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG);
            Log.d("E/REQFRAG", e.toString());
        }
    }

    public void setUpRecyclerView()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setOnFlingListener(null);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(new RequestAdapter(getContext(), to_match));
        if(recyclerView.getAdapter().getItemCount()==0) empty.setVisibility(View.VISIBLE);
        else empty.setVisibility(View.GONE);
    }

}