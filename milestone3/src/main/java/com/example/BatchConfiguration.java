package com.example;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfiguration {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public Job job (JobRepository jobRepository,
                    Step movieStep, Step ratingStep, Step userStep,
                    JobCompletionNotificationListener listener) {
        return new JobBuilder("job", jobRepository)
                .listener(listener)
                .start(movieStep)
                .next(ratingStep)
                .next(userStep)
                .build();
    }
    @Bean
    public Step movieStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                          FlatFileItemReader<MovieModel> movieReader,
                          MovieProcessor movieProcessor,
                          MongoItemWriter<Movie> movieWriter) {

        return new StepBuilder("movieStep", jobRepository)
                .<MovieModel, Movie>chunk(100, transactionManager)
                .reader(movieReader)
                .processor(movieProcessor)
                .writer(movieWriter)
                .build();
    }
    @Bean
    public Step ratingStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                           FlatFileItemReader<RatingModel> ratingReader,
                           RatingProcessor ratingProcessor,
                           MongoItemWriter<Rating> ratingWriter) {
        return new StepBuilder("ratingStep", jobRepository)
                .<RatingModel, Rating>chunk(100, transactionManager)
                .reader(ratingReader)
                .processor(ratingProcessor)
                .writer(ratingWriter)
                .build();
    }
    @Bean
    public Step userStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                         FlatFileItemReader<UserModel> userReader,
                         UserProcessor userProcessor,
                         MongoItemWriter<User> userWriter) {
        return new StepBuilder("userStep", jobRepository)
                .<UserModel, User>chunk(100, transactionManager)
                .reader(userReader)
                .processor(userProcessor)
                .writer(userWriter)
                .build();
    }
    // end::jobstep[]
    // tag::readerwriterprocessor[]
    @Bean
    public FlatFileItemReader<MovieModel> movieReader() {
        return new FlatFileItemReaderBuilder<MovieModel>()
                .name("movieReader")
                .resource(new FileSystemResource("data/movies.csv"))
                .delimited().delimiter("::")
                .names("movieId", "title", "genre")
                .targetType(MovieModel.class)
                .build();
    }
    @Bean
    public MovieProcessor movieProcessor() {
        return new MovieProcessor();
    }
    @Bean
    public MongoItemWriter<Movie> movieWriter() {
        MongoItemWriter<Movie> mongoItemWriter = new MongoItemWriter<>();
        mongoItemWriter.setTemplate(mongoTemplate);
        mongoItemWriter.setCollection("movies");
        return mongoItemWriter;
    }
    @Bean
    public FlatFileItemReader<RatingModel> ratingReader() {
        return new FlatFileItemReaderBuilder<RatingModel>()
                .name("ratingReader")
                .resource(new FileSystemResource("data/ratings.csv"))
                .delimited().delimiter("::")
                .names("userId", "movieId", "rating", "timeStamp")
                .targetType(RatingModel.class)
                .build();
    }
    @Bean
    public RatingProcessor ratingProcessor() {
        return new RatingProcessor();
    }
    @Bean
    public MongoItemWriter<Rating> ratingWriter(MongoTemplate mongoTemplate) {
        MongoItemWriter<Rating> mongoItemWriter = new MongoItemWriter<>();
        mongoItemWriter.setTemplate(mongoTemplate);
        mongoItemWriter.setCollection("ratings");
        return mongoItemWriter;
    }
    @Bean
    public FlatFileItemReader<UserModel> userReader() {
        return new FlatFileItemReaderBuilder<UserModel>()
                .name("userReader")
                .resource(new FileSystemResource("data/users.csv"))
                .delimited().delimiter("::")
                .names("userId", "gender", "age", "occupation", "zipcode")
                .targetType(UserModel.class)
                .build();
    }
    @Bean
    public UserProcessor userProcessor() {
        return new UserProcessor();
    }
    @Bean
    public MongoItemWriter<User> userWriter(MongoTemplate mongoTemplate) {
        MongoItemWriter<User> mongoItemWriter = new MongoItemWriter<>();
        mongoItemWriter.setTemplate(mongoTemplate);
        mongoItemWriter.setCollection("users");
        return mongoItemWriter;
    }
    // end::readerwriterprocessor[]
}
