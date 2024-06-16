package com.example;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class getMovieDTO {
    private String movieId;
    private String movieName;
    private String movieGenre;
    private Double averageRating;
    private float[] AgeRatio;
    private float[] OccupationRatio;
    private float[] GenderRatio;
}
