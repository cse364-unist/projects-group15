package com.example;

import java.util.List;

public interface RatingDAL {
    List<String> getMovieIdsByRating(Double rating);
    Rating getRating(String userId, String movieId);
    boolean checkUserIdAndMovieIdExist(String userId, String movieId);

    String getIdByUserIdAndMovieId(String userId, String movieId);

    List<Rating> getUserbyMovieId(String movieId);
}
