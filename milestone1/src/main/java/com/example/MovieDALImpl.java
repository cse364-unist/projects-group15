package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovieDALImpl implements MovieDAL{
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public List<Movie> getMovieInfosByMovieId(List<String> movieIds) {
        Query query = Query.query(Criteria.where("movieId").in(movieIds));
        return mongoTemplate.find(query, Movie.class);
    }
    @Override
    public boolean checkMovieIdExists(String movieId) {
        Query query = Query.query(Criteria.where("movieId").is(movieId));
        return mongoTemplate.exists(query, Movie.class);
    }
}
