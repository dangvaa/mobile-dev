package com.example.recipesapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.concurrent.Executors;

public class AddFragment extends Fragment {

    private EditText inputName, inputIngredients, inputDescription;
    private ImageView imagePreview;
    private String selectedImageUri = "";

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        selectedImageUri = uri.toString();
                        imagePreview.setImageURI(uri);
                        imagePreview.setVisibility(View.VISIBLE);

                        getContext().getContentResolver().takePersistableUriPermission(uri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        inputName = view.findViewById(R.id.input_name);
        inputIngredients = view.findViewById(R.id.input_ingredients);
        inputDescription = view.findViewById(R.id.input_description);
        imagePreview = view.findViewById(R.id.image_preview);
        Button btnLoad = view.findViewById(R.id.btn_load_photo);
        Button btnSave = view.findViewById(R.id.btn_save);

        imagePreview.setVisibility(View.GONE);

        btnLoad.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            pickImageLauncher.launch(intent);
        });

        btnSave.setOnClickListener(v -> saveRecipe());

        return view;
    }

    private void saveRecipe() {
        String name = inputName.getText().toString().trim();
        String ingredients = inputIngredients.getText().toString().trim();
        String description = inputDescription.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Введите название!", Toast.LENGTH_SHORT).show();
            return;
        }

        Recipe newRecipe = new Recipe(name, selectedImageUri, ingredients, description);

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase.getInstance(getContext()).recipeDao().insert(newRecipe);

            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Рецепт добавлен!", Toast.LENGTH_SHORT).show();

                inputName.setText("");
                inputIngredients.setText("");
                inputDescription.setText("");
                imagePreview.setVisibility(View.GONE);
                selectedImageUri = "";

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            });
        });
    }
}