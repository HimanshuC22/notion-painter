package com.example.iitinder.chatting;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * The class defines a user in chat
 */

public class User {

    private String uid, name, email, profileImage,ldapid;

    public User(String ldap) {
        final String[] uid = new String[1];
        final String[] name = new String[1];
        final String[] email = new String[1];
        final String[] profileImage = new String[1];
        this.ldapid = ldap;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("data").child(ldap);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                uid[0] = snapshot.child("uid").getValue().toString();
                name[0] = snapshot.child("name").getValue().toString();
                email[0] = ldap+"@iitb.ac.in";
                profileImage[0] = "";
            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        });
        this.uid = uid[0];
        this.name = name[0];
        this.email = email[0];
        this.profileImage = profileImage[0];
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getLdapid() {
        return ldapid;
    }

    public void setLdapid(String ldapid) {
        this.ldapid = ldapid;
    }

}

