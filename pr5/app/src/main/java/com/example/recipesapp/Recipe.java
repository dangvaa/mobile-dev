package com.example.recipesapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "recipes_table")
public class Recipe implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String imageUri;
    private String ingredients;
    private String description;
    private boolean isFavorite = false;

    public Recipe(String name, String imageUri, String ingredients, String description) {
        this.name = name;
        this.imageUri = imageUri;
        this.ingredients = ingredients;
        this.description = description;
    }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}