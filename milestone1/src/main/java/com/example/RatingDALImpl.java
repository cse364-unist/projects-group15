package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RatingDALImpl implements RatingDAL{
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public List<String> getMovieIdsByRating(Double rating) {

        AggregationResults<AverageRating> result = mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        Aggregation.group("movieId")
                                .first("movieId").as("movieId")
                                .avg("rating").as("averageRating"),
                        Aggregation.match(Criteria.where("averageRating").gte(rating))
                ),
                "ratings",
                AverageRating.class
        );


        return result.getMappedResults().stream()
                .map(AverageRating::getMovieId)
                .collect(Collectors.toList());
    }

    @Override
    public Rating getRating(String userId, String movieId) {
        Query query = Query.query(Criteria.where("userId").is(userId)
                .and("movieId").is(movieId));
        return mongoTemplate.findOne(query, Rating.class);
    }

    @Override
    public boolean checkUserIdAndMovieIdExist(String userId, String movieId) {
        Query query = Query.query(Criteria.where("userId").is(userId)
                .and("movieId").is(movieId));
        return mongoTemplate.exists(query, Rating.class);
    }

    @Override
    public String getIdByUserIdAndMovieId(String userId, String movieId) {
        Query query = Query.query(Criteria.where("userId").is(userId)
                .and("movieId").is(movieId));
        return mongoTemplate.findOne(query, Rating.class).getId();
    }
}
