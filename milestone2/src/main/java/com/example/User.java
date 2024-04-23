package com.example;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Getter
@Setter
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id private String userId;
    private String gender;
    private String age;
    private String occupation;
    private String userName;
    private String password;
    private ArrayList<ArrayList<String>> movieList;
}
