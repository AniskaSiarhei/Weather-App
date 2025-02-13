![2025-02-13_11-38-41](https://github.com/user-attachments/assets/8267111a-ea10-42bd-bc61-8ebdc2a46ed4)
![2025-02-13_11-34-53](https://github.com/user-attachments/assets/538dd726-2408-4681-a40f-130f52e85c4e)

# WeatherApp

A simple Spring Boot weather application that allows users to:

•   Register and log in to their account.
•   Search for weather information by city.
•   Save locations to their account for quick access.
•   View current weather information for their saved locations.

## Technologies Used

•   **Spring Boot:** A Java-based framework for building web applications.
•   **Spring Security:** For user authentication and authorization.
•   **Spring Data JPA:**  Simplified database interactions using JPA repositories.
•   **Thymeleaf:** A server-side Java template engine for creating dynamic web pages.
•   **Bootstrap 5:**
  A CSS framework for responsive and modern UI design.
•   **MySQL:**  A relational database for storing user accounts and saved locations.
•   **OpenWeatherMap API:** A service providing real-time weather data.
•   **JDBC:** For storing HTTP session data in a MySQL database.
•   **SLF4J and Logback:** For logging application events.

## Features

•   **User Authentication:**  Secure user registration and login using Spring Security.  Passwords are encrypted using BCrypt.
•   **Weather Search:**  Find current weather conditions for any city using the OpenWeatherMap API.
•   **Location Management:**
    •   Save frequently visited locations to your account.
    •   View current weather conditions for all saved locations on the homepage.
    •   Remove saved locations.
•   **Responsive Design:**  The application is built with Bootstrap 5, providing a responsive and user-friendly experience on different devices.
•   **Session Management:** User session data is stored in a MySQL database using JDBC, allowing for session persistence across application restarts and scalability.
•   **Logging:** Detailed logging using SLF4J and Logback helps track application behavior and debug issues.

## Setup and Installation

1.  **Prerequisites:**
    •   Java Development Kit (JDK) 17 or higher
    •   Maven
    •   MySQL Database
    •   OpenWeatherMap API key.  [Sign up for a free account](https://openweathermap.org/price) to obtain an API key.

2.  **Clone the repository:**
```
bash
  git clone
  cd WeatherApp
```

3. Configure the application:

  •  Rename the file to application.properties or application.yml and place in the src/main/resources folder.
  •  Update the following properties in application.properties with your database credentials and OpenWeatherMap API key:
  
```
properties
  spring.datasource.url=jdbc:mysql://localhost:3306/weather_app?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
  spring.datasource.username=your_mysql_username
  spring.datasource.password=your_mysql_password
  openweather.api.key=your_openweathermap_api_key
```
4. Build and run the application:
 
```
bash
    mvn spring-boot:run
```

5. Access the application:

  Open your web browser and navigate to http://localhost:8080.

▌Configuration

The application can be configured using the application.properties file. Here are some of the key configuration options:

•  spring.datasource.url: The URL for connecting to the MySQL database.
•  spring.datasource.username: The username for connecting to the MySQL database.
•  spring.datasource.password: The password for connecting to the MySQL database.
•  spring.jpa.hibernate.ddl-auto: Automatically generates a database schema upon application startup. It's highly advised to set to none in production
•  openweather.api.key: Your OpenWeatherMap API key.
•  server.servlet.session.timeout: Session timeout in seconds.
•  spring.session.store-type: Defaults to jdbc, but you can explicity set it.
•  spring.session.jdbc.table-name: The name of the database table used to store HTTP sessions (defaults to SPRING_SESSION).
•  spring.session.cleanup-cron: Cron expression for cleaning up expired sessions. Example: 0 0 12   ? (runs daily at noon).

▌Security

•  User authentication is implemented using Spring Security.
•  Passwords are encrypted using BCrypt.
•  The application currently disables CSRF protection for simplicity.
   In a production environment, CSRF protection should be enabled and properly configured.

▌Logging

The application uses SLF4J and Logback for logging.

▌API Endpoints
```
•  GET /: Displays the homepage with weather information for saved locations.
•  GET /login: Displays the login page.
•  POST /login: Handles user login.
•  GET /register: Displays the registration page.
•  POST /register: Handles user registration.
•  GET /logout: Logs out the user.
•  POST /getWeatherByCity: Retrieves weather information for a specific city.
•  POST /addLocation: Adds a city to the user's saved locations.
•  POST /removeLocation: Removes a city from the user's saved locations.
```
▌Database Schema

The application uses the following database tables:
```
•  user: Stores user account information (id, login, password).
•  location: Stores saved locations for each user (id, name, latitude, longitude, user_id).

•  SPRING_SESSION: Stores user session data (if using JDBC session management).
•  SPRING_SESSION_ATTRIBUTES: Stores session attributes (if using JDBC session management).
```
▌Code Structure
```
•  src/main/java/com/weather/WeatherApp/:
  •  config/: Contains Spring configuration classes (SecurityConfig, SessionConfig).
  •  controller/: Contains Spring MVC controllers (UserController, WeatherController).
  •  dto/: Contains Data Transfer Objects (LocationWeatherDTO, WeatherDTO).
  •  model/: Contains data models (Location, User).
  •  repository/: Contains Spring Data JPA repositories (LocationRepository, UserRepository).
  •  service/: Contains business logic services (CustomUserDetailsService, UserService, WeatherService).
  •  WeatherAppApplication.java: The main Spring Boot application class.
•  src/main/resources/:
  •  application.properties: Configuration properties for the application.
  •  templates/: Thymeleaf templates for the web pages (index.html, login.html, register.html).
  •  static/: Static resources (CSS, JavaScript, images).
```
