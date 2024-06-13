package com.example;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    @Test
    public void testGetMovieValid() {
        ResponseEntity<getMovieDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/movies/3",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<getMovieDTO>() {
                }
        );
        getMovieDTO movie = response.getBody();
        assertEquals("Grumpier Old Men (1995)", movie.getMovieName(), "Movie title does not match");
    }
    @Test
    public void testGetMovieInvalid() {
        ResponseEntity<getMovieDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/movies/12345",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<getMovieDTO>() {}
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expected 404 NOT FOUND status");
    }
    @Test
    public void testGetUserValid() {
        ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
        User user = response.getBody();
        assertEquals("1", user.getUserId(), "User id does not match");
    }
    @Test
    public void testGetUserInvalid() {
        ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/10000", User.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expected 404 NOT FOUND status");
    }
    @Test
    public void testGetRatingValid() {
        ResponseEntity<Rating> response = restTemplate.getForEntity("http://localhost:" + port + "/ratings/1/1", Rating.class);
        Rating rating = response.getBody();
        assertEquals("1", rating.getUserId(), "User id does not match");
    }
    @Test
    public void testGetRatingInvalid() {
        ResponseEntity<Rating> response = restTemplate.getForEntity("http://localhost:" + port + "/ratings/10000/1", Rating.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expected 404 NOT FOUND status");
    }
    @Test
    public void testAddNewMovieValid() {
        Movie movie = new Movie("9999", "test (1234)", "action", null);
        ResponseEntity<Movie> response = restTemplate.postForEntity("http://localhost:" + port + "/movies", movie, Movie.class);
        Movie movie2 = response.getBody();
        assertEquals("9999", movie2.getMovieId(), "Movie id does not match");
    }
    @Test
    public void testAddNewMovieInvalid() {
        Movie movie = new Movie("1", "test (1234)", "action", null);
        ResponseEntity<Movie> response = restTemplate.postForEntity("http://localhost:" + port + "/movies", movie, Movie.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expected 404 NOT FOUND status");
    }
    @Test
    public void testAddNewRatingValid() {
        Rating rating = new Rating("1", "2", 3.0, "987654321");
        ResponseEntity<Rating> response = restTemplate.postForEntity("http://localhost:" + port + "/ratings", rating, Rating.class);
        Rating rating2 = response.getBody();
        assertEquals("2", rating2.getMovieId(), "Movie id not match");
    }
    @Test
    public void testAddNewRatingInvalid() {
        Rating rating2 = new Rating("15151", "2", 3.0, "987654321");
        Rating rating3 = new Rating("1", "22255", 3.0, "987654321");
        Rating rating4 = new Rating("1", "2", 6.0, "987654321");
        ResponseEntity<Rating> response = restTemplate.postForEntity("http://localhost:" + port + "/ratings", rating2, Rating.class);
        restTemplate.postForEntity("http://localhost:" + port + "/ratings", rating3, Rating.class);
        restTemplate.postForEntity("http://localhost:" + port + "/ratings", rating4, Rating.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expected 404 NOT FOUND status");
    }
    @Test
    public void testAddNewUserValid() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userId", "9898");
        map.add("username", "default_username");
        map.add("password", "default_password");
        map.add("gender", "F");
        map.add("age", "1");
        map.add("occupation", "10");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<User> response = restTemplate.postForEntity("http://localhost:" + port + "/users", request, User.class);
        User user2 = response.getBody();
        assertEquals("9898", user2.getUserId(), "User id does not match");
    }
    @Test
    public void testAddNewUserInvalid() {
        // Assuming "1" is an existing userId in the database
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userId", "1");  // Duplicate userId
        map.add("username", "newUser");
        map.add("password", "newPassword");
        map.add("gender", "M");
        map.add("age", "25");
        map.add("occupation", "5");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        // Send the POST request
        ResponseEntity<User> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/users",
                request,
                User.class
        );

        // Check for HTTP Status 409 CONFLICT or similar status indicating a conflict/error
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expected 404 CONFLICT status due to duplicate userId");
    }
    @Test
    public void testUpdateMovieValid() {
        Movie movie = new Movie("1", "test (1234)", "action", null);
        restTemplate.put("http://localhost:" + port + "/movies/1", movie);
        ResponseEntity<getMovieDTO> response = restTemplate.getForEntity("http://localhost:" + port + "/movies/1", getMovieDTO.class);
        getMovieDTO movie2 = response.getBody();
        assertEquals("test (1234)", movie2.getMovieName(), "Movie title does not match");
    }
    @Test
    public void testUpdateMovieInvalid() {
        Movie movie = new Movie("21212", "test", "action", null);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Movie> requestEntity = new HttpEntity<>(movie, headers);
        ResponseEntity<getMovieDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/movies/21212",
                HttpMethod.PUT,
                requestEntity,
                getMovieDTO.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expected 404 NOT FOUND status");
    }
    @Test
    public void testUpdateRatingValid() {
        restTemplate.put("http://localhost:" + port + "/ratings/1/1", "3");
        ResponseEntity<Rating> response = restTemplate.getForEntity("http://localhost:" + port + "/ratings/1/1", Rating.class);
        Rating rating = response.getBody();
        assertEquals(3.0, rating.getRating(), "Rating does not match");
    }
    @Test
    public void testUpdateRatingInvalid() {
        HttpHeaders headers1 = new HttpHeaders();
        HttpEntity<String> requestEntity1 = new HttpEntity<>("3", headers1);
        ResponseEntity<Rating> response1 = restTemplate.exchange(
                "http://localhost:" + port + "/ratings/12321/1",
                HttpMethod.PUT,
                requestEntity1,
                Rating.class);
        assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode(), "Expected 404 NOT FOUND status");

        HttpHeaders headers2 = new HttpHeaders();
        HttpEntity<String> requestEntity2 = new HttpEntity<>("6", headers2);
        ResponseEntity<Rating> response2 = restTemplate.exchange(
                "http://localhost:" + port + "/ratings/1/1",
                HttpMethod.PUT,
                requestEntity2,
                Rating.class);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode(), "Expected 404 NOT FOUND status");
    }//here
    @Test
    public void testUpdateUserValid() {
        User user1 = new User("1", "F", "1", "10", "default_username", "default_password", new HashMap<>());
        restTemplate.put("http://localhost:" + port + "/users/1", user1);
        ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
        User user2 = response.getBody();
        assertEquals("1", user2.getUserId(), "User id does not match");
    }
    @Test
    public void testUpdateUserInvalid() {
        User user2 = new User("99987", "F", "1", "10", "default_username", "default_password", new HashMap<>());

        HttpHeaders headers1 = new HttpHeaders();
        HttpEntity<User> requestEntity1 = new HttpEntity<>(user2, headers1);
        ResponseEntity<User> response1 = restTemplate.exchange(
                "http://localhost:" + port + "/users/1",
                HttpMethod.PUT,
                requestEntity1,
                User.class);
        assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode(), "Expected 404 NOT FOUND status");

        HttpHeaders headers2 = new HttpHeaders();
        HttpEntity<User> requestEntity2 = new HttpEntity<>(user2, headers2);
        ResponseEntity<User> response2 = restTemplate.exchange(
                "http://localhost:" + port + "/users/99987",
                HttpMethod.PUT,
                requestEntity2,
                User.class);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode(), "Expected 404 NOT FOUND status");
    }
    @Test
    public void testGetMoviesByRatingValid() {
        ResponseEntity<List<Movie>> response = restTemplate.exchange(
                "http://localhost:" + port + "/ratings/5",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
        assertNotNull(response.getBody());
    }
    @Test
    public void testGetMoviesByRatingInvalid() {
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/ratings/6",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expected 404 NOT FOUND status");
    }
    @Test
    public void testGetRecommendationByMovieIdValid() {
        ResponseEntity<List<Movie>> response = restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/movie/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
        assertNotNull(response.getBody());
    }
    @Test
    public void testGetRecommendationByMovieIdInvalid() {
        ResponseEntity<List<Movie>> response = restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/movie/91",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
        assertNull(response.getBody());
    }
    @Test
    public void testGetRecommendationByInfoValid() {
        ResponseEntity<List<Movie>> response = restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/info/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
        assertNotNull(response.getBody());
    }
    @Test
    public void testGetRecommendationByInfoInvalid() {
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/info/15771",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expected 404 NOT FOUND status");
    }//here
    @Test
    public void testGetRecommendationBySeasonValid() {
        ResponseEntity<List<Movie>> response = restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/season/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
        restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/season/4",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
        restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/season/7",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
        restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/season/10",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
        assertNotNull(response.getBody());
    }
    @Test
    public void testGetRecommendationBySeasonInvalid() {
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/season/13",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expected 404 NOT FOUND status");
    }
    @Test
    public void testGetRecommendationByTimeValid() {
        ResponseEntity<List<Movie>> response = restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/time/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
        restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/time/7",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
        restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/time/13",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
        restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/time/19",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
        assertNotNull(response.getBody());
    }
    @Test
    public void testGetRecommendationByTimeInvalid() {
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/recommendation/time/123",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expected 404 NOT FOUND status");
    }
    @Test
    public void testGetSearchingCase1() {
        ResponseEntity<List<SearchDTO>> response = restTemplate.exchange(
                "http://localhost:" + port + "/searching/1/toy?containingGenres=animation&filteringGenres=drama",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SearchDTO>>() {
                }
        );
        assertNotNull(response);
    }
    @Test
    public void testGetSearchingCase2() {
        ResponseEntity<List<SearchDTO>> response = restTemplate.exchange(
                "http://localhost:" + port + "/searching/1/toy?containingLists=123&filteringLists=456",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SearchDTO>>() {
                }
        );
        assertNotNull(response);
    }
    @Test
    public void testPasswordMatchValid() {
        ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1/default_password", User.class);
        assertEquals("default_password", response.getBody().getPassword());
    }
    @Test
    public void testPasswordMatchInvalid() {
        ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/10000/default_password", User.class);
        restTemplate.getForEntity("http://localhost:" + port + "/users/1/false_password", User.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expected 404 NOT FOUND status");
    }
    @Test
    public void testPasswordUpdateValid() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>("123", headers);
        ResponseEntity<User> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/password/1/default_password",
                HttpMethod.PUT,
                requestEntity,
                User.class);
        assertEquals("123", response.getBody().getPassword());
    }
    @Test
    public void testPasswordUpdateInvalid() {
        HttpHeaders headers1 = new HttpHeaders();
        HttpEntity<String> requestEntity1 = new HttpEntity<>("123", headers1);
        ResponseEntity<User> response1 = restTemplate.exchange(
                "http://localhost:" + port + "/users/password/10000/default_password",
                HttpMethod.PUT,
                requestEntity1,
                User.class);
        assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode(), "Expected 404 NOT FOUND status");

        HttpHeaders headers2 = new HttpHeaders();
        HttpEntity<String> requestEntity2 = new HttpEntity<>("123", headers2);
        ResponseEntity<User> response2 = restTemplate.exchange(
                "http://localhost:" + port + "/users/password/1/false_password",
                HttpMethod.PUT,
                requestEntity2,
                User.class);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode(), "Expected 404 NOT FOUND status");
    }
    @Test
    public void testChangeUserInfoValid() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>("M", headers);
        ResponseEntity<User> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/info/1/gender",
                HttpMethod.PUT,
                requestEntity,
                User.class);
        assertEquals("M", response.getBody().getGender());

        restTemplate.put("http://localhost:" + port + "/users/info/1/age", "25");
        restTemplate.put("http://localhost:" + port + "/users/info/1/occupation", "0");
        restTemplate.put("http://localhost:" + port + "/users/info/1/username", "123");
    }
    @Test
    public void testChangeUserInfoInvalid() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>("A", headers);
        ResponseEntity<User> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/info/1/gender",
                HttpMethod.PUT,
                requestEntity,
                User.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expected 404 NOT FOUND status");

        restTemplate.put("http://localhost:" + port + "/users/info/1/age", "9999");
        restTemplate.put("http://localhost:" + port + "/users/info/1/occupation", "0");
        restTemplate.put("http://localhost:" + port + "/users/info/1/false", "123");
        restTemplate.put("http://localhost:" + port + "/users/info/99999/gender", "M");
    }
    @Test
    @Order(1)
    public void testAddMovieListValid() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>("123", headers);
        ResponseEntity<User> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/movielist/1",
                HttpMethod.PUT,
                requestEntity,
                User.class);
        assertTrue(response.getBody().getMovieList().containsKey("123"));
    }
    @Test
    @Order(2)
    public void testAddMovieListInvalid() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>("123", headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/movielist/99999",
                HttpMethod.PUT,
                requestEntity,
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expected 404 NOT FOUND status");
    }
    @Test
    @Order(3)
    public void testAddElementToMovieListValid() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>("456", headers);
        ResponseEntity<User> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/movielist/1/123",
                HttpMethod.PUT,
                requestEntity,
                User.class);
        assertTrue(response.getBody().getMovieList().get("123").contains("456"));
    }
    @Test
    @Order(4)
    public void testAddElementToMovieListInvalid() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>("456", headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/movielist/9999/123",
                HttpMethod.PUT,
                requestEntity,
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expected 404 NOT FOUND status");
    }
    @Test
    @Order(5)
    public void testRemoveFromMovieListValid() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>("456", headers);
        ResponseEntity<User> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/movielist/1/123/0",
                HttpMethod.PUT,
                requestEntity,
                User.class);
        assertEquals(0, response.getBody().getMovieList().get("123").size());
    }
    @Test
    @Order(6)
    public void testRemoveFromMovieListInvalid() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>("456", headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/users/movielist/9999/123/0",
                HttpMethod.PUT,
                requestEntity,
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expected 404 NOT FOUND status");
    }
}