package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {
    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
    private static RatingRepositoryService ratingRepositoryService = null;
    private static MovieRepositoryService movieRepositoryService = null;
    @Autowired
    public JobCompletionNotificationListener(RatingRepositoryService ratingRepositoryService, MovieRepositoryService movieRepositoryService) {
        JobCompletionNotificationListener.ratingRepositoryService = ratingRepositoryService;
        JobCompletionNotificationListener.movieRepositoryService = movieRepositoryService;
    }
    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("Creating association");
        Controller.associations = ratingRepositoryService.groupMoviesByUser();
        System.out.println(Controller.associations.size());
        for (UserMovies s : Controller.associations)
            for (String e : s.getMovieIds())
                Controller.totalCounter.put(e, Controller.totalCounter.getOrDefault(e, 0) + 1);
        log.info("Movie keywords creating");
        movieRepositoryService.createKeywords();
        if(jobExecution.getStatus() == BatchStatus.COMPLETED)
            log.info("Complete");
    }
}
