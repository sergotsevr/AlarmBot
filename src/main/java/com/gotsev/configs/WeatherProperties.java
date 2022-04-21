package com.gotsev.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class WeatherProperties {

    @Value("${weather.appid}")
    private String appid;
}
