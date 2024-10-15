package com.example.gamecommerce.fragment.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.gamecommerce.R;
import com.example.gamecommerce.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileFragment extends Fragment {

    ImageView image;
    TextView profileName, profileEmail, profilePhone, profileAddress;
    Button profileUpdateButton;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase db;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile,container,false);
        image = root.findViewById(R.id.profile_image);
        profileName = root.findViewById(R.id.profile_name);
        profileEmail = root.findViewById(R.id.profile_email);
        profilePhone = root.findViewById(R.id.profile_phone);
        profileAddress = root.findViewById(R.id.profile_address);
        profileUpdateButton = root.findViewById(R.id.profile_update_button);
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            if(result.getData() != null) {
                                Uri profileUri = result.getData().getData();
                                if(profileUri != null) {
                                    image.setImageURI(profileUri);
                                    final StorageReference reference = storage.getReference().child("profile_picture").child(auth.getCurrentUser().getUid());
                                    reference.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(@NonNull Uri uri) {
                                                    db.getReference().child("users").child(auth.getCurrentUser().getUid()).child("profileImage").setValue(uri.toString());
                                                    Toast.makeText(getContext(), "Đã cập nhật ảnh", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
        );
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                activityResultLauncher.launch(i);
            }
        });
        profileUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        db.getReference().child("users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(getContext()).load(user.getProfileImage()).into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}