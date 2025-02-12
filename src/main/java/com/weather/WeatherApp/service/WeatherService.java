package com.weather.WeatherApp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.WeatherApp.dto.WeatherDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Value("${openweather.api.key}")        // inject API key from application.properties
    private String apiKey;

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public WeatherDTO getWeatherByCityName(String city) {
        try {
            // 1. Geocoding to get latitude and longitude
            JsonNode getCodingResult = getCoordinates(city);
            if (getCodingResult == null || !getCodingResult.isArray() || getCodingResult.isEmpty()) {
                logger.warn("No coordinates found for city: {}", city);
                return null;  // return null if the city is not found
            }

            double lat = getCodingResult.get(0).get("lat").asDouble();
            double lon = getCodingResult.get(0).get("lon").asDouble();

            // 2. Get weather using latitude and longitude
            return getWeather(lat, lon);    // return the WeatherDTO

        } catch (Exception e) {
            logger.error("Error getting weather for city: {}", city, e);
            return null;
        }
    }

    private JsonNode getCoordinates(String city) {

        String geoCodingUrl = "http://api.openweathermap.org/geo/1.0/direct?q={city}&limit=1&appid={apiKey}";
        geoCodingUrl = geoCodingUrl.replace("{city}", city).replace("{apiKey}", apiKey);

        try {
            String response = restTemplate.getForObject(geoCodingUrl, String.class);
            return new ObjectMapper().readTree(response);
        } catch (Exception e) {
            logger.error("Error getting coordinates for city: {}", city, e);
            return null;
        }
    }

    private WeatherDTO getWeather(double lat, double lon) {

        String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={apiKey}&units=metric"; //Added units=metric
        weatherUrl = weatherUrl
                .replace("{lat}", String.valueOf(lat))
                .replace("{lon}", String.valueOf(lon))
                .replace("{apiKey}", apiKey);

        try {
            String response = restTemplate.getForObject(weatherUrl, String.class);
            JsonNode root = new ObjectMapper().readTree(response);

            WeatherDTO weatherDTO = new WeatherDTO();

            // extract relevant data:
            weatherDTO.setTemperature(root.path("main").path("temp").asDouble());
            weatherDTO.setPressure(root.path("main").path("pressure").asInt());
            weatherDTO.setHumidity(root.path("main").path("humidity").asInt());
            weatherDTO.setWindSpeed(root.path("wind").path("speed").asDouble());
            weatherDTO.setWindDirection(root.path("wind").path("deg").asInt());
            weatherDTO.setCloudiness(root.path("clouds").path("all").asInt());

           // sunrise and sunset are in UNIX timestamp format, convert to a readable time
           long sunriseTimestamp = root.path("sys").path("sunrise").asLong();
           long sunsetTimestamp = root.path("sys").path("sunset").asLong();

           weatherDTO.setSunrise(formatTimestamp(sunriseTimestamp));
           weatherDTO.setSunset(formatTimestamp(sunsetTimestamp));

           // calculate the duration of the day
            Duration dayLengthDuration = Duration.ofSeconds(sunsetTimestamp - sunriseTimestamp);
            String dayLength = formatDuration(dayLengthDuration);
            weatherDTO.setDayLength(dayLength);

           weatherDTO.setDescription(root.path("weather").get(0).path("description").asText());

           weatherDTO.setCountry(root.path("sys").path("country").asText());

           return weatherDTO;

        } catch (Exception e) {
            logger.error("Error fetching weather data", e);
            return null;
        }
    }

    private String formatTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minures = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        return String.format("%02d:%02d:%02d", hours, minures, seconds);
    }

    public Map<String, Double> getCoordinatesForCity(String city) {

        JsonNode geoCodingResult = getCoordinates(city);

        if (geoCodingResult == null || !geoCodingResult.isArray() || geoCodingResult.isEmpty()) {
            logger.warn("No coordinates found for city: {}", city);
            return null;
        }

        try {
            double lat = geoCodingResult.get(0).get("lat").asDouble();
            double lon = geoCodingResult.get(0).get("lon").asDouble();

            Map<String, Double> coordinates = new HashMap<>();
            coordinates.put("lat", lat);
            coordinates.put("lon", lon);
            return coordinates;
        } catch (Exception e) {
            logger.error("Error getting coordinates for city: {}", city, e);
            return null;
        }
    }
}










