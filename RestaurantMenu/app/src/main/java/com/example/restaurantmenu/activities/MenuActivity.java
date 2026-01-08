package com.example.restaurantmenu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.restaurantmenu.R;
import com.example.restaurantmenu.adapters.CategoryAdapter;
import com.example.restaurantmenu.adapters.FoodItemAdapter;
import com.example.restaurantmenu.databinding.ActivityMenuBinding;
import com.example.restaurantmenu.models.Category;
import com.example.restaurantmenu.models.FoodItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * MenuActivity displays food categories and items from Firestore.
 * Uses RecyclerView to show categories (Appetizers, Mains, Desserts).
 */
public class MenuActivity extends AppCompatActivity implements 
        CategoryAdapter.OnCategoryClickListener,
        FoodItemAdapter.OnFoodItemClickListener {

    private static final String TAG = "MenuActivity";
    
    private ActivityMenuBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    
    private CategoryAdapter categoryAdapter;
    private FoodItemAdapter foodItemAdapter;
    
    private List<Category> categories;
    private List<FoodItem> foodItems;
    private String selectedCategory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.menu_title);
        }

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        setupCategories();
        setupRecyclerViews();
        loadFoodItems();
    }

    private void setupCategories() {
        categories = new ArrayList<>();
        categories.add(new Category("Appetizers", "Start your meal right", R.drawable.ic_appetizer));
        categories.add(new Category("Mains", "Hearty main courses", R.drawable.ic_main));
        categories.add(new Category("Desserts", "Sweet endings", R.drawable.ic_dessert));
    }

    private void setupRecyclerViews() {
        // Setup category RecyclerView (horizontal)
        categoryAdapter = new CategoryAdapter(categories, this);
        binding.rvCategories.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategories.setAdapter(categoryAdapter);

        // Setup food items RecyclerView (vertical)
        foodItems = new ArrayList<>();
        foodItemAdapter = new FoodItemAdapter(foodItems, this);
        binding.rvFoodItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvFoodItems.setAdapter(foodItemAdapter);
    }

    private void loadFoodItems() {
        showLoading(true);
        
        db.collection("foodItems")
                .get()
                .addOnCompleteListener(task -> {
                    showLoading(false);
                    
                    if (task.isSuccessful()) {
                        foodItems.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FoodItem item = document.toObject(FoodItem.class);
                            item.setId(document.getId());
                            foodItems.add(item);
                        }
                        
                        if (foodItems.isEmpty()) {
                            showEmptyState(true);
                        } else {
                            showEmptyState(false);
                            filterByCategory(selectedCategory);
                        }
                        
                        Log.d(TAG, "Loaded " + foodItems.size() + " food items");
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                        Toast.makeText(this, "Failed to load menu items", Toast.LENGTH_SHORT).show();
                        showEmptyState(true);
                    }
                });
    }

    private void filterByCategory(String category) {
        List<FoodItem> filteredItems = new ArrayList<>();
        
        if (category == null || category.isEmpty()) {
            filteredItems.addAll(foodItems);
        } else {
            for (FoodItem item : foodItems) {
                if (category.equalsIgnoreCase(item.getCategory())) {
                    filteredItems.add(item);
                }
            }
        }
        
        foodItemAdapter.updateItems(filteredItems);
        
        if (filteredItems.isEmpty() && !foodItems.isEmpty()) {
            binding.tvEmptyState.setText(R.string.no_items_in_category);
            showEmptyState(true);
        } else if (!filteredItems.isEmpty()) {
            showEmptyState(false);
        }
    }

    @Override
    public void onCategoryClick(Category category, int position) {
        if (selectedCategory != null && selectedCategory.equals(category.getName())) {
            // Deselect if clicking the same category
            selectedCategory = null;
            categoryAdapter.setSelectedPosition(-1);
        } else {
            selectedCategory = category.getName();
            categoryAdapter.setSelectedPosition(position);
        }
        filterByCategory(selectedCategory);
    }

    @Override
    public void onFoodItemClick(FoodItem foodItem) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_FOOD_ID, foodItem.getId());
        intent.putExtra(DetailActivity.EXTRA_FOOD_NAME, foodItem.getName());
        intent.putExtra(DetailActivity.EXTRA_FOOD_PRICE, foodItem.getPrice());
        intent.putExtra(DetailActivity.EXTRA_FOOD_DESCRIPTION, foodItem.getDescription());
        intent.putExtra(DetailActivity.EXTRA_FOOD_IMAGE_URL, foodItem.getImageUrl());
        intent.putExtra(DetailActivity.EXTRA_FOOD_CATEGORY, foodItem.getCategory());
        startActivity(intent);
    }

    private void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.rvFoodItems.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    private void showEmptyState(boolean isEmpty) {
        binding.tvEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.rvFoodItems.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        } else if (item.getItemId() == R.id.action_refresh) {
            loadFoodItems();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
