package com.example.adminbaasumart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.adminbaasumart.Adapter.CategoryAdapter;
import com.example.adminbaasumart.Category.NewCategory;
import com.example.adminbaasumart.Category.ProductActivity;
import com.example.adminbaasumart.Model.Categories;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LinearLayout categoryLL;
    private CategoryAdapter categoryAdapter;
    private RecyclerView catRv;
    private Categories categoriesClicked;
    private List<Categories> categoriesList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindID();
    }

    private void bindID() {
        categoryLL = findViewById(R.id.categoryLL);
        catRv = findViewById(R.id.categoryRV);
        categoryLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewCategory.class));
            }
        });
        catRv.setHasFixedSize(true);
        categoryAdapter = new CategoryAdapter(getApplicationContext(), categoriesList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        catRv.setLayoutManager(gridLayoutManager);
        catRv.setAdapter(categoryAdapter);
        readCategory();
    }

    private void readCategory() {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Admins").child("product").child("category");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoriesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Categories categories = snapshot.getValue(Categories.class);
                    categoriesList.add(categories);
                }

                categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                        Categories categoriesClicked = categoriesList.get(position);
                        intent.putExtra("title", categoriesClicked.getTitle());
                        intent.putExtra("imageurl", categoriesClicked.getIcon());
                        startActivity(intent);
                    }
                });

                categoryAdapter.setOnItemLongClickListener(new CategoryAdapter.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClicked(int position) {
                        categoriesClicked = categoriesList.get(position);
                        return true;
                    }
                });
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==121){
            StorageReference ItemImageRef = FirebaseStorage.getInstance().getReferenceFromUrl(categoriesClicked.getIcon());
            ItemImageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    DatabaseReference mrootRef= FirebaseDatabase.getInstance().getReference().child("Admins").child("product").child("category").child(categoriesClicked.getTitle());
                    mrootRef.removeValue();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Admins")
                            .child("product");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                if (snapshot.child("category").getValue().equals(categoriesClicked.getTitle())){
                                    snapshot.getRef().removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Toast.makeText(MainActivity.this, "Deleted"+ categoriesClicked.getTitle()+"successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Can't delete", Toast.LENGTH_SHORT).show();
                }
            });

            return true;
        }
        return super.onContextItemSelected(item);
    }
}