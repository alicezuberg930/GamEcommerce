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

public class RecommendedProductAdapter extends RecyclerView.Adapter<RecommendedProductAdapter.ViewHolder> {
    private Context context;
    private List<Product> products;
    private int layout;

    public RecommendedProductAdapter(Context context, List<Product> products, int layout) {
        this.context = context;
        this.products = products;
        this.layout = layout;
    }

    @NonNull
    @Override
    public RecommendedProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedProductAdapter.ViewHolder holder, int i) {
        Glide.with(context).load(products.get(i).getImageUrl()).into(holder.image);
        holder.name.setText(products.get(i).getName());
        holder.price.setText(Integer.toString(products.get(i).getPrice()));
        holder.discount.setText(Integer.toString(products.get(i).getDiscount()));
        holder.rating.setText(String.format(Locale.getDefault(), "%f", products.get(i).getRating()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("product", products.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
