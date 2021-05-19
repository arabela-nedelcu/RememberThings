package com.optima.rememberthings.http.interfaces;

import com.optima.rememberthings.models.Weather;

import dagger.Provides;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface WeatherAPIService {

    @GET("data/2.5/weather")
    Call<Weather> getWeatherData(@Query("q") String cityName,
                                 @Query("appid") String appId,
                                 @Query("units") String units);
}
