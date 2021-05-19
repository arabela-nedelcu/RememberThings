package com.optima.rememberthings.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {

    @SerializedName("weather")
    @Expose
    private List<WeatherModel> weather = null;
    @SerializedName("main")
    @Expose
    private WeatherTemperatureModel weatherTemperature;

    public List<WeatherModel> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherModel> weather) {
        this.weather = weather;
    }

    public WeatherTemperatureModel getWeatherTemperature() {
        return weatherTemperature;
    }

    public void setWeatherTemperature(WeatherTemperatureModel weatherTemperature) {
        this.weatherTemperature = weatherTemperature;
    }
}
