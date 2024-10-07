package com.example.gamecommerce.fragment.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamecommerce.R;
import com.example.gamecommerce.adapters.GenresAdapter;
import com.example.gamecommerce.adapters.PopularProductAdapter;
import com.example.gamecommerce.adapters.RecommendedProductAdapter;
import com.example.gamecommerce.models.Genre;
import com.example.gamecommerce.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    List<Product> popularProducts, recommendedProducts;
    List<Genre> genres;
    PopularProductAdapter popularProductAdapter;
    GenresAdapter genresAdapter;
    RecommendedProductAdapter recommendedProductAdapter;
    RecyclerView popularProductsView, genresView, recommendedProductsView;
    FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        db = FirebaseFirestore.getInstance();
        popularProductsView = root.findViewById(R.id.popular_list_view);
        popularProductsView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        popularProducts = new ArrayList<>();
        popularProductAdapter = new PopularProductAdapter(getActivity(), popularProducts);
        popularProductsView.setAdapter(popularProductAdapter);
        db.collection("popular_products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product product = document.toObject(Product.class);
                                popularProducts.add(product);
                                popularProductAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        genresView = root.findViewById(R.id.explore_list_view);
        genresView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        genres = new ArrayList<>();
        genresAdapter = new GenresAdapter(getActivity(), genres, R.layout.genre_card);
        genresView.setAdapter(genresAdapter);
        db.collection("genres")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Genre genre = document.toObject(Genre.class);
                                genres.add(genre);
                                genresAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        recommendedProductsView = root.findViewById(R.id.recommended_list_view);
        recommendedProductsView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recommendedProducts = new ArrayList<>();
        recommendedProductAdapter = new RecommendedProductAdapter(getActivity(), recommendedProducts, R.layout.vertical_product_card);
        recommendedProductsView.setAdapter(recommendedProductAdapter);
        db.collection("recommended_products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product product = document.toObject(Product.class);
                                recommendedProducts.add(product);
                                recommendedProductAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}