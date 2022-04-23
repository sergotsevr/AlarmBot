package com.gotsev.service.interfaces;

import com.gotsev.models.gen.Weather;

public interface WeatherService {

    public Integer getWindDirection(Double latitude, Double longitude);
    public Weather getWeather(Double latitude, Double longitude);

}
