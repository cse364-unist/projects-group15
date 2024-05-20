package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UserDALImpl implements UserDAL{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean checkUserIdExists(String userId) {
        Query query = Query.query(Criteria.where("userId").is(userId));
        return mongoTemplate.exists(query, User.class);
    }
    
    @Override
    public User getUserbyUserId(String userId){
            Query query = Query.query(Criteria.where("userId").is(userId));
            return mongoTemplate.findOne(query, User.class);
    }

    @Override
    public Integer classifyAge(String age){
        return switch (age) {
            case "1" -> 0;
            case "18" -> 1;
            case "25" -> 2;
            case "35" -> 3;
            case "45" -> 4;
            case "50" -> 5;
            case "56" -> 6;
            default -> -1;
        };
    }

    @Override
    public Integer classifyOccupation(String occupation){
        return switch (occupation){
            case "0" -> 0;
            case "1" -> 1;
            case "2" -> 2;
            case "3" -> 3;
            case "4" -> 4;
            case "5" -> 5;
            case "6" -> 6;
            case "7" -> 7;
            case "8" -> 8;
            case "9" -> 9;
            case "10" -> 10;
            case "11" -> 11;
            case "12"-> 12;
            case "13" -> 13;
            case "14" -> 14;
            case "15" -> 15;
            case "16" -> 16;
            case "17" -> 17;
            case "18" -> 18;
            case "19" -> 19;
            case "20" -> 20;
            default -> -1;
        };
    }
}
