package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MovieRepositoryService {
    @Autowired
    private MovieRepository movieRepository;

    public void createKeywords() {
        for (Movie movie : movieRepository.findAll()) {
            List<String> keywords = new ArrayList<>(List.of(movie.getTitle().split(" ")));
            keywords.addAll(List.of(movie.getGenre().split("\\|")));
            movie.setKeywords(keywords);
            movieRepository.save(movie);
        }
    }
}
