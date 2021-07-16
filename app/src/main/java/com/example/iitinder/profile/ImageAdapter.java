package com.example.iitinder.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.iitinder.R;

import java.util.ArrayList;

public class ImageAdapter  extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    Context context;
    private ArrayList<DataModel> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
//        TextView textView;
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
//            this.textView = itemView.findViewById(R.id.sampleText);
            this.imageView = itemView.findViewById(R.id.imageViewCard);
        }
    }

    public ImageAdapter(ArrayList<DataModel> data)
    {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_image, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        context = parent.getContext();
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //StorageReference reference = FirebaseStorage.getInstance().getReference().child("photos").child("200020059").child("profile.jpg");
//        TextView textView = holder.textView;
        ImageView imageView = holder.imageView;
        /*reference.getBytes(2*1024*1024).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bitmap);
        });*/
        imageView.setImageBitmap(dataSet.get(position).getBitmap());
//        imageView.setImageDrawable();
//        textView.setText(dataSet.get(position).getDataText());
        /*reference.getBytes(2*1024*1024).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bitmap);
        });*/
        /*Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(reference)
                .into(imageView);*/
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
