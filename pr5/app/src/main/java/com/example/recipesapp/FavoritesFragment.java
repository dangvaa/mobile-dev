package com.example.recipesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.concurrent.Executors;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        view.findViewById(R.id.btn_weather).setOnClickListener(v -> openFragment(new WeatherFragment()));
        view.findViewById(R.id.btn_map).setOnClickListener(v -> openFragment(new MapFragment()));

        recyclerView = view.findViewById(R.id.favRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadFavorites();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites();
    }

    private void loadFavorites() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Recipe> favs = AppDatabase.getInstance(getContext()).recipeDao().getFavoriteRecipes();

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    adapter = new RecipeAdapter(favs);

                    adapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Recipe recipe) {
                            RecipeDetailFragment detailFragment = new RecipeDetailFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("recipe_extra", recipe);
                            detailFragment.setArguments(bundle);
                            openFragment(detailFragment);
                        }

                        @Override
                        public void onItemLongClick(Recipe recipe) {
                            showOptionsDialog(recipe);
                        }
                    });

                    recyclerView.setAdapter(adapter);
                });
            }
        });
    }

    private void showOptionsDialog(Recipe recipe) {
        String[] options = {"Убрать из избранного", "Редактировать", "Удалить"};

        new AlertDialog.Builder(requireContext())
                .setTitle(recipe.getName())
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            removeFromFavorites(recipe);
                            break;
                        case 1:
                            openEditFragment(recipe);
                            break;
                        case 2:
                            deleteRecipe(recipe);
                            break;
                    }
                })
                .show();
    }

    private void removeFromFavorites(Recipe recipe) {
        recipe.setFavorite(false);
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase.getInstance(getContext()).recipeDao().update(recipe);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Удалено из избранного", Toast.LENGTH_SHORT).show();
                    loadFavorites();
                });
            }
        });
    }

    private void deleteRecipe(Recipe recipe) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Удаление")
                .setMessage("Вы точно хотите навсегда удалить рецепт «" + recipe.getName() + "»?")
                .setPositiveButton("Да", (d, w) -> {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        AppDatabase.getInstance(getContext()).recipeDao().delete(recipe);
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                loadFavorites();
                                Toast.makeText(getContext(), "Рецепт удален", Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                })
                .setNegativeButton("Нет", null)
                .show();
    }

    private void openEditFragment(Recipe recipe) {
        AddFragment addFragment = new AddFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("recipe_to_edit", recipe);
        addFragment.setArguments(bundle);
        openFragment(addFragment);
    }

    private void openFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}