package com.example.recipesapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    void insert(Recipe recipe);

    @Query("SELECT * FROM recipes_table")
    List<Recipe> getAllRecipes();

    @Update
    void update(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Query("DELETE FROM recipes_table")
    void deleteAll();

    @Query("SELECT * FROM recipes_table WHERE isFavorite = 1")
    List<Recipe> getFavoriteRecipes();
}