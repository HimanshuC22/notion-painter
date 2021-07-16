package com.example.iitinder.profile;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iitinder.LDAPEntryFragment;
import com.example.iitinder.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }

    public String LDAP_ID;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(getActivity()==null) return;
        super.onCreate(savedInstanceState);
        String EMAIL = mAuth.getCurrentUser().getEmail();
        LDAP_ID = EMAIL.substring(0, EMAIL.indexOf("@"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    // Variables
    boolean loaded = false;
    TextView name, LDAPIDText, gender, birthday, mobile, bio, place;
    ImageView profilePicture;
    ImageButton interestForward;
    ArrayList<String> main_interests, imageURLs;
    ArrayList<com.example.iitinder.profile.DataModel> dataSet;
    HashMap<String, List<String>> interests_map;
    ExpandableListView expandableListView;
    NestedScrollView scrollView;
    ConstraintLayout birthdayLayout, mobileLayout, bioLayout, placeLayout;
    String IMAGE = "image";
    String SAMPLEBIO, BIO, BLANK, URL;
    ProgressBar progressBar;

    public void initialize(View view) {
        URL = "gs://notion-painter.appspot.com/photos/";
        scrollView = view.findViewById(R.id.scrollView);
        profilePicture = view.findViewById(R.id.profilePhoto);
        progressBar = view.findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.VISIBLE);
        profilePicture.setVisibility(View.INVISIBLE);
        name = view.findViewById(R.id.textName);
        gender = view.findViewById(R.id.textGender);
        LDAPIDText = view.findViewById(R.id.textLDAP);
        birthday = view.findViewById(R.id.birthdayText);
        mobile = view.findViewById(R.id.mobileText);
        birthdayLayout = view.findViewById(R.id.birthdayLayout);
        bio = view.findViewById(R.id.textBio);
        bioLayout = view.findViewById(R.id.bio);
        interestForward = view.findViewById(R.id.interestForward);
        place = view.findViewById(R.id.placeFromText);
        placeLayout = view.findViewById(R.id.placeFromLayout);
        SAMPLEBIO = "This is a Sample Bio";
        imageURLs = new ArrayList<>();
        dataSet = new ArrayList<>();
        main_interests = new ArrayList<>();
        interests_map = new HashMap<>();
        expandableListView = view.findViewById(R.id.expandableListView);
        bio.setText(SAMPLEBIO);
        BLANK = "Not Available";
        name.setText(BLANK);
        LDAPIDText.setText(BLANK);
        gender.setText(BLANK);
        birthday.setText(BLANK);
        mobile.setText(BLANK);
        place.setText(BLANK);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize(view); // findViewByID for all

        getDetails();

        setUpRecycler(view);

        URL += LDAP_ID + "/";

        interestForward.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), InterestViewActivity.class));
        });

        placeLayout.setOnClickListener(v -> {
            AtomicReference<String> STATE = new AtomicReference<>("");
            Dialog placeChangeDialog = new Dialog(getContext());
            placeChangeDialog.setCanceledOnTouchOutside(false);
            placeChangeDialog.setContentView(R.layout.place_change);
            placeChangeDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bio_dialog_alpha));
            placeChangeDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            Spinner spinner = placeChangeDialog.findViewById(R.id.spinner);
            List<String> states = new ArrayList<>(Arrays.asList(new String[]{
                    "Adaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa", "Gujrat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerela", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Puducherry", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttrakhand", "West Bengal"
            }));
            ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, states);
            stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(stringArrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    STATE.set(states.get(position));
                    Log.d("sts", STATE.toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    STATE.set(states.get(0));
                }
            });
            Button OK = placeChangeDialog.findViewById(R.id.placeDialogBtnOK);
            Button cancel = placeChangeDialog.findViewById(R.id.placeDialogBtnCancel);
            OK.setOnClickListener(v1 -> {
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("data").child(LDAP_ID);
                HashMap<String, String> map = new HashMap<>();
                map.put("place", STATE.toString());
                databaseReference1.child("place").setValue(STATE.toString());
                placeChangeDialog.dismiss();
            });
            cancel.setOnClickListener(v1 -> {
                placeChangeDialog.dismiss();
            });
            placeChangeDialog.show();
        });

        profilePicture.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), ImageChangeActivity.class);
            startActivity(intent);

        });

        bioLayout.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getContext());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.bio_dialog);
            dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bio_dialog_alpha));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            EditText bioEditText = dialog.findViewById(R.id.editTextBio);
            bioEditText.setText(BIO);
            dialog.show();
            Button OKDialog = dialog.findViewById(R.id.placeDialogBtnOK);
            Button CancelDialog = dialog.findViewById(R.id.placeDialogBtnCancel);
            OKDialog.setOnClickListener(v1 -> {
                String NEW_BIO = bioEditText.getText().toString();
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("data").child(LDAP_ID);
                databaseReference1.child("bio").setValue(NEW_BIO);
                dialog.dismiss();
            });
            CancelDialog.setOnClickListener(v1 -> {
                dialog.dismiss();
            });

        });

    }

    private void getImagesFromFirebase(RecyclerView recyclerView) {
        ArrayList<ImageData> dataSet = new ArrayList<>();
        com.example.iitinder.profile.RecyclerViewImageAdapter imageAdapter = new com.example.iitinder.profile.RecyclerViewImageAdapter(dataSet);
        DatabaseReference photosReference = FirebaseDatabase.getInstance().getReference().child("images").child(LDAP_ID);
        photosReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int number = 1;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String name = IMAGE + Integer.toString(number) + ".png";
                    String imageURL = dataSnapshot.getValue().toString();
                    StorageReference image = FirebaseStorage.getInstance().getReferenceFromUrl(imageURL);
                    image.getBytes(2 * 1024 * 1024).addOnSuccessListener(bytes -> {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        dataSet.add(new ImageData(name, imageURL, bitmap));
                    });
                    number++;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        recyclerView.setAdapter(imageAdapter);
    }


    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d("SUCCESSFUL", "SIGN_IN_SUCCESSFUL");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("ERROR", "signInAnonymously:FAILURE", e);
                    }
                });
    }


    public void getDetails() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("data").child(LDAP_ID).child("bio");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageReference.child("photos").child(LDAP_ID).child("profile.jpg");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                imageRef.getBytes(2 * 1024 * 1024)
                        .addOnSuccessListener(bytes -> {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            Glide.with(getContext()).load(bitmap).circleCrop().into(profilePicture);
                            progressBar.setVisibility(View.GONE);
                            profilePicture.setVisibility(View.VISIBLE);
                        });
                BIO = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference detailsReference = firebaseDatabase.getReference().child("data").child(LDAP_ID);

        detailsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                place.setText(snapshot.child("place").getValue().toString());
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
                name.setText(snapshot.child("name").getValue().toString());
                String G = snapshot.child("gender").getValue().toString();
                if (G.equals("m")) {
                    gender.setText("Male");
//                    profilePicture.setImageResource(R.drawable.male);
                } else {
                    gender.setText("Female");
//                    profilePicture.setImageResource(R.drawable.female);
                }
                mobile.setText("+91 " + snapshot.child("mobile").getValue().toString());
                bio.setText(BIO);
                LDAPIDText.setText(LDAP_ID + "@iitb.ac.in");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("ERROR", error.toString());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getDetails();
    }

    public void setUpRecycler(View view) {
        DatabaseReference interestReference = FirebaseDatabase.getInstance().getReference().child("interests").child(LDAP_ID);

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
                ExpandableListAdapter expandableListAdapter = new com.example.iitinder.profile.ExpandableInterestCustomized(getContext(), main_interests, interests_map);
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

    public void setUpMultipleImages(View view) {
        RecyclerView imageRecyclerView = view.findViewById(R.id.recyclerViewMain);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        imageRecyclerView.setLayoutManager(layoutManager);
        getImagesFromFirebase(imageRecyclerView);
    }

}