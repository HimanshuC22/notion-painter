package com.example.iitinder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * Adapter for the matches list
 */

public class MatchRecyclerAdapter extends RecyclerView.Adapter<MatchRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> data;

    public MatchRecyclerAdapter(Context context, ArrayList<String> data)
    {
        this.context = context;
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        // Declare views
        TextView name, email;
        ImageView imageView;
        ConstraintLayout layout;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            // define views (findViewById)
            name = itemView.findViewById(R.id.friend_name);
            email = itemView.findViewById(R.id.friend_email);
            imageView = itemView.findViewById(R.id.friend_profile_image);
            layout = itemView.findViewById(R.id.friend_layout);
            progressBar = itemView.findViewById(R.id.progressBar2);
        }
    }

    @Override
    public MatchRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchRecyclerAdapter.ViewHolder holder, int position) {
        holder.progressBar.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                holder.name.setText(snapshot.child(data.get(position)).child("name").getValue().toString());
                holder.email.setText(data.get(position) + "@iitb.ac.in");
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("photos").child(data.get(position)).child("profile.jpg");
        storageReference.getBytes(2*1024*1024).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Glide.with(context).load(bitmap).circleCrop().into(holder.imageView);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.GONE);
        });

        holder.layout.setOnClickListener(v -> {
            Intent intent = new Intent(context, OtherProfileActivity.class);
            intent.putExtra("ldap", data.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
