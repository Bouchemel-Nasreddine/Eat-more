package com.exemple.eatmore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.exemple.eatmore.MainActivity.Category;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private final ArrayList<Category> categoriesList;
    private final OnCategoryListener onCategoryListener;

    public CategoriesAdapter(ArrayList<Category> categoriesList, OnCategoryListener onCategoryListener) {
        this.categoriesList = categoriesList;
        this.onCategoryListener = onCategoryListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View categoryItem = inflater.inflate(R.layout.category_layout, parent, false);
        return new ViewHolder(categoryItem, this.onCategoryListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category item = this.categoriesList.get(position);
        holder.catName.setText(item.getName());
        holder.catImage.setImageResource(item.getDrawable_ID());

        if (position == 0) {
            item.changeClicked();
            holder.catName.setTextColor(ContextCompat.getColor(holder.catLayout.getContext(), R.color.pink));
            holder.catImage.setImageResource(item.getDrawable_pink_ID());
            holder.catLayout.setBackground(ContextCompat.getDrawable(holder.catLayout.getContext(), R.drawable.categorie_shadow_pink));
        }
    }

    @Override
    public int getItemCount() {
        return this.categoriesList.size();
    }

    public interface OnCategoryListener {
        void onCategoryClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ConstraintLayout catLayout;
        public ImageView catImage;
        public TextView catName;
        public OnCategoryListener onCategoryListener;

        public ViewHolder(@NonNull View itemView, OnCategoryListener onCategoryListener) {
            super(itemView);
            this.catLayout = itemView.findViewById(R.id.category_layout);
            this.catImage = itemView.findViewById(R.id.category_picture);
            this.catName = itemView.findViewById(R.id.category_name);
            this.onCategoryListener = onCategoryListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onCategoryListener.onCategoryClick(getAdapterPosition());
        }
    }

}
