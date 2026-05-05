package com.example.recipesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.recipesapp.BuildConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherFragment extends Fragment {

    private TextView tempText, descText, humidityText, windText;
    private ImageView weatherIcon;
    private final String CITY = "Krasnoyarsk";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        tempText = view.findViewById(R.id.temp_text);
        descText = view.findViewById(R.id.desc_text);
        humidityText = view.findViewById(R.id.humidity_text);
        windText = view.findViewById(R.id.wind_text);
        weatherIcon = view.findViewById(R.id.weather_icon);

        fetchWeatherData();

        return view;
    }

    private void fetchWeatherData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi api = retrofit.create(WeatherApi.class);

        api.getWeather(CITY, BuildConfig.WEATHER_KEY, "metric", "ru").enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse data = response.body();

                    tempText.setText((int) data.main.temp + "°C");

                    descText.setText(data.weather[0].description);

                    humidityText.setText("Влажность: " + data.main.humidity + "%");
                    windText.setText("Ветер: " + (int) data.wind.speed + " м/с");

                    String iconCode = data.weather[0].icon;
                    String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@4x.png";

                    if (getActivity() != null) {
                        Glide.with(WeatherFragment.this)
                                .load(iconUrl)
                                .into(weatherIcon);
                    }
                } else {
                    descText.setText("Ошибка: проверьте API ключ");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                descText.setText("Ошибка сети");
                Toast.makeText(getContext(), "Проверьте интернет-соединение", Toast.LENGTH_SHORT).show();
            }
        });
    }
}