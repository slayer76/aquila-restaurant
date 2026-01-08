package com.example.restaurantmenu.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.restaurantmenu.R;
import com.example.restaurantmenu.databinding.ItemFoodBinding;
import com.example.restaurantmenu.models.FoodItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying food items in a vertical RecyclerView.
 */
public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder> {

    private List<FoodItem> foodItems;
    private final OnFoodItemClickListener listener;
    private final NumberFormat currencyFormat;

    public interface OnFoodItemClickListener {
        void onFoodItemClick(FoodItem foodItem);
    }

    public FoodItemAdapter(List<FoodItem> foodItems, OnFoodItemClickListener listener) {
        this.foodItems = foodItems;
        this.listener = listener;
        this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    }

    @NonNull
    @Override
    public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodBinding binding = ItemFoodBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);
        holder.bind(foodItem);
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public void updateItems(List<FoodItem> newItems) {
        this.foodItems = newItems;
        notifyDataSetChanged();
    }

    class FoodItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemFoodBinding binding;

        FoodItemViewHolder(ItemFoodBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(FoodItem foodItem) {
            binding.tvFoodName.setText(foodItem.getName());
            binding.tvFoodDescription.setText(foodItem.getDescription());
            binding.tvFoodPrice.setText(currencyFormat.format(foodItem.getPrice()));
            binding.tvFoodCategory.setText(foodItem.getCategory());

            // Load image with Glide
            if (foodItem.getImageUrl() != null && !foodItem.getImageUrl().isEmpty()) {
                Glide.with(binding.getRoot().getContext())
                        .load(foodItem.getImageUrl())
                        .placeholder(R.drawable.placeholder_food)
                        .error(R.drawable.placeholder_food)
                        .centerCrop()
                        .into(binding.ivFoodImage);
            } else {
                binding.ivFoodImage.setImageResource(R.drawable.placeholder_food);
            }

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFoodItemClick(foodItem);
                }
            });
        }
    }
}
