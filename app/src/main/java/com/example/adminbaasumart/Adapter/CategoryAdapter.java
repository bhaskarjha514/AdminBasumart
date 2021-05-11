package com.example.adminbaasumart.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adminbaasumart.Model.Categories;
import com.example.adminbaasumart.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder>{
    private Context mContext;
    private List<Categories> mCategory;
    private OnItemClickListener mListener;
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener){
        onItemLongClickListener = longClickListener;
    }
    public CategoryAdapter(Context mContext, List<Categories> mCategory) {
        this.mContext = mContext;
        this.mCategory = mCategory;

    }
    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_list, parent, false);
        return new CategoryAdapter.CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Categories categories = mCategory.get(position);
        Glide.with(mContext).load(categories.getIcon()).into(holder.icon);
        if (categories.getTitle() == null){
            holder.title.setVisibility(View.GONE);
        }else {
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(categories.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView title;
        public ImageView icon;
        CardView cardView;
        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.categories_title_id);
            icon = itemView.findViewById(R.id.categories_img_id);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (onItemLongClickListener != null){
                        int position =getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            onItemLongClickListener.onItemLongClicked(position);
                        }
                    }
                    return false;
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("options");
            menu.add(this.getAdapterPosition(),121,0,"Delete");
        }
    }
}
