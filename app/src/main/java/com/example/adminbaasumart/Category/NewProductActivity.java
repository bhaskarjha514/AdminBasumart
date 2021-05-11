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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class NewProductActivity extends AppCompatActivity {
    private EditText titleEt, descEt, priceEt, discountEt, ratingEt;
    private ImageView imageView;
    private MaterialButton materialButton;
    private static final int GalleryPick = 1;
    private Uri uri;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
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
                if(titleEt.getText().toString().isEmpty() || descEt.getText().toString().isEmpty() || priceEt.getText().toString().isEmpty()
                || discountEt.getText().toString().isEmpty()|| ratingEt.getText().toString().isEmpty()){
                    Toast.makeText(NewProductActivity.this, "Enter All fields", Toast.LENGTH_SHORT).show();
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
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("admin").child("product")
                    .child("category").child(title).child("Item").child(UUID.randomUUID().toString() + ".png");
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    loadingBar.dismiss();
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Admins")
                                    .child("products");
                            String productId = reference.push().getKey();
                            DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
                            Date date = new Date();
                            String strDate = dateFormat.format(date).toString();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("productId", productId);
                            hashMap.put("category", title);
                            hashMap.put("Imageurl", String.valueOf(uri));
                            hashMap.put("datetime", strDate);
                            hashMap.put("productName",titleEt.getText().toString());
                            hashMap.put("desc",descEt.getText().toString());
                            hashMap.put("price",priceEt.getText().toString());
                            hashMap.put("discount",discountEt.getText().toString());
                            hashMap.put("rating",ratingEt.getText().toString());

                            reference.child(productId).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseDatabase.getInstance().getReference("Admins").child("product")
                                            .child("category").child(title).child("products").child(productId).setValue(true);
                                    Toast.makeText(NewProductActivity.this, "uploaded ", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            });

                        }
                    });
                }
            });
        }
    }

    private void chooseImage() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
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

    private void bindID() {
        titleEt = findViewById(R.id.select_title);
        descEt = findViewById(R.id.select_desc);
        priceEt = findViewById(R.id.select_price);
        discountEt = findViewById(R.id.select_discount);
        ratingEt = findViewById(R.id.select_rating);
        imageView = findViewById(R.id.imageUpload);
        materialButton = findViewById(R.id.uploadProductBtn);
        final Intent intent = getIntent();
        title = intent.getStringExtra("title");
    }
}