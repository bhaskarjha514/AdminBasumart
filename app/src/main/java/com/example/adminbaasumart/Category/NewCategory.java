package com.example.adminbaasumart.Category;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adminbaasumart.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class NewCategory extends AppCompatActivity {
    private static final int GalleryPick = 1;
    private Uri uri;
    private ImageView imageView;
    private MaterialButton materialButton;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        bindID();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().isEmpty()|| uri.equals(null)){
                    Toast.makeText(NewCategory.this, "Enter title and choose a image", Toast.LENGTH_SHORT).show();
                }else{
                    uploadData();
                }
            }
        });
    }

    private void uploadData() {
        if(uri!=null){
            final ProgressDialog loadingBar = new ProgressDialog(this);
            loadingBar.setTitle("Uploading..");
            loadingBar.show();
            final String categoryName = editText.getText().toString();
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("admin").child("product")
                    .child("category").child(categoryName).child("main").child(categoryName + ".png");
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    loadingBar.dismiss();
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference mrootRef = FirebaseDatabase.getInstance().getReference().child("Admins").child("product").child("category").child(categoryName);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("icon", String.valueOf(uri));
                            hashMap.put("title", categoryName);
                            mrootRef.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(NewCategory.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    int currentprogress = (int) progress;
                    loadingBar.setMessage("uploaded " + currentprogress + " %");
                }
            });
        }else{
            Toast.makeText(this, "Choose image first", Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseImage() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    private void bindID() {
        imageView = findViewById(R.id.select_icon);
        materialButton = findViewById(R.id.uploadCatBtn);
        editText = findViewById(R.id.select_title);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}