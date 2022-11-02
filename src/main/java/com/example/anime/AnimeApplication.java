package com.example.anime;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;

@EnableBatchProcessing
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@PropertySource("classpath:sql.properties")
@Slf4j
public class AnimeApplication {

	public static ApplicationContext ctx;

	public static void main(String[] args) throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		ctx = SpringApplication.run(AnimeApplication.class, args);
		initiateJob();
	}

	/**
	 * Initiates the job based on the jobName
	 *
	 * @return
	 *
	 * @throws JobParametersInvalidException
	 * @throws JobInstanceAlreadyCompleteException
	 * @throws JobRestartException
	 * @throws JobExecutionAlreadyRunningException
	 */
	public static void initiateJob() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		String jobName = ctx.getEnvironment().getProperty("JOB_NAME", "DUMMY");
		JobExecution jobExecution = launchJob(jobName);
		log.info("Anime data batch ran with jobName {} is finished with execution status {}", jobName,
				jobExecution.getExitStatus().getExitDescription());
	}

	/**
	 * Triggers the job with empty params
	 *
	 * @param jobName
	 * @throws JobParametersInvalidException
	 * @throws JobInstanceAlreadyCompleteException
	 * @throws JobRestartException
	 * @throws JobExecutionAlreadyRunningException
	 */
	private static JobExecution launchJob(String jobName) throws JobExecutionAlreadyRunningException,
			JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		log.info("Launching job " + jobName);
		JobLauncher jobLauncher = (JobLauncher) ctx.getBean("jobLauncher");
		Job job = (Job) ctx.getBean(jobName);
		JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
		return jobExecution;
	}

}
