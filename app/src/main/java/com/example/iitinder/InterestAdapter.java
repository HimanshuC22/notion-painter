package com.example.iitinder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InterestAdapter extends RecyclerView.Adapter<InterestViewHolder> {

    private Context mContext;
    private List<InterestData> mInterestList;

    InterestAdapter(Context mContext, List<InterestData> mInterestList) {
        this.mContext = mContext;
        this.mInterestList = mInterestList;
    }

    @Override
    public InterestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_row, parent, false);
        return new InterestViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final InterestViewHolder holder, int position) {
        holder.mImage.setImageResource(R.drawable.movies);
        holder.mTextView.setText(mInterestList.get(position).getInterestName());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InterestPickerActivity.class);
                intent.putExtra("POSITION", position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mInterestList.size();
    }
}

class InterestViewHolder extends RecyclerView.ViewHolder {
    ImageView mImage;
    TextView mTextView;
    CardView mCardView;

    InterestViewHolder(View itemView) {
        super(itemView);
        mImage = itemView.findViewById(R.id.mainImage);
        mTextView = itemView.findViewById(R.id.mainText);
        mCardView = itemView.findViewById(R.id.mainCardView);
    }
}
