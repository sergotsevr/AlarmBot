package com.gotsev.service;

import com.gotsev.models.gen.Weather;
import com.gotsev.service.interfaces.MessageService;
import com.gotsev.service.interfaces.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.gotsev.util.Messages.CURRENT_WEATHER_MESSAGE;


@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private WeatherService weatherService;

    public String prepareCurrentWeatherMessage(double[] coordinates) {
        Weather currentWeather = weatherService.getWeather(coordinates[0], coordinates[1]);
        String weatherMessage = String.format(CURRENT_WEATHER_MESSAGE, currentWeather.getName(),
                currentWeather.getWeather().get(0).getDescription(),
                currentWeather.getWind().getDeg(),
                currentWeather.getWind().getSpeed());
        return weatherMessage;
    }
}
