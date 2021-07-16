package com.example.iitinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LDAPEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LDAPEntryFragment extends Fragment {

    public LDAPEntryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LDAPEntryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LDAPEntryFragment newInstance() {
        LDAPEntryFragment fragment = new LDAPEntryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ldap_entry, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btnLDAPEntryNext).setOnClickListener(v -> {
            if (isOnline()) {
                String email = ((EditText) view.findViewById(R.id.editTextTextEmailAddress)).getText().toString();

                // check if the email ends in iitb.ac.in
                if (email.substring(email.lastIndexOf('@') + 1).equals("iitb.ac.in")) {
                    String ldap = email.substring(0, email.indexOf("@"));

                    //connect to the database and set the data URL
                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://notion-painter-default-rtdb.firebaseio.com/");
                    database.getReference().child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(email.substring(0, email.indexOf("@")))) {
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Already Registered")
                                        .setMessage("Please sign in instead")
                                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                            dialog.dismiss();
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            } else {
                                getParentFragmentManager().beginTransaction()
                                        .setReorderingAllowed(true)
                                        .replace(R.id.fragment_container_view, PasswordEntryFragment.newInstance(email, ""))
                                        .addToBackStack(null)
                                        .commit();
                                /*database.getReference().child("data").child(email.substring(0, email.indexOf("@"))).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        DatabaseReference myRef = database.getReference("/users/" + email.substring(0, email.lastIndexOf('@')));
                                        myRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                //if the database contains the LDAP id, value will be name of user
                                                String value = dataSnapshot.getValue(String.class);
                                                if (value != null) {
                                                    Log.d("FIREBASE", "Value is " + value);
                                                    getParentFragmentManager().beginTransaction()
                                                            .setReorderingAllowed(true)
                                                            .replace(R.id.fragment_container_view, PasswordEntryFragment.newInstance(email, value))
                                                            .addToBackStack(null)
                                                            .commit();
                                                } else {
                                                    new AlertDialog.Builder(getContext())
                                                            .setTitle("Invalid LDAP")
                                                            .setMessage("Please enter a valid LDAP ID")
                                                            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                                                dialog.dismiss();
                                                            })
                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                            .show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Log.w("FIREBASE", "Failed to read value", databaseError.toException());
                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {

                                    }
                                });*/
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });

                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Invalid LDAP")
                            .setMessage("Please enter a valid LDAP ID")
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle("No Internet!")
                        .setMessage("Please connect to the internet to proceed")
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    /**
     * Checks if the device is online
     * Copied from https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     *
     * @return true if the device is online
     */
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}