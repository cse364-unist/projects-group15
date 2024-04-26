package com.example;

public interface UserDAL {
    boolean checkUserIdExists(String userId);

    User getUserbyUserId(String userId);

    Integer classifyAge(String age);

    Integer classifyOccupation(String occupation);
}
