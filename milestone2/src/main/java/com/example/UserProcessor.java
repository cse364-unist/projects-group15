package com.example;

import java.util.ArrayList;
import org.springframework.batch.item.ItemProcessor;

public class UserProcessor implements ItemProcessor<UserModel, User> {

    @Override
    public User process(final UserModel userModel) {
        final String userId = userModel.userId();
        final String gender = userModel.gender();
        final String age = userModel.age();
        final String occupation = userModel.occupation();
        final String userName = null;
        final String password = "default";
        final ArrayList<ArrayList<String>> movieList = new ArrayList<>();

        final User transformedUser = new User(userId, gender, age, occupation, userName, password, movieList);

        return transformedUser;
    }

}
