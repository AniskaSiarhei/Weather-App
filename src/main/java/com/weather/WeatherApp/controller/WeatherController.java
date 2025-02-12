package com.weather.WeatherApp.controller;

import com.weather.WeatherApp.dto.LocationWeatherDTO;
import com.weather.WeatherApp.dto.WeatherDTO;
import com.weather.WeatherApp.model.Location;
import com.weather.WeatherApp.model.User;
import com.weather.WeatherApp.repository.LocationRepository;
import com.weather.WeatherApp.repository.UserRepository;
import com.weather.WeatherApp.service.WeatherService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    private final WeatherService weatherService;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public WeatherController(WeatherService weatherService, LocationRepository locationRepository, UserRepository userRepository) {
        this.weatherService = weatherService;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String showWeather(Model model, HttpSession session) {

        logger.info("User '{}' accessed the home page", session.getAttribute("user"));  // Log user access.

        User user = (User) session.getAttribute("user");

        // If User is not logged in, redirect to the login page
        if (user == null) {
            return "redirect:/login";
        }

        // Getting a list of locations added by User
        List<Location> locations = locationRepository.findByUser(user);
        List<LocationWeatherDTO> locationWeatherList = new ArrayList<>();

        if (locations != null && !locations.isEmpty()) {
            for (Location location : locations) {

                WeatherDTO weatherData = weatherService.getWeatherByCityName(location.getName()); // Use city name to the weather
                String temperature = (weatherData != null) ? String.format("%.1f", weatherData.getTemperature()) : "N/A";
                String windSpeed = (weatherData != null) ? String.format("%.1f", weatherData.getWindSpeed()) : "N/A";
                String description = (weatherData != null) ? weatherData.getDescription() : "N/A";
                int humidity = (weatherData != null) ? weatherData.getHumidity() : 0;

                LocationWeatherDTO locationWeather = new LocationWeatherDTO(location.getName(), description, temperature, windSpeed, humidity, location.getId());
                locationWeatherList.add(locationWeather);   // Create and add LocationWeatherDTO.
            }
        }

        model.addAttribute("locations", locationWeatherList); // Add locations to model.
        model.addAttribute("userId", user.getId()); // Add user ID to model.
        return "index"; // Return index view.
    }

    @PostMapping("/getWeatherByCity")
    public String getWeatherByCity(@RequestParam String city, Model model, HttpSession session) {

        logger.info("User '{}' accessed the home page", session.getAttribute("user"));

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        WeatherDTO weatherData = weatherService.getWeatherByCityName(city);

        if (weatherData != null) {
            model.addAttribute("weather", weatherData);
            model.addAttribute("city", city);
        } else {
            model.addAttribute("errorMessage", "Could not retrieve weather for " + city);   //Add error message to model.
        }

        // refetch the location list after searching a new list
        List<Location> locations = locationRepository.findByUser(user);
        List<LocationWeatherDTO> locationWeatherList = new ArrayList<>();

        if (locations != null && !locations.isEmpty()) {
            for (Location location : locations) {
                WeatherDTO locationWeatherData = weatherService.getWeatherByCityName(location.getName()); // get full weather data

                String temperature = (locationWeatherData != null) ? String.format("%.1f", locationWeatherData.getTemperature()) : "N/A";
                String windSpeed = (locationWeatherData != null) ? String.format("%.1f", locationWeatherData.getWindSpeed()) : "N/A";
                String description = (locationWeatherData != null) ? locationWeatherData.getDescription() : "N/A";
                int humidity = (locationWeatherData != null) ? locationWeatherData.getHumidity() : 0;

                LocationWeatherDTO locationWeather = new LocationWeatherDTO(location.getName(), description, temperature, windSpeed, humidity, location.getId());
                locationWeatherList.add(locationWeather);
            }
        }

        model.addAttribute("locations", locationWeatherList);
        return "index";
    }

    @PostMapping("/removeLocation")
    public String removeLocation(@RequestParam Long locationId, HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Location location = locationRepository.findById(locationId).orElse(null);

        if (location != null && location.getUser().equals(user)) {
            locationRepository.delete(location);
        } else {
            // Log a warning if the location doesn't belong to the user or doesn't exist
            logger.warn("Attempted to delete location {} by user {}, but location either doesn't exist or doesn't belong to the user.", locationId, user.getId());
        }

        return "redirect:/";
    }

    @PostMapping("/addLocation")
    public String addLocation(@RequestParam String city, HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Map<String, Double> coordinates = weatherService.getCoordinatesForCity(city);

        if (coordinates == null) {
            model.addAttribute("errorMessage", "The coordinates for this city could not be found.");
            return "redirect:/";
        }

        Double latitudeObj = coordinates.get("lat");
        Double longitudeObj = coordinates.get("lon");

        if (latitudeObj == null || longitudeObj == null) {
            logger.error("Latitude or longitude is null for city: {}", city);
            model.addAttribute("errorMessage", "The coordinates for this city could not be found.");
            return "redirect:/";
        }

        double latitude = latitudeObj.doubleValue();
        double longitude = longitudeObj.doubleValue();

        // save location for user
        Location location = new Location();
        location.setName(city);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setUser(user);

        locationRepository.save(location);
        return "redirect:/";
    }
}
