package com.example.recipesapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class RecipeDetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        TextView titleText = view.findViewById(R.id.detailTitle);
        ImageView recipeImage = view.findViewById(R.id.detailImage);
        TextView ingredientsText = view.findViewById(R.id.ingredientsText);
        TextView descriptionText = view.findViewById(R.id.instructionText);

        if (getArguments() != null) {
            Recipe recipe = (Recipe) getArguments().getSerializable("recipe_extra");

            if (recipe != null) {
                titleText.setText(recipe.getName());
                ingredientsText.setText(recipe.getIngredients());
                descriptionText.setText(recipe.getDescription());

                if (recipe.getImageUri() == null || recipe.getImageUri().isEmpty()) {
                    recipeImage.setVisibility(View.GONE);
                } else {
                    recipeImage.setVisibility(View.VISIBLE);
                    Glide.with(this)
                            .load(Uri.parse(recipe.getImageUri()))
                            .into(recipeImage);
                }
            }
        }

        return view;
    }
}