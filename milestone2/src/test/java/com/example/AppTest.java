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

package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    @Test
    public void testGetUser() {
        User user = new User("1", "F", "1", "10", "default_username", "default_password", new HashMap<>());
        ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
        System.out.println(response.getBody().getUserId());
        assertEquals(user.getUserId(), response.getBody().getUserId());
    }

    // User Function
    @Test
    public void testPasswordMatchTrue() {
        User user = new User("1", "F", "1", "10", "default_username", "default_password", new HashMap<>());
        ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1/default_password", User.class);
        System.out.println(response.getBody().getUserId());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testPasswordMatchFalse() {
        ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1/default_passwor", User.class);
        System.out.println(response.getBody().getUserId());
        assertEquals(new RuntimeException("Password do not match"), response.getBody());
    }

    @Test
    public void testPasswordUpdateOk() {
        User user = new User("1", "F", "1", "10", "default_username", "123", new HashMap<>());
        try {
            restTemplate.put("http://localhost:" + port + "/users/password/1/default_password", "123");
        } finally {
            ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
            assertEquals(user.getPassword(), response.getBody().getPassword());
        }
    }

    @Test
    public void testPasswordUpdatePasswordNotMatch() {
        User user = new User("1", "F", "1", "10", "default_username", "default_password", new HashMap<>());
        try {
            restTemplate.put("http://localhost:" + port + "/users/password/1/default_passwor", "123");
        } finally {
            ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
            assertEquals(user, response.getBody());
        }
    }

    @Test
    public void testPasswordUpdateInvalidID() {
        ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/99999", User.class);
        assertEquals(new RuntimeException("Invalid Id"), response.getBody());
    }

    @Test
    public void testChangeUserInfoGenderOk() {
        User user = new User("1", "M", "1", "10", "default_username", "default_password", new HashMap<>());
        try {
            restTemplate.put("http://localhost:" + port + "/users/info/1/gender", "M");
        } finally {
            ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
            assertEquals(user, response.getBody());
        }
    }

    @Test
    public void testChangeUserInfoGenderInvalid() {
        User user = new User("1", "F", "1", "10", "default_username", "default_password", new HashMap<>());
        try {
            restTemplate.put("http://localhost:" + port + "/users/info/1/gender", "A");
        } finally {
            ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
            assertEquals(user, response.getBody());
        }
    }

    @Test
    public void testChangeUserInfoAgeOk() {
        User user = new User("1", "F", "1", "25", "default_username", "default_password", new HashMap<>());
        try {
            restTemplate.put("http://localhost:" + port + "/users/info/1/age", "25");
        } finally {
            ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
            assertEquals(user, response.getBody());
        }
    }

    @Test
    public void testChangeUserInfoAgeInvalid() {
        User user = new User("1", "F", "1", "10", "default_username", "default_password", new HashMap<>());
        try {
            restTemplate.put("http://localhost:" + port + "/users/info/1/age", "9999");
        } finally {
            ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
            assertEquals(user, response.getBody());
        }
    }

    @Test
    public void testChangeUserInfoOccupationOk() {
        User user = new User("1", "F", "1", "0", "default_username", "default_password", new HashMap<>());
        try {
            restTemplate.put("http://localhost:" + port + "/users/info/1/occupation", "0");
        } finally {
            ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
            assertEquals(user, response.getBody());
        }
    }

    @Test
    public void testChangeUserInfoOccupationInvalid() {
        User user = new User("1", "F", "1", "10", "default_username", "default_password", new HashMap<>());
        try {
            restTemplate.put("http://localhost:" + port + "/users/info/1/occupation", "50");
        } finally {
            ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
            assertEquals(user, response.getBody());
        }
    }

    @Test
    public void testChangeUserInfoUsernameOk() {
        User user = new User("1", "F", "1", "0", "123", "default_password", new HashMap<>());
        try {
            restTemplate.put("http://localhost:" + port + "/users/info/1/username", "123");
        } finally {
            ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
            assertEquals(user, response.getBody());
        }
    }

    @Test
    public void testChangeUserInfoIDInvalid() {
        User user = new User("1", "F", "1", "10", "default_username", "default_password", new HashMap<>());
        try {
            restTemplate.put("http://localhost:" + port + "/users/info/99999/gender", "M");
        } finally {
            ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
            assertEquals(user, response.getBody());
        }
    }

    @Test
    public void testAddMovieListOk() {
        HashMap<String, List<String>> tmpList = new HashMap<>();
        tmpList.put("123", new ArrayList<String>());
        User user = new User("1", "F", "1", "10", "default_username", "default_password", tmpList);
        try {
            restTemplate.put("http://localhost:" + port + "/users/movielist/1", "123");
        } finally {
            ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
            assertEquals(user, response.getBody());
        }
    }

    @Test
    public void testAddMovieListInvalidID() {
        User user = new User("1", "F", "1", "10", "default_username", "default_password", new HashMap<>());
        try {
            restTemplate.put("http://localhost:" + port + "/users/movielist/99999", "123");
        } finally {
            ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
            assertEquals(user, response.getBody());
        }
    }

    @Test
    public void testAddElementToMovieListOk() {
        HashMap<String, List<String>> tmpList = new HashMap<>();
        List<String> tmpList2 = new ArrayList<String>();
        tmpList2.add("456");
        tmpList.put("123", tmpList2);
        User user = new User("1", "F", "1", "10", "default_username", "default_password", tmpList);
        try {
            restTemplate.put("http://localhost:" + port + "/users/movielist/1/123", "456");
        } finally {
            ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
            assertEquals(user, response.getBody());
        }
    }

    @Test
    public void testAddElementToMovieListInvalidID() {
        User user = new User("1", "F", "1", "10", "default_username", "default_password", new HashMap<>());
        try {
            restTemplate.put("http://localhost:" + port + "/users/movielist/9999/123", "123");
        } finally {
            ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
            assertEquals(user, response.getBody());
        }
    }

    @Test
    public void testRemoveFromMovieListOk() {
        User user = new User("1", "F", "1", "10", "default_username", "default_password", new HashMap<>());
        try {
            restTemplate.put("http://localhost:" + port + "/users/movielist/9999/123/0", "123");
        } finally {
            ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
            assertEquals(user, response.getBody());
        }
    }

    @Test
    public void testRemoveFromMovieListInvalidUser() {
        HashMap<String, List<String>> tmpList = new HashMap<>();
        tmpList.put("123", new ArrayList<String>());
        User user = new User("1", "F", "1", "10", "default_username", "default_password", tmpList);
        try {
            restTemplate.put("http://localhost:" + port + "/users/movielist/9999/123/0", "123");
        } finally {
            ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/1", User.class);
            assertEquals(user, response.getBody());
        }
    }

    //End
}
