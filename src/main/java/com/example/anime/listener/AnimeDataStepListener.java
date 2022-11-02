package com.example.anime.listener;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
@Slf4j
public class AnimeDataStepListener implements StepExecutionListener {

    public Date startDate;
    public Date endDate;

    @Override
    public void beforeStep(StepExecution stepExecution) {
       startDate = Date.from(Instant.now());
       log.info("The anime data load step started at: "+startDate);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        endDate = Date.from(Instant.now());
        return ExitStatus.COMPLETED;
    }
}
