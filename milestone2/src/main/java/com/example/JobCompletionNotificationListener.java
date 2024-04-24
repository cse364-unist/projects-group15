package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {
    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
    private static RatingRepositoryService ratingRepositoryService = null;
    @Autowired
    public JobCompletionNotificationListener(RatingRepositoryService ratingRepositoryService) {
        JobCompletionNotificationListener.ratingRepositoryService = ratingRepositoryService;
    }
    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("Creating association");
        Controller.associations = ratingRepositoryService.groupMoviesByUser().stream()
                .map(UserMovies::getMovieIds).toList();
        if(jobExecution.getStatus() == BatchStatus.COMPLETED)
            log.info("Complete");
    }
}
