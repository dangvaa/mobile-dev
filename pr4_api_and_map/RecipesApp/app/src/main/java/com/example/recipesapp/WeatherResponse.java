package com.example.recipesapp;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("main")
    public MainData main;

    @SerializedName("weather")
    public WeatherDescription[] weather;

    @SerializedName("wind")
    public WindData wind;

    public class MainData {
        public float temp;
        public int humidity;
    }

    public class WeatherDescription {
        public String description;
        public String icon;
    }

    public class WindData {
        public float speed;
    }
}