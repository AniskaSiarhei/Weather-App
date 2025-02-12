
package com.weather.WeatherApp.config;

import com.weather.WeatherApp.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for Spring Security.  Defines security rules, authentication mechanisms, and password encoding.
 */
@Configuration
@EnableWebSecurity  // Enable Spring Security's web security features.
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructor for SecurityConfig, injecting the CustomUserDetailsService.
     *
     * @param userDetailsService Service responsible for loading user-specific data.
     */
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Defines the security filter chain.  Configures authentication and authorization rules for
     * incoming HTTP requests.
     *
     * @param http HttpSecurity object used to configure security features.
     * @return A SecurityFilterChain object representing the configured security filter chain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection (Cross-Site Request Forgery).
                .csrf(AbstractHttpConfigurer::disable)  // Use lambda expression to disable CSRF.
                //.csrf(csrf -> csrf.disable()) //Alternative way to disable CSRF

                // Configure authorization rules for HTTP requests.
                .authorizeHttpRequests(auth -> auth
                        // Allow unrestricted access to the specified paths (root "/", login, register, CSS, JS, images, fonts).
                        // These paths typically represent public resources and pages.
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**", "/fonts/**")
                        .permitAll()  // Allow access to the specified paths without authentication.

                        // All other requests require authentication.  Any request that does not match the permitAll()
                        // rules will require the user to be authenticated.
                        .anyRequest().authenticated() // All other requests require authentication.
                )

                // Configure form-based login.  This allows users to authenticate using a login form.
                .formLogin(form -> form
                        // Specify the URL for the custom login page.  This is the endpoint that will render the login form.
                        .loginPage("/login")        // Specify a custom login page.

                        // Specify the URL to redirect to after successful login.  `true` argument always redirects to this URL.
                        //  If set to false, spring will redirect to the requested page.
                        .defaultSuccessUrl("/", true)     // Redirect to the root path ("/") after successful login.  The `true` argument forces redirection even if the user had requested a different page before logging in.

                        // Specify the URL to redirect to after a failed login attempt.  The `error=true` parameter indicates
                        // that an error occurred during login, which the login page can use to display an error message.
                        .failureUrl("/login?error=true")

                        // Allow unrestricted access to the login page (both for displaying the form and processing the login).
                        .permitAll()
                )

                // Configure logout handling.  This allows users to log out of the application.
                .logout(logout -> logout
                        // Specify the URL for initiating the logout process.
                        .logoutUrl("/logout")

                        // Specify the URL to redirect to after successful logout.
                        .logoutSuccessUrl("/login")

                        // Allow unrestricted access to the logout URL.
                        .permitAll()                // Exit URL
                )

                // Set the UserDetailsService.  This is responsible for loading user details during authentication.  The
                // `CustomUserDetailsService` implementation will be used to retrieve user information from the database.
                .userDetailsService(userDetailsService);

        return http.build(); // Build and return the configured SecurityFilterChain.
    }

    /**
     * Defines a bean for BCryptPasswordEncoder.  This is used to encode and verify passwords.
     * BCrypt is a strong password hashing algorithm that provides good security against brute-force attacks.
     *
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(); // Create and return a new BCryptPasswordEncoder.
    }
}
