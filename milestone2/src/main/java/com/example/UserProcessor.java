package com.example;

import java.util.List;
import org.springframework.batch.item.ItemProcessor;

public class UserProcessor implements ItemProcessor<UserModel, User> {

    @Override
    public User process(final UserModel userModel) {
        final String userId = userModel.userId();
        final String password = userModel.password();
        final String gender = userModel.gender();
        final String age = userModel.age();
        final String occupation = userModel.occupation();
        final List<List<String>> movieList = userModel.movieList();

        final User transformedUser = new User(userId, password, gender, age, occupation, movieList);

        return transformedUser;
    }

}
