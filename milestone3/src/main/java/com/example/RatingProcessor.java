package com.example;

import org.springframework.batch.item.ItemProcessor;

public class RatingProcessor implements ItemProcessor<RatingModel, Rating> {

    @Override
    public Rating process(final RatingModel ratingModel) {
        final String userId = ratingModel.userId();
        final String movieId = ratingModel.movieId();
        final double rating = ratingModel.rating();
        final String timeStamp = ratingModel.timeStamp();

        final Rating transformedRating = new Rating(userId, movieId, rating, timeStamp);

        return transformedRating;
    }

}
