package com.example.gamecommerce.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamecommerce.R;
import com.example.gamecommerce.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore db;
    TextView ooga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        ooga = findViewById(R.id.ooga);
        List<Product> cartItems = (ArrayList<Product>) getIntent().getSerializableExtra("cart_list");
        if(cartItems != null) {
            HashMap<String, Object> orderDetails = new HashMap<>();
            List<HashMap<String, Object>> cartItemMaps = new ArrayList<>();
            for (Product cartItem: cartItems) {
                cartItemMaps.add(cartItem.toMap());
            }
            LocalDateTime now = LocalDateTime.now();
            // Define the date and time format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            // Format the current date and time
            String orderID = auth.getCurrentUser().getUid().toUpperCase(Locale.ROOT) + String.valueOf(System.currentTimeMillis());
            orderDetails.put("orderID", orderID);
            orderDetails.put("orderItems", cartItemMaps);
            orderDetails.put("createDate", now.format(formatter));
            db.collection("orders").document(auth.getCurrentUser().getUid()).collection("my_orders").add(orderDetails).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful()) {

                    } else {

                    }
                    Toast.makeText(CheckoutActivity.this, "Order placed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}