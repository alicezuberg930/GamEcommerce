package com.example.gamecommerce.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamecommerce.R;
import com.example.gamecommerce.adapters.GeneralProductAdapter;
import com.example.gamecommerce.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    TextView textView;
    RecyclerView searchListView;
    FirebaseFirestore db;
    List<Product> searchProducts;
    GeneralProductAdapter generalProductAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        db = FirebaseFirestore.getInstance();
        searchListView = findViewById(R.id.search_list_view);
        searchListView.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
        searchProducts = new ArrayList<>();
        generalProductAdapter = new GeneralProductAdapter(this, searchProducts, R.layout.vertical_product_card);
        searchListView.setAdapter(generalProductAdapter);
        Intent intent = getIntent();

        // Retrieve the query passed from MainActivity
        if (intent != null && intent.hasExtra("query")) {
            String query = intent.getStringExtra("query");
            Toast.makeText(this, query, Toast.LENGTH_LONG).show();
            db.collection("recommended_products").whereEqualTo("name", query).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful() && task.getResult() != null) {
                        for (DocumentSnapshot document: task.getResult().getDocuments()) {
                            for(int i=0; i<50;i++){
                                Product product = document.toObject(Product.class);
                                searchProducts.add(product);
                            }
                        }
                        generalProductAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }
}