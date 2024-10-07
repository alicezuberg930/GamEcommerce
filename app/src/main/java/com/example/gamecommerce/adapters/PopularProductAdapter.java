package com.example.gamecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamecommerce.R;
import com.example.gamecommerce.models.Product;

import java.util.List;
import java.util.Locale;

public class PopularProductAdapter extends RecyclerView.Adapter<PopularProductAdapter.ViewHolder> {
    private Context context;
    private List<Product> popularProducts;

    public PopularProductAdapter(Context context, List<Product> popularProducts) {
        this.context = context;
        this.popularProducts = popularProducts;
    }

    @NonNull
    @Override
    public PopularProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_product_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopularProductAdapter.ViewHolder holder, int i) {
        Glide.with(context).load(popularProducts.get(i).getImageUrl()).into(holder.image);
        holder.name.setText(popularProducts.get(i).getName());
        holder.price.setText(Integer.toString(popularProducts.get(i).getPrice()));
        holder.discount.setText(Integer.toString(popularProducts.get(i).getDiscount()));
        holder.rating.setText(String.format(Locale.getDefault(), "%f", popularProducts.get(i).getRating()));
    }

    @Override
    public int getItemCount() {
        return popularProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, price, discount, rating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.popular_image);
            name = itemView.findViewById(R.id.popular_name);
            price = itemView.findViewById(R.id.popular_price);
            discount = itemView.findViewById(R.id.popular_discount);
            rating = itemView.findViewById(R.id.popular_rating);
        }
    }
}
