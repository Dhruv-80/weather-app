package com.weather.weather_app.service;

import com.weather.weather_app.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherResponse getWeather(String city) {
        String url = String.format("http://api.openweathermap.org/data/2.5/weather?appid=%s&q=%s", apiKey, city);

        try {
            return restTemplate.getForObject(url, WeatherResponse.class);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new RuntimeException("Unauthorized access to OpenWeatherMap API. Please check your API key.");
            } else {
                throw new RuntimeException("Error calling OpenWeatherMap API: " + ex.getMessage());
            }
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error calling OpenWeatherMap API: " + ex.getMessage());
        }
    }
}
