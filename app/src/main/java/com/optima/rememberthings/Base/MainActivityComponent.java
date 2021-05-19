package com.optima.rememberthings.Base;

import com.optima.rememberthings.activities.MainActivity;
import com.optima.rememberthings.http.RetrofitApiClient;
import com.optima.rememberthings.http.interfaces.WeatherAPIService;

import dagger.Component;

@Component(modules = RetrofitApiClient.class)
public interface MainActivityComponent {
    public WeatherAPIService getWeatherApiService();
    void inject(MainActivity main);
}