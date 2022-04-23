package com.gotsev.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gotsev.configs.WeatherProperties;
import com.gotsev.models.gen.Weather;
import com.gotsev.service.interfaces.WeatherService;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.gotsev.util.ParameterStringBuilder.getParamsString;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    public WeatherServiceImpl(WeatherProperties properties) throws MalformedURLException {
        this.properties = properties;
    }

    private final WeatherProperties properties;

    private final URL weatherPath = Paths.get("src/main/resources/schemas/weather.json").toUri().toURL();
    String url = "https://api.openweathermap.org/data/2.5/weather";
    private final ObjectMapper objectMapper = new ObjectMapper();


    @SneakyThrows
    @Override
    public Integer getWindDirection(Double latitude, Double longitude) {
        updateWeather(latitude, longitude);
        Weather weather = objectMapper.readValue(weatherPath, Weather.class);
        log.info(weather.toString());
        return weather.getWind().getDeg();
    }

    @SneakyThrows
    @Override
    public Weather getWeather(Double latitude, Double longitude) {
        updateWeather(latitude, longitude);
        Weather weather = objectMapper.readValue(weatherPath, Weather.class);
        log.info(weather.toString());
        return weather;
    }

    @SneakyThrows
    private void updateWeather(Double latitude, Double longitude){
        Map<String, String> params = new HashMap<>();
        params.put("appid", properties.getAppid());
        params.put("lat", latitude.toString());
        params.put("lon", longitude.toString());
        String paramsStr = getParamsString(params);
        URL obj = new URL(url + paramsStr);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");

        FileOutputStream outputStream = new FileOutputStream("src/main/resources/schemas/weather.json");
        outputStream.write(connection.getInputStream().readAllBytes());
        outputStream.close();
    }


}
