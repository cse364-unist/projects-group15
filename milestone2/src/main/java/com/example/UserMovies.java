package com.example;
import lombok.Getter;

import java.util.Set;

@Getter
public class UserMovies {
    private String userId;
    private Set<String> movieIds;
}
