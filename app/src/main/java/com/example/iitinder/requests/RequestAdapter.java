package com.example.iitinder.requests;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iitinder.OtherProfileActivity;
import com.example.iitinder.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.ArrayList;


public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    String EMAIL = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    String myLDAP = EMAIL.substring(0, EMAIL.indexOf("@"));
    private Context context;
    private ArrayList<String> data;

    public RequestAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage, accept, reject, chat, see;
        TextView name, commonInterests, result;
        ProgressBar progressBar;
        CardView request;

        public ViewHolder(View itemView) {
            super(itemView);
            accept = itemView.findViewById(R.id.btn_req_accept);
            profileImage = itemView.findViewById(R.id.req_profilePic);
            name = itemView.findViewById(R.id.req_name);
            progressBar = itemView.findViewById(R.id.profile_progress);
            request = itemView.findViewById(R.id.request_background);
            reject = itemView.findViewById(R.id.btn_req_reject);
            chat = itemView.findViewById(R.id.btn_req_chat);
            result = itemView.findViewById(R.id.result);
            see = itemView.findViewById(R.id.viewProfile);
            commonInterests = itemView.findViewById(R.id.req_interests);
        }
    }

    public RequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestAdapter.ViewHolder holder, int position) {

        String ldap = data.get(position);

        StorageReference reference = FirebaseStorage.getInstance().getReference().child("photos").child(ldap).child("profile.jpg");
        reference.getBytes(9 * 1024 * 1024).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.profileImage.setImageBitmap(bitmap);
            holder.progressBar.setVisibility(View.GONE);
            holder.profileImage.setVisibility(View.VISIBLE);
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("data").child(ldap);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                holder.name.setText(snapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();

        holder.accept.setOnClickListener(v -> {
            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
            holder.result.setText("Accepted");
            holder.result.setVisibility(View.VISIBLE);
            reference1.child("data").child(myLDAP).child("interest_matches").child(data.get(position)).setValue("matched");
            reference1.child("data").child(myLDAP).child("requests").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.child(data.get(position)).getValue() != null) {
                        reference1.child("data").child(myLDAP).child("requests").child(data.get(position)).setValue("accepted");
                        reference1.child("data").child(data.get(position)).child("matches").child(myLDAP).setValue("friend");
                        reference1.child("data").child(myLDAP).child("matches").child(data.get(position)).setValue("friend");
                        Toast.makeText(context, "Matched from both users!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        reference1.child("data").child(data.get(position)).child("requests").child(myLDAP).setValue("requested");
                    }
                    Toast.makeText(context, "Accepted Match and Requested", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });

        });

        holder.see.setOnClickListener(v -> {
            Intent intent = new Intent(context, OtherProfileActivity.class);
            intent.putExtra("ldap", data.get(position));
            context.startActivity(intent);
        });
        ArrayList<String> common = getCommonInterests(myLDAP, data.get(position));
        String s = "";
        for (String str : common) s += str + " ";
        holder.commonInterests.setText(holder.commonInterests.getText()+" "+s);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public ArrayList<String> getCommonInterests(String LDAP1, String LDAP2)
    {
        ArrayList<String> common = new ArrayList<>();

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("interests").child(LDAP1);
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("interests").child(LDAP2);
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    reference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot22) {
                            for(DataSnapshot snapshot2 : snapshot22.getChildren())
                            {
                                if(snapshot1.getKey() == snapshot2.getKey())
                                    common.add(snapshot1.getKey());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        return common;
    }
}
