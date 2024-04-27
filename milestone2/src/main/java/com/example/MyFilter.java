package com.example;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class MyFilter {
    public List<String> containingGenres;
    public List<String> filteringGenres;
    public List<String> containingLists;
    public List<String> filteringLists;

    public MyFilter(String containingGenres, String filteringGenres, String containingLists, String filteringLists) {
        this.containingGenres = Objects.equals(containingGenres, "") ? null : List.of(containingGenres.split("\\|"));
        this.filteringGenres = Objects.equals(filteringGenres, "") ? null : List.of(filteringGenres.split("\\|"));
        this.containingLists = Objects.equals(containingLists, "") ? null : List.of(containingLists.split("\\|"));
        this.filteringLists = Objects.equals(filteringLists, "") ? null : List.of(filteringLists.split("\\|"));
    }
}
