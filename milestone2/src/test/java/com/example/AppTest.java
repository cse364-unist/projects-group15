package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    @Test
    public void testGetUser() {
        ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
    }
    @Test
    public void testGetRating() {
        ResponseEntity<Rating> response = restTemplate.getForEntity("http://localhost:" + port + "/ratings/1/1", Rating.class);
    }
    @Test
    public void testAddNewMovie() {
        Movie movie = new Movie("9999", "test", "action", null);
        ResponseEntity<Movie> response = restTemplate.postForEntity("http://localhost:" + port + "/movies", movie, Movie.class);
    }
    @Test
    public void testAddNewRating() {
        Rating rating = new Rating("1", "2", 3.0, "987654321");
        ResponseEntity<Rating> response = restTemplate.postForEntity("http://localhost:" + port + "/ratings", rating, Rating.class);
    }
    @Test
    public void testAddNewUser() {
        User user = new User("9876", "F", "1", "10", "default_username", "default_password", new HashMap<>());
        ResponseEntity<User> response = restTemplate.postForEntity("http://localhost:" + port + "/ratings", user, User.class);
    }
    @Test
    public void testUpdateMovie() {
        Movie movie = new Movie("1", "test", "action", null);
        HttpEntity<Movie> putMovie = new HttpEntity<>(movie);
        ResponseEntity<Movie> response = restTemplate.exchange("http://localhost:" + port + "/movies", HttpMethod.PUT, putMovie, Movie.class);
    }
    @Test
    public void testUpdateRating() {
        Rating rating = new Rating("1", "1", 3.0, "987654321");
        HttpEntity<Rating> putRating = new HttpEntity<>(rating);
        ResponseEntity<Rating> response = restTemplate.exchange("http://localhost:" + port + "/ratings", HttpMethod.PUT, putRating, Rating.class);
    }
    @Test
    public void testUpdateUser() {
        User user = new User("1", "F", "1", "10", "default_username", "default_password", new HashMap<>());
        HttpEntity<User> putUser = new HttpEntity<>(user);
        ResponseEntity<User> response = restTemplate.exchange("http://localhost:" + port + "/ratings", HttpMethod.PUT, putUser, User.class);
    }
    @Test
    public void testGetMoviesByRating() {
        ResponseEntity<List<Movie>> response = restTemplate.exchange(
                "http://localhost:" + port + "/ratings/5",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {}
        );
    }
    @Test
    public void testGetRecommendationByMovieId() {
        ResponseEntity<List<Movie>> response = restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/movie/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {}
        );
    }
    @Test
    public void testGetRecommendationByInfo() {
        ResponseEntity<List<Movie>> response = restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/info/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {}
        );
    }
    @Test
    public void testGetRecommendationBySeason() {
        ResponseEntity<List<Movie>> response = restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/season",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {}
        );
    }
    @Test
    public void testGetRecommendationByTime() {
        ResponseEntity<List<Movie>> response = restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/time",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {}
        );
    }
    @Test
    public void testGetSearching() {
        ResponseEntity<List<Movie>> response = restTemplate.exchange(
                "http://localhost:" + port + "/searching/1/toy",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {}
        );
    }
}
