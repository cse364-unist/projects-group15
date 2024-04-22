package com.example;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.management.ConstructorParameters;
import java.beans.ConstructorProperties;

@Getter
@Setter
@Document(collection = "ratings")
public class Rating {
    @Id private String id;
    private String userId;
    private String movieId;
    private Double rating;
    private String timeStamp;

    public Rating(String userId, String movieId, Double rating, String timeStamp) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.timeStamp = timeStamp;
    }
}
