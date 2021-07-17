package com.example.iitinder.chatting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iitinder.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * This is the adapter for showing contacts available for chat
 * It uses a {@link User} class to store and get user details to firebase
 */

public class UsersAdapter extends  RecyclerView.Adapter<UsersAdapter.UsersViewHolder>{

    Context context;
    ArrayList<User> users;
    ArrayList<String> ldaps;

    public UsersAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }
    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_conversation, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
//        Log.d("LDAp", ldaps.get(position));
        User user = users.get(position);
        holder.userName.setText(user.getName());
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("photos").child(user.getLdapid()).child("profile.jpg");

        reference.getBytes(9*1024*1024).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Glide.with(context).load(bitmap).circleCrop().into(holder.imageView);
            holder.progressBar.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("uid",user.getUid());
                intent.putExtra("ldap", user.getLdapid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView userName;
        ProgressBar progressBar;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profile_list);
            userName = itemView.findViewById(R.id.username);
            progressBar = itemView.findViewById(R.id.progressChat);
        }
    }

}
