package com.example.restaurantmenu.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmenu.R;
import com.example.restaurantmenu.databinding.ItemCategoryBinding;
import com.example.restaurantmenu.models.Category;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

/**
 * Adapter for displaying food categories in a horizontal RecyclerView.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<Category> categories;
    private final OnCategoryClickListener listener;
    private int selectedPosition = -1;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category, int position);
    }

    public CategoryAdapter(List<Category> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category, position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setSelectedPosition(int position) {
        int previousSelected = selectedPosition;
        selectedPosition = position;
        if (previousSelected != -1) {
            notifyItemChanged(previousSelected);
        }
        if (position != -1) {
            notifyItemChanged(position);
        }
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemCategoryBinding binding;

        CategoryViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Category category, boolean isSelected) {
            binding.tvCategoryName.setText(category.getName());
            binding.ivCategoryIcon.setImageResource(category.getIconResId());

            // Update selection state
            MaterialCardView cardView = (MaterialCardView) binding.getRoot();
            if (isSelected) {
                cardView.setStrokeWidth(4);
                cardView.setStrokeColor(binding.getRoot().getContext()
                        .getColor(R.color.md_theme_primary));
                cardView.setCardBackgroundColor(binding.getRoot().getContext()
                        .getColor(R.color.md_theme_primaryContainer));
            } else {
                cardView.setStrokeWidth(1);
                cardView.setStrokeColor(binding.getRoot().getContext()
                        .getColor(R.color.md_theme_outline));
                cardView.setCardBackgroundColor(binding.getRoot().getContext()
                        .getColor(R.color.md_theme_surface));
            }

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategoryClick(category, getAdapterPosition());
                }
            });
        }
    }
}
