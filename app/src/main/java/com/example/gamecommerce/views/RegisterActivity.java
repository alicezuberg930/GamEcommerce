package com.example.gamecommerce.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamecommerce.R;
import com.example.gamecommerce.models.User;
import com.example.gamecommerce.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    TextView infoReg2;
    Button registerBtn;
    EditText name,email,password;
    ImageView registerBg;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://grocery-store-df271-default-rtdb.asia-southeast1.firebasedatabase.app");
        registerBg = findViewById(R.id.register_bg);
        registerBtn = findViewById(R.id.register_btn);
        infoReg2 = findViewById(R.id.info_reg2);
        name = findViewById(R.id.register_name);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        registerBg.setImageBitmap(Utils.decodeSampledBitmapFromResource(getResources(), R.drawable.register_bg));
        progress = findViewById(R.id.register_progress);

        progress.setVisibility(View.GONE);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        infoReg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }

    private void createUser() {
        String username = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        if(username.isEmpty()) {
            Toast.makeText(this, "Hãy nhập tên người dùng", Toast.LENGTH_SHORT).show();
            return;
        }
        if(userEmail.isEmpty()) {
            Toast.makeText(this, "Hãy nhập email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(userPassword.isEmpty()) {
            Toast.makeText(this, "Hãy nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        if(userPassword.length() < 7) {
            Toast.makeText(this, "Mật khẩu phải dài hơn 7 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        progress.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    User user = new User(username,userEmail,userPassword);
                    String id = task.getResult().getUser().getUid();
                    database.getReference("users").child(id).setValue(user);
                    Toast.makeText(RegisterActivity.this, id, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                }
                progress.setVisibility(View.GONE);
            }
        });
    }
}