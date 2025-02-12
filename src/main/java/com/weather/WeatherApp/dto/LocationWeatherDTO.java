package com.weather.WeatherApp.dto;

public class LocationWeatherDTO {

    private String name;
    private String temperature;
    private Long locationId;
    private String windSpeed;
    private String description;
    private int humidity;

    public LocationWeatherDTO() {
    }

    public LocationWeatherDTO(String name, String description, String temperature, String windSpeed, int humidity, Long locationId) {
        this.name = name;
        this.temperature = temperature;
        this.locationId = locationId;
        this.windSpeed = windSpeed;
        this.description = description;
        this.humidity = humidity;
    }

    public String getName() {
        return this.name;
    }

    public String getTemperature() {
        return this.temperature;
    }

    public Long getLocationId() {
        return this.locationId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
