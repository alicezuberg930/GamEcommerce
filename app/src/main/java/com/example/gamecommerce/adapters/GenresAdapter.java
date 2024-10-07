package com.example.gamecommerce.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamecommerce.R;
import com.example.gamecommerce.models.Genre;
import com.example.gamecommerce.views.ViewAllActivity;

import java.util.List;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.ViewHolder> {
    private final Context context;
    private final List<Genre> genres;
    private final int layout;

    public GenresAdapter(Context context, List<Genre> genres, int layout) {
        this.context = context;
        this.genres = genres;
        this.layout = layout;
    }

    @NonNull
    @Override
    public GenresAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GenresAdapter.ViewHolder holder, int i) {
        Glide.with(context).load(genres.get(i).getImgUrl()).into(holder.image);
        holder.name.setText(genres.get(i).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewAllActivity.class);
                intent.putExtra("genre", genres.get(holder.getAdapterPosition()).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.genre_image);
            name = itemView.findViewById(R.id.genre_name);
        }
    }
}
