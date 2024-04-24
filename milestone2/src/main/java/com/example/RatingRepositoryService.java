package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingRepositoryService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<UserMovies> groupMoviesByUser() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("userId").addToSet("movieId").as("movieIds")
        ).withOptions(AggregationOptions.builder().allowDiskUse(true).build());

        return mongoTemplate.aggregate(aggregation, "ratings", UserMovies.class).getMappedResults();
    }
}
