package com.example.gamecommerce.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamecommerce.R;
import com.example.gamecommerce.adapters.CartAdapter;
import com.example.gamecommerce.models.Product;
import com.example.gamecommerce.utils.DeleteButtonClickListener;
import com.example.gamecommerce.utils.SwipeHelper;
import com.example.gamecommerce.utils.Utils;
import com.example.gamecommerce.views.CheckoutActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseFirestore db;
    RecyclerView cartListView;
    CartAdapter cartAdapter;
    List<Product> products;
    TextView cartTotalPrice;
    Button checkout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        checkout = root.findViewById(R.id.checkout);
        cartListView = root.findViewById(R.id.cart_list_view);
        cartTotalPrice = root.findViewById(R.id.cart_total_price);
        cartListView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        products = new ArrayList<>();
        cartAdapter = new CartAdapter(getContext(), products);
        cartListView.setAdapter(cartAdapter);
        SwipeHelper swipeHelper = new SwipeHelper(getContext(), cartListView, 150) {
            @Override
            public void instantiateDeleteButton(RecyclerView.ViewHolder viewHolder, List<SwipeHelper.DeleteButton> buffer) {
                buffer.add(new DeleteButton(getContext(), R.drawable.ic_delete, Color.parseColor("#EDEDED"), new DeleteButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        db.collection("cart").document(auth.getCurrentUser().getUid()).collection("my_cart").document(products.get(pos).getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Snackbar.make(root.findViewById(R.id.snack_bar), "Đã xóa " + products.get(pos).getName(), Snackbar.LENGTH_LONG).show();
                                    products.remove(pos);
                                    cartAdapter.notifyItemChanged(pos);
                                } else {
                                    Snackbar.make(root.findViewById(R.id.snack_bar), "Đã có lỗi khi xóa sản phẩm", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }));
            }
        };
        db.collection("cart").document(auth.getCurrentUser().getUid()).collection("my_cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    if(!task.getResult().getDocuments().isEmpty()) {
                        int total = 0;
                        for(DocumentSnapshot snapshot: task.getResult().getDocuments()) {
                            Product product = snapshot.toObject(Product.class);
                            product.setId(snapshot.getId());
                            products.add(product);
                            total += product.getPrice();
                            cartAdapter.notifyDataSetChanged();
                            cartTotalPrice.setText(Utils.formatVND(total));
                        }
                    } else {
                        cartTotalPrice.setText("0");
                    }
                }
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CheckoutActivity.class);
                i.putExtra("cart_list", (Serializable) products);
                startActivity(i);
            }
        });
        return root;
    }
}