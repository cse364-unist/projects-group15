package com.example;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserMovies {
    private String userId;
    private Set<String> movieIds;
}
