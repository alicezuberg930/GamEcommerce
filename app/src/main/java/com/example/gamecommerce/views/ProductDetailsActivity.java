package com.example.gamecommerce.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gamecommerce.R;
import com.example.gamecommerce.models.Product;
import com.example.gamecommerce.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    ImageView image;
    TextView price, rating, description;
    Product product;
    Button addCartButton;
    FirebaseFirestore fb;
    FirebaseAuth auth;
    int quantity = 2;
    int totalPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fb = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_product_details);
        image = findViewById(R.id.detail_image);
        price = findViewById(R.id.detail_price);
        rating = findViewById(R.id.detail_rating);
        description = findViewById(R.id.detail_description);
        addCartButton = findViewById(R.id.add_cart_button);
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

        addCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                totalPrice += quantity * product.getPrice();
//                String currentDate, currentTime;
//                Calendar calendar = Calendar.getInstance();
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
//                currentDate = simpleDateFormat.format(calendar.getTime());
//                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss");
//                currentTime = simpleTimeFormat.format(calendar.getTime());
                HashMap<String, Object> cart = product.toMap();
                fb.collection("cart").document(auth.getCurrentUser().getUid()).collection("my_cart").add(cart).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(ProductDetailsActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}