package com.example;

import java.util.List;

public interface MovieDAL {
    List<Movie> getMovieInfosByMovieId(List<String> movieIds);
    boolean checkMovieIdExists(String movieId);
}
