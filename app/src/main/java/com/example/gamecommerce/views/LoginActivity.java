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
import com.example.gamecommerce.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView infoLogin2;
    Button loginBtn;
    EditText email,password;
    ImageView loginBg;
    FirebaseAuth auth;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.login_btn);
        loginBg = findViewById(R.id.login_bg);
        infoLogin2 = findViewById(R.id.info_login2);
        loginBg.setImageBitmap(Utils.decodeSampledBitmapFromResource(getResources(), R.drawable.login_bg));
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        progress = findViewById(R.id.login_progress);

        progress.setVisibility(View.GONE);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        infoLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginUser() {
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
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
        auth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                }
                progress.setVisibility(View.GONE);
            }
        });
    }
}