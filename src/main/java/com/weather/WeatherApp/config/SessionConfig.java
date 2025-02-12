package com.weather.WeatherApp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

/**
 • Configuration class for enabling JDBC-based HTTP sessions.
 • This configuration allows storing user session data in a relational database using JDBC.
 • This is useful for scenarios where you need to share sessions across multiple application instances
 • or when you want to persist session data even after the application restarts.
 */
@Configuration
@EnableJdbcHttpSession // Enables the use of JDBC to manage HTTP sessions.
public class SessionConfig {

    // No explicit configuration is needed here.  The @EnableJdbcHttpSession annotation
    // automatically configures the necessary beans and infrastructure to store sessions in a JDBC-compatible database.
    //
    // By default, it will create a table named "SPRING_SESSION" to store session data. You can customize the
    // table name and other session properties using application.properties or application.yml.
    //
    // Example customizations in application.properties:
    //
    // spring.session.store-type=jdbc   // Explicitly specify JDBC as the session store type (optional, but good practice)
    // spring.session.jdbc.table-name=MY_SESSION_TABLE  // Customize the session table name
    // spring.session.cleanup-cron=0 0 12 * * ?  // Configure a cron expression for cleaning up expired sessions.
}
