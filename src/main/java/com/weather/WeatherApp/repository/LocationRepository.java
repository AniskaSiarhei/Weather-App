package com.weather.WeatherApp.repository;

import com.weather.WeatherApp.model.Location;
import com.weather.WeatherApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByUser(User user);

}
