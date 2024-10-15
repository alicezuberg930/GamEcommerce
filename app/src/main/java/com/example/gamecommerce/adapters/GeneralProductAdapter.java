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
import com.example.gamecommerce.models.Product;
import com.example.gamecommerce.views.ProductDetailsActivity;

import java.util.List;
import java.util.Locale;

public class GeneralProductAdapter extends RecyclerView.Adapter<GeneralProductAdapter.ViewHolder> {
    private final Context context;
    private final List<Product> products;
    private final int layout;

    public GeneralProductAdapter(Context context, List<Product> products, int layout) {
        this.context = context;
        this.products = products;
        this.layout = layout;
    }

    @NonNull
    @Override
    public GeneralProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralProductAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(products.get(position).getImageUrl()).into(holder.image);
        holder.name.setText(products.get(position).getName());
        holder.price.setText(Integer.toString(products.get(position).getPrice()));
        holder.discount.setText(Integer.toString(products.get(position).getDiscount()));
        holder.rating.setText(String.format(Locale.getDefault(), "%f", products.get(position).getRating()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("product", products.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, price, discount, rating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.product_image);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            discount = itemView.findViewById(R.id.product_discount);
            rating = itemView.findViewById(R.id.product_rating);
        }
    }
}
