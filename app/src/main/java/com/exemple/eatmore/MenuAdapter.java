package com.exemple.eatmore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exemple.eatmore.MainActivity.MenuItem;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private final ArrayList<MenuItem> menuList;
    private final OnMenuListener mOnMenuListener;

    public MenuAdapter(ArrayList<MenuItem> menuList, OnMenuListener onMenuListener) {
        this.menuList = menuList;
        this.mOnMenuListener = onMenuListener;
    }

    @NonNull
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View layout = inflater.inflate(R.layout.menu_layout, parent, false);
        return new ViewHolder(layout, mOnMenuListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ViewHolder holder, int position) {
        MenuItem item = menuList.get(position);
        holder.picture.setImageResource(item.getDrawable_ID());
        holder.name.setText(item.getName());
        holder.price.setText("$" + item.getPrice());
        switch (item.getRating()) {
            case 1:
                holder.rating.setImageResource(R.drawable.rating_4);
            case 2:
                holder.rating.setImageResource(R.drawable.rating_4);
            case 3:
                holder.rating.setImageResource(R.drawable.rating_4);
            case 4:
                holder.rating.setImageResource(R.drawable.rating_4);
            case 5:
                holder.rating.setImageResource(R.drawable.rating_4);
        }

    }

    @Override
    public int getItemCount() {
        return this.menuList.size();
    }

    public interface OnMenuListener {
        void onMenuClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, price;
        public ImageView picture, rating;
        public OnMenuListener onMenuListener;

        public ViewHolder(@NonNull View itemView, OnMenuListener onMenuListener) {
            super(itemView);
            picture = itemView.findViewById(R.id.menu_item_picture);
            name = itemView.findViewById(R.id.menu_item_name);
            rating = itemView.findViewById(R.id.menu_item_rating);
            price = itemView.findViewById(R.id.menu_item_price);
            this.onMenuListener = onMenuListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMenuListener.onMenuClick(getAdapterPosition());
        }
    }
}
