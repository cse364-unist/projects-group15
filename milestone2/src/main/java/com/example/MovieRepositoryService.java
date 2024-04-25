package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieRepositoryService {
    @Autowired
    private MovieRepository movieRepository;

    public void createKeywords() {
        for (int i = 1; i <= movieRepository.count(); i++) {
            Movie movie = movieRepository.findById(String.valueOf(i)).get();
            List<String> keywords = new ArrayList<>(List.of(movie.getTitle().split(" ")));
            keywords.retainAll(List.of(movie.getGenre().split("\\|")));
            movie.setKeywords(keywords);
        }
    }
}
