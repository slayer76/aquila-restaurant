package com.aquila.restaurant.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.aquila.restaurant.R;
import com.aquila.restaurant.databinding.ActivityDetailBinding;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * DetailActivity displays detailed information about a selected food item.
 */
public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_FOOD_ID = "extra_food_id";
    public static final String EXTRA_FOOD_NAME = "extra_food_name";
    public static final String EXTRA_FOOD_PRICE = "extra_food_price";
    public static final String EXTRA_FOOD_DESCRIPTION = "extra_food_description";
    public static final String EXTRA_FOOD_IMAGE_URL = "extra_food_image_url";
    public static final String EXTRA_FOOD_CATEGORY = "extra_food_category";

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up toolbar with back button
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        loadFoodDetails();
    }

    private void loadFoodDetails() {
        String name = getIntent().getStringExtra(EXTRA_FOOD_NAME);
        double price = getIntent().getDoubleExtra(EXTRA_FOOD_PRICE, 0.0);
        String description = getIntent().getStringExtra(EXTRA_FOOD_DESCRIPTION);
        String imageUrl = getIntent().getStringExtra(EXTRA_FOOD_IMAGE_URL);
        String category = getIntent().getStringExtra(EXTRA_FOOD_CATEGORY);

        // Set toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(name != null ? name : getString(R.string.food_details));
        }

        // Set food details
        binding.tvFoodName.setText(name != null ? name : "");
        binding.tvFoodDescription.setText(description != null ? description : "");
        binding.tvFoodCategory.setText(category != null ? category : "");
        
        // Format price
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        binding.tvFoodPrice.setText(currencyFormat.format(price));

        // Load image with Glide
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_food)
                    .error(R.drawable.placeholder_food)
                    .centerCrop()
                    .into(binding.ivFoodImage);
        } else {
            binding.ivFoodImage.setImageResource(R.drawable.placeholder_food);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
