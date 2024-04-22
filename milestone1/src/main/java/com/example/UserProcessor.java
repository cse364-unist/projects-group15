package com.example;

import org.springframework.batch.item.ItemProcessor;

public class UserProcessor implements ItemProcessor<UserModel, User> {

    @Override
    public User process(final UserModel userModel) {
        final String userId = userModel.userId();
        final String gender = userModel.gender();
        final String age = userModel.age();
        final String occupation = userModel.occupation();
        final String zipcode = userModel.zipcode();

        final User transformedUser = new User(userId, gender, age, occupation, zipcode);

        return transformedUser;
    }

}
