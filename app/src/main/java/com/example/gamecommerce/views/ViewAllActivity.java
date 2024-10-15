package com.example.gamecommerce.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.gamecommerce.R;
import com.example.gamecommerce.adapters.GeneralProductAdapter;
import com.example.gamecommerce.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    GeneralProductAdapter adapter;
    RecyclerView allProductsView;
    FirebaseFirestore db;
    List<Product> products;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        progressBar = findViewById(R.id.loading_bar);
        db = FirebaseFirestore.getInstance();
        allProductsView = findViewById(R.id.all_product_list);
        allProductsView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        products = new ArrayList<>();
        adapter = new GeneralProductAdapter(this, products, R.layout.horizontal_product_card);
        allProductsView.setAdapter(adapter);
        String genre = getIntent().getStringExtra("genre");
        db.collection("recommended_products").whereEqualTo("genre", genre)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product product = document.toObject(Product.class);
                                products.add(product);
                                adapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}