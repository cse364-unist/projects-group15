package com.example;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {
    @Aggregation(pipeline = {
            "{ $addFields: { 'month': { $month: { $toDate: '$timestamp' } } } }",
            "{ $match: { $or: [ { 'month': { $gte: 1, $lt: 3 } }, { 'month': 12 } ] } }",
            "{ $group: { _id: '$movieId', count: { $sum: 1 } } }",
            "{ $match: { 'count': { $gte: 5 } } }",
            "{ $project: { movieId: '$_id', _id: 0 } }"
    })
    List<String> getRecommendationWinter();
    @Aggregation(pipeline = {
            "{ $addFields: { 'month': { $month: { $toDate: '$timestamp' } } } }",
            "{ $match: { 'month': { $gte: 3, $lt: 6 } } }",
            "{ $group: { _id: '$movieId', count: { $sum: 1 } } }",
            "{ $match: { 'count': { $gte: 5 } } }",
            "{ $project: { movieId: '$_id', _id: 0 } }"
    })
    List<String> getRecommendationSpring();
    @Aggregation(pipeline = {
            "{ $addFields: { 'month': { $month: { $toDate: '$timestamp' } } } }",
            "{ $match: { 'month': { $gte: 6, $lt: 9 } } }",
            "{ $group: { _id: '$movieId', count: { $sum: 1 } } }",
            "{ $match: { 'count': { $gte: 5 } } }",
            "{ $project: { movieId: '$_id', _id: 0 } }"
    })
    List<String> getRecommendationSummer();
    @Aggregation(pipeline = {
            "{ $addFields: { 'month': { $month: { $toDate: '$timestamp' } } } }",
            "{ $match: { 'month': { $gte: 9, $lt: 12 } } }",
            "{ $group: { _id: '$movieId', count: { $sum: 1 } } }",
            "{ $match: { 'count': { $gte: 5 } } }",
            "{ $project: { movieId: '$_id', _id: 0 } }"
    })
    List<String> getRecommendationFall();
}
