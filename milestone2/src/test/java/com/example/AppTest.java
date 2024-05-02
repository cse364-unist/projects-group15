package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    @Test
    public void testGetMovie() {
        restTemplate.exchange(
                "http://localhost:" + port + "/movies/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<getMovieDTO>() {
                }
        );
        restTemplate.exchange(
                "http://localhost:" + port + "/movies/12345",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<getMovieDTO>() {
                }
        );
    }
    @Test
    public void testGetUser() {
        restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
        restTemplate.getForEntity("http://localhost:" + port + "/users/10000", User.class);
    }
    @Test
    public void testGetRating() {
        restTemplate.getForEntity("http://localhost:" + port + "/ratings/1/1", Rating.class);
        restTemplate.getForEntity("http://localhost:" + port + "/ratings/10000/1", Rating.class);
    }
    @Test
    public void testAddNewMovie() {
        Movie movie1 = new Movie("1", "test", "action", null);
        Movie movie2 = new Movie("9999", "test", "action", null);
        restTemplate.postForEntity("http://localhost:" + port + "/movies", movie1, Movie.class);
        restTemplate.postForEntity("http://localhost:" + port + "/movies", movie2, Movie.class);
    }
    @Test
    public void testAddNewRating() {
        Rating rating1 = new Rating("1", "2", 3.0, "987654321");
        Rating rating2 = new Rating("15151", "2", 3.0, "987654321");
        Rating rating3 = new Rating("1", "2", 6.0, "987654321");
        restTemplate.postForEntity("http://localhost:" + port + "/ratings", rating1, Rating.class);
        restTemplate.postForEntity("http://localhost:" + port + "/ratings", rating2, Rating.class);
        restTemplate.postForEntity("http://localhost:" + port + "/ratings", rating3, Rating.class);
    }
    @Test
    public void testAddNewUser() {
        User user1 = new User("1", "F", "1", "10", "default_username", "default_password", new HashMap<String, List<String>>());
        User user2 = new User("9898", "F", "1", "10", "default_username", "default_password", new HashMap<String, List<String>>());
        restTemplate.postForEntity("http://localhost:" + port + "/users", user1, User.class);
        restTemplate.postForEntity("http://localhost:" + port + "/users", user2, User.class);
    }
    @Test
    public void testUpdateMovie() {
        Movie movie = new Movie("1", "test", "action", null);
        restTemplate.put("http://localhost:" + port + "/movies", movie);
    }
    @Test
    public void testUpdateRating() {
        Rating rating = new Rating("1", "1", 3.0, "987654321");
        restTemplate.put("http://localhost:" + port + "/ratings", rating);
    }
    @Test
    public void testUpdateUser() {
        User user = new User("1", "F", "1", "10", "default_username", "default_password", new HashMap<>());
        restTemplate.put("http://localhost:" + port + "/users", user);
    }
    @Test
    public void testGetMoviesByRating() {
        restTemplate.exchange(
                "http://localhost:" + port + "/ratings/5",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
    }
    @Test
    public void testGetRecommendationByMovieId() {
        restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/movie/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
        restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/movie/91",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
    }
    @Test
    public void testGetRecommendationByInfo() {
        restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/info/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
    }
    @Test
    public void testGetRecommendationBySeason() {
        restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/season",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
    }
    @Test
    public void testGetRecommendationByTime() {
        restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/time",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
    }
    @Test
    public void testGetSearching() {
        restTemplate.exchange(
                "http://localhost:" + port + "/searching/1/toy?containingGenres=animation&filteringGenres=drama",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SearchDTO>>() {
                }
        );
    }
    @Test
    public void testPasswordMatch() {
        restTemplate.getForEntity("http://localhost:" + port + "/users/1/default_password", User.class);
        restTemplate.getForEntity("http://localhost:" + port + "/users/10000/default_password", User.class);
        restTemplate.getForEntity("http://localhost:" + port + "/users/1/false_password", User.class);
    }
    @Test
    public void testPasswordUpdate() {
        restTemplate.put("http://localhost:" + port + "/users/password/1/default_password", "123");
        restTemplate.put("http://localhost:" + port + "/users/password/10000/default_password", "123");
        restTemplate.put("http://localhost:" + port + "/users/password/1/false_password", "123");
    }
    @Test
    public void testChangeUserInfo() {
        restTemplate.put("http://localhost:" + port + "/users/info/1/gender", "M");
        restTemplate.put("http://localhost:" + port + "/users/info/1/gender", "A");
        restTemplate.put("http://localhost:" + port + "/users/info/1/age", "25");
        restTemplate.put("http://localhost:" + port + "/users/info/1/age", "9999");
        restTemplate.put("http://localhost:" + port + "/users/info/1/occupation", "0");
        restTemplate.put("http://localhost:" + port + "/users/info/1/occupation", "50");
        restTemplate.put("http://localhost:" + port + "/users/info/1/username", "123");
        restTemplate.put("http://localhost:" + port + "/users/info/1/false", "123");
        restTemplate.put("http://localhost:" + port + "/users/info/99999/gender", "M");
    }
    @Test
    public void testAddMovieListAndAddElementToMovieListAndRemoveFromMovieList() {
        restTemplate.put("http://localhost:" + port + "/users/movielist/1", "123");
        restTemplate.put("http://localhost:" + port + "/users/movielist/99999", "123");
        restTemplate.put("http://localhost:" + port + "/users/movielist/1/123", "456");
        restTemplate.put("http://localhost:" + port + "/users/movielist/9999/123", "123");
        restTemplate.put("http://localhost:" + port + "/users/movielist/1/123/0", "456");
        restTemplate.put("http://localhost:" + port + "/users/movielist/9999/123/0", "123");
    }
}