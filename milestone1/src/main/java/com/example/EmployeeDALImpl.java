package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDALImpl implements EmployeeDAL{
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public boolean checkEmployeeIdExists(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        return mongoTemplate.exists(query, Employee.class);
    }
}
