package com.example.adminbaasumart.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adminbaasumart.Model.Product;
import com.example.adminbaasumart.R;

import java.util.List;

import javax.microedition.khronos.opengles.GL;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder>{
    private Context mContext;
    private List<Product> productsList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public ProductAdapter(Context mContext, List<Product> mPost) {
        this.mContext = mContext;
        this.productsList = mPost;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product, parent,false);
        return new ProductAdapter.ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = productsList.get(position);
        Glide.with(mContext).load(product.getImageurl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder{
        public ImageView image, chooseOption, whatsApp, messenger, telegram, share;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.itemImage);

            chooseOption = itemView.findViewById(R.id.option);

            chooseOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onDeleteClick(position);
                        }
                    }
                }
            });

        }
    }
}
