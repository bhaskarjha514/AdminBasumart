package com.example.adminbaasumart.Category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adminbaasumart.Adapter.ProductAdapter;
import com.example.adminbaasumart.Model.Product;
import com.example.adminbaasumart.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ImageView toolImage;
    private String title;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private List<String> productIdList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        bindID();
        fetchProduct();
    }

    private void fetchProduct() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Admins").child("product").child("category").child(title)
                .child("products");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productIdList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    productIdList.add(snapshot.getKey());
                }
                readProductbyId();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readProductbyId() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Admins").child("products");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for(String productId : productIdList){
                        if(snapshot.getKey().equals(productId)){
                            Product product = snapshot.getValue(Product.class);
                            productList.add(product);
                        }
                    }
                }
                productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                    @Override
                    public void onDeleteClick(int position) {
                        removeItem(position);
                    }
                });
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void removeItem(int position) {
        final Product product = productList.get(position);
        StorageReference ItemImageRef = FirebaseStorage.getInstance().getReferenceFromUrl(product.getImageurl());
        ItemImageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Admins").child("products")
                        .child(product.getProductId());
                databaseReference.removeValue();
                FirebaseDatabase.getInstance().getReference().child("Admins").child("product").child("category").child(title).child("products").child(product.getProductId()).removeValue();
                Toast.makeText(ProductActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void bindID() {
        toolbar = findViewById(R.id.categoryPostToolbar);
        toolImage = findViewById(R.id.categoryPostImage);
        recyclerView = findViewById(R.id.productRV);
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(productAdapter);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        final Intent intent = getIntent();
        title = intent.getStringExtra("title");
        final String icon = intent.getStringExtra("imageurl");
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
        Glide.with(getApplicationContext()).load(icon).fitCenter().centerCrop().into(toolImage);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, NewProductActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("icon", icon);
                startActivity(intent);
            }
        });
    }
}