package com.example.gamecommerce.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gamecommerce.R;
import com.example.gamecommerce.models.Product;
import com.example.gamecommerce.utils.Utils;

public class ProductDetailsActivity extends AppCompatActivity {

    ImageView image;
    TextView price, rating, description;
    Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        image = findViewById(R.id.detail_image);
        price = findViewById(R.id.detail_price);
        rating = findViewById(R.id.detail_rating);
        description = findViewById(R.id.detail_description);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        ViewGroup.LayoutParams layoutParams = image.getLayoutParams();
        layoutParams.height = (screenHeight / 2);  // Set calculated height
        image.setLayoutParams(layoutParams);

        final Object obj = getIntent().getSerializableExtra("product");
        if(obj instanceof Product) {
            product = (Product) obj;
        }
        Drawable drawable = AppCompatResources.getDrawable(this, R.drawable.ic_loading);
        Glide.with(this).load(product.getImageUrl()).into(image).onLoadStarted(drawable);
        price.setText(Utils.formatVND(product.getPrice()));
        rating.setText(Utils.formatRating(product.getRating()));
        description.setText(product.getDescription());
    }
}