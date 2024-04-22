package com.example;

import org.springframework.batch.item.ItemProcessor;

public class MovieProcessor implements ItemProcessor<MovieModel, Movie> {

	@Override
	public Movie process(final MovieModel movieModel) {
		final String movieId = movieModel.movieId();
		final String title = movieModel.title();
		final String genre = movieModel.genre();

		final Movie transformedMovie = new Movie(movieId, title, genre);
		return transformedMovie;
	}

}
