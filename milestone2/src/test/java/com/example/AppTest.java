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
