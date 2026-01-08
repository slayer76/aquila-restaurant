package com.example.restaurantmenu.models;

/**
 * Model class representing a food category in the restaurant menu.
 */
public class Category {
    
    private String name;
    private String description;
    private int iconResId;

    public Category() {}

    public Category(String name, String description, int iconResId) {
        this.name = name;
        this.description = description;
        this.iconResId = iconResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
}
