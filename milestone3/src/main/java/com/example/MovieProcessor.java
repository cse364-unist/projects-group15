package com.example;

import java.util.List;
import java.util.ArrayList;
import org.springframework.batch.item.ItemProcessor;

public class MovieProcessor implements ItemProcessor<MovieModel, Movie> {

	@Override
	public Movie process(final MovieModel movieModel) {
		final String movieId = movieModel.movieId();
		final String title = movieModel.title();
		final String genre = movieModel.genre();
		final List<String> keywords = new ArrayList<String>();

		final Movie transformedMovie = new Movie(movieId, title, genre, keywords);
		return transformedMovie;
	}
}
