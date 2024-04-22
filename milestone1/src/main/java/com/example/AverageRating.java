package com.example;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AverageRating {
    private double averageRating;
    private String movieId;

    public AverageRating(double averageRating, String movieId) {
        this.averageRating = averageRating;
        this.movieId = movieId;
    }

}
