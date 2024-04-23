package com.example;

import java.util.List;

public record UserModel(String userId, String password, String gender, String age, String occupation, String zipcode, List<List<String>> movieList) {

}
