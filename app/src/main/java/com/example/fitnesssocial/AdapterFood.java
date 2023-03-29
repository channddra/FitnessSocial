package com.example.fitnesssocial;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterFood extends RecyclerView.Adapter<com.example.fitnesssocial.AdapterFood.MyHolder> {
    Context context;
    List<Hit> list;

    public AdapterFood(Context context, List<Hit> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_rows, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String foodName = list.get(position).getRecipe().getLabel();
        String imageUrl = list.get(position).getRecipe().getImage();
        String foodUrl = list.get(position).getRecipe().getUrl();
        String source = list.get(position).getRecipe().getSource();

        holder.textSource.setText(source);
        holder.textSource.setTextColor(context.getResources().getColor(R.color.blue_light_uranium));
        holder.textFood.setText(foodName);
        try{
            Glide.with(context).load(imageUrl).into(holder.imageFood);
        } catch (Exception e) {
            e.printStackTrace();
        }holder.imageFood.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(foodUrl));
            context.startActivity(browserIntent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageFood;
        TextView textFood, textSource;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageFood = itemView.findViewById(R.id.imageFood);
            textFood = itemView.findViewById(R.id.textFood);
            textSource = itemView.findViewById(R.id.textSource);
        }
    }
}
