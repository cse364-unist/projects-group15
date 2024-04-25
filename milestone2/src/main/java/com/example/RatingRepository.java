package com.example;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {
    @Aggregation(pipeline = {
            "{ $addFields: { 'month': { $month: { $toDate: { $multiply: [{ $toLong: '$timeStamp' }, 1000] } } } } }",
            "{ $group: { _id: '$movieId', totalCount: { $sum: 1 }, conditionalCount: { $sum: { $cond: [{ $or: [{ $eq: ['$month', 1] }, { $eq: ['$month', 2] }, { $eq: ['$month', 12] }] }, 1, 0] } } } }",
            "{ $match: { totalCount: { $gte: 10 } } }",
            "{ $addFields: { 'matchRatio': { $divide: ['$conditionalCount', '$totalCount'] } } }",
            "{ $sort: { 'matchRatio': -1 } }",
            "{ $limit: 20 }",
            "{ $project: { movieId: '$_id', _id: 0, matchRatio: 1 } }"
    })
    List<String> getRecommendationWinter();

    @Aggregation(pipeline = {
            "{ $addFields: { 'month': { $month: { $toDate: { $multiply: [{ $toLong: '$timeStamp' }, 1000] } } } } }",
            "{ $group: { _id: '$movieId', totalCount: { $sum: 1 }, conditionalCount: { $sum: { $cond: [{ $and: [{ $gte: ['$month', 3] }, { $lte: ['$month', 5] }] }, 1, 0] } } } }",
            "{ $match: { totalCount: { $gte: 10 } } }",
            "{ $addFields: { 'matchRatio': { $divide: ['$conditionalCount', '$totalCount'] } } }",
            "{ $sort: { 'matchRatio': -1 } }",
            "{ $limit: 20 }",
            "{ $project: { movieId: '$_id', _id: 0, matchRatio: 1 } }"
    })
    List<String> getRecommendationSpring();

    @Aggregation(pipeline = {
            "{ $addFields: { 'month': { $month: { $toDate: { $multiply: [{ $toLong: '$timeStamp' }, 1000] } } } } }",
            "{ $group: { _id: '$movieId', totalCount: { $sum: 1 }, conditionalCount: { $sum: { $cond: [{ $and: [{ $gte: ['$month', 6] }, { $lte: ['$month', 8] }] }, 1, 0] } } } }",
            "{ $match: { totalCount: { $gte: 10 } } }",
            "{ $addFields: { 'matchRatio': { $divide: ['$conditionalCount', '$totalCount'] } } }",
            "{ $sort: { 'matchRatio': -1 } }",
            "{ $limit: 20 }",
            "{ $project: { movieId: '$_id', _id: 0, matchRatio: 1 } }"
    })
    List<String> getRecommendationSummer();

    @Aggregation(pipeline = {
            "{ $addFields: { 'month': { $month: { $toDate: { $multiply: [{ $toLong: '$timeStamp' }, 1000] } } } } }",
            "{ $group: { _id: '$movieId', totalCount: { $sum: 1 }, conditionalCount: { $sum: { $cond: [{ $and: [{ $gte: ['$month', 9] }, { $lte: ['$month', 11] }] }, 1, 0] } } } }",
            "{ $match: { totalCount: { $gte: 10 } } }",
            "{ $addFields: { 'matchRatio': { $divide: ['$conditionalCount', '$totalCount'] } } }",
            "{ $sort: { 'matchRatio': -1 } }",
            "{ $limit: 20 }",
            "{ $project: { movieId: '$_id', _id: 0, matchRatio: 1 } }"
    })
    List<String> getRecommendationFall();

    @Aggregation(pipeline = {
            "{ $addFields: { 'hour': { $hour: { $toDate: { $multiply: [{ $toLong: '$timeStamp' }, 1000] } } } } }",
            "{ $group: { _id: '$movieId', totalCount: { $sum: 1 }, conditionalCount: { $sum: { $cond: [{ $and: [{ $gte: ['$hour', 0] }, { $lte: ['$hour', 6] }] }, 1, 0] } } } }",
            "{ $match: { totalCount: { $gte: 10 } } }",
            "{ $addFields: { 'matchRatio': { $divide: ['$conditionalCount', '$totalCount'] } } }",
            "{ $sort: { 'matchRatio': -1 } }",
            "{ $limit: 20 }",
            "{ $project: { movieId: '$_id', _id: 0, matchRatio: 1 } }"
    })
    List<String> getRecommendationDawn();
    @Aggregation(pipeline = {
            "{ $addFields: { 'hour': { $hour: { $toDate: { $multiply: [{ $toLong: '$timeStamp' }, 1000] } } } } }",
            "{ $group: { _id: '$movieId', totalCount: { $sum: 1 }, conditionalCount: { $sum: { $cond: [{ $and: [{ $gte: ['$hour', 7] }, { $lte: ['$hour', 12] }] }, 1, 0] } } } }",
            "{ $match: { totalCount: { $gte: 10 } } }",
            "{ $addFields: { 'matchRatio': { $divide: ['$conditionalCount', '$totalCount'] } } }",
            "{ $sort: { 'matchRatio': -1 } }",
            "{ $limit: 20 }",
            "{ $project: { movieId: '$_id', _id: 0, matchRatio: 1 } }"
    })
    List<String> getRecommendationMorning();
    @Aggregation(pipeline = {
            "{ $addFields: { 'hour': { $hour: { $toDate: { $multiply: [{ $toLong: '$timeStamp' }, 1000] } } } } }",
            "{ $group: { _id: '$movieId', totalCount: { $sum: 1 }, conditionalCount: { $sum: { $cond: [{ $and: [{ $gte: ['$hour', 13] }, { $lte: ['$hour', 18] }] }, 1, 0] } } } }",
            "{ $match: { totalCount: { $gte: 10 } } }",
            "{ $addFields: { 'matchRatio': { $divide: ['$conditionalCount', '$totalCount'] } } }",
            "{ $sort: { 'matchRatio': -1 } }",
            "{ $limit: 20 }",
            "{ $project: { movieId: '$_id', _id: 0, matchRatio: 1 } }"
    })
    List<String> getRecommendationAfternoon();
    @Aggregation(pipeline = {
            "{ $addFields: { 'hour': { $hour: { $toDate: { $multiply: [{ $toLong: '$timeStamp' }, 1000] } } } } }",
            "{ $group: { _id: '$movieId', totalCount: { $sum: 1 }, conditionalCount: { $sum: { $cond: [{ $and: [{ $gte: ['$hour', 19] }, { $lte: ['$hour', 23] }] }, 1, 0] } } } }",
            "{ $match: { totalCount: { $gte: 10 } } }",
            "{ $addFields: { 'matchRatio': { $divide: ['$conditionalCount', '$totalCount'] } } }",
            "{ $sort: { 'matchRatio': -1 } }",
            "{ $limit: 20 }",
            "{ $project: { movieId: '$_id', _id: 0, matchRatio: 1 } }"
    })
    List<String> getRecommendationNight();
}
