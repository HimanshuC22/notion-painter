package com.example.iitinder.chatting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iitinder.R;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * The adapter for showing message in a chat
 */

public class MessagesAdapter extends RecyclerView.Adapter{

    Context context;
    ArrayList<Message> messages;
    final int ITEM_SENT=1;
    final int ITEM_RECEIVE=2;

    String senderRoom,senderldap;
    String receiverRoom,receiverldap;

    public MessagesAdapter(Context context, ArrayList<Message> messages, String senderRoom, String receiverRoom){
        this.context=context;
        this.messages=messages;
        this.senderRoom=senderRoom;
        this.receiverRoom=receiverRoom;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_SENT){
            View view = LayoutInflater.from(context).inflate(R.layout.message_sent,parent,false);
            return new SentViewHolder(view);
            //  return new ReceiverViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.message_rece,parent,false);
            return new ReceiverViewHolder(view);
            //return new SentViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (FirebaseAuth.getInstance().getUid().equals(message.getSenderId())){
            return ITEM_SENT;
        }else{
            return ITEM_RECEIVE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder.getClass()==SentViewHolder.class){
            SentViewHolder viewHolder=(SentViewHolder) holder;
            ((SentViewHolder) holder).textView.setText(message.getMessage());
        }else{
            ReceiverViewHolder viewHolder=(ReceiverViewHolder) holder;
            ((ReceiverViewHolder) holder).text.setText(message.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.message);
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        LinearLayout linearLayout;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.message);
            linearLayout = itemView.findViewById(R.id.linearLayout2);
        }
    }

}
