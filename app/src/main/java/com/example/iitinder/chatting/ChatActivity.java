package com.example.iitinder.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.iitinder.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

/**
 *  This is the activity where two users chat
 */

public class ChatActivity extends AppCompatActivity {

    MessagesAdapter adapter;
    ArrayList<Message> messages;
    FirebaseAuth mAuth;
    String senderRoom, receiverRoom;
    FirebaseDatabase database;
    RecyclerView recyclerView;
    TextView name;
    ImageView sendBtn, backbtn, profilePic;
    EditText messageBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        messages = new ArrayList<>();
        adapter = new MessagesAdapter(this, messages, senderRoom, receiverRoom);

        recyclerView = findViewById(R.id.recyclerViewChatActivity);
        sendBtn = findViewById(R.id.sendBtn);
        messageBox = findViewById(R.id.messageBox);
        backbtn = findViewById(R.id.backbtn);
        name = findViewById(R.id.name_chat_person);
        profilePic = findViewById(R.id.profilePicActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();


        String uName = getIntent().getStringExtra("name");
        String uid = getIntent().getStringExtra("uid");
        String LDAP_USER = getIntent().getStringExtra("ldap");
        String receiveruid = getIntent().getStringExtra("uid");
        String senderUid = FirebaseAuth.getInstance().getUid();

        getprofilePic(LDAP_USER);

        senderRoom = senderUid + receiveruid;
        receiverRoom = receiveruid + senderUid;

        database = FirebaseDatabase.getInstance();

        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Message message = snapshot1.getValue(Message.class);
                            message.setMessageId(snapshot1.getKey());
                            messages.add(message);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageTxt = messageBox.getText().toString();
                Date date = new Date();
                Message message = new Message(messageTxt, senderUid, date.getTime());
                messageBox.setText("");
                String randomkey = database.getReference().push().getKey();

                database.getReference().child("chats").child(senderRoom).child("messages").child(randomkey).setValue(message)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                                database.getReference().child("chats").child(receiverRoom).child("messages").child(randomkey).setValue(message)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        });


                            }
                        });
            }
        });

        name.setText(uName);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getprofilePic(String LDAP_U)
    {
        ProgressBar progressBar = findViewById(R.id.imageProgress1);
        progressBar.setVisibility(View.VISIBLE);
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("photos").child(LDAP_U).child("profile.jpg");
        reference.getBytes(9*1024*1024).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Glide.with(this).load(bitmap).circleCrop().into(profilePic);
            progressBar.setVisibility(View.GONE);
            profilePic.setVisibility(View.VISIBLE);
        });
    }
}