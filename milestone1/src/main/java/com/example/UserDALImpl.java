package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserDALImpl implements UserDAL{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean checkUserIdExists(String userId) {
        Query query = Query.query(Criteria.where("userId").is(userId));
        return mongoTemplate.exists(query, User.class);
    }
}