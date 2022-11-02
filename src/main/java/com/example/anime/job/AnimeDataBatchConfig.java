package com.example.anime.job;

import com.example.anime.listener.AnimeDataStepListener;
import com.example.anime.model.Anime;
import com.example.anime.service.AnimeDataProcessor;
import com.example.anime.service.AnimeDataReader;
import com.example.anime.service.AnimeDataStatsGenerator;
import com.example.anime.service.AnimeDataWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.sql.DataSource;
import java.util.List;

@Primary
@Configuration
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class AnimeDataBatchConfig extends DefaultBatchConfigurer {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private AnimeDataReader animeDataReader;

    @Autowired
    private AnimeDataWriter animeDataWriter;

    @Autowired
    private AnimeDataProcessor animeDataProcessor;

    @Autowired
    private AnimeDataStatsGenerator animeDataStatsGenerator;

    @Autowired
    private AnimeDataStepListener stepListener;

    @Value("${credentialsPath}")
    private String credentialsPath;

    @Value("${MAX_THREADS}")
    private int maxThreads;

    @Bean
    public FileSystemResource credentials() {
        return new FileSystemResource(credentialsPath);
    }

    /**
     * Enables parallel processing based on configured number of threads
     *
     * @return
     */
    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(maxThreads);
        return taskExecutor;
    }

    @Bean
    protected Step step1() {
        return stepBuilderFactory.get("step1").<List<String>,List<Anime>>chunk(1).reader(animeDataReader)
                .chunk(1).processor(animeDataProcessor).chunk(1).writer(animeDataWriter).chunk(1)
                .taskExecutor(taskExecutor()).throttleLimit(maxThreads).listener(stepListener).build();
    }

    @Bean
    protected Step step2() {
        return stepBuilderFactory.get("step2").tasklet(animeDataStatsGenerator).build();

    }

    @Bean(name = "animeDataLoad")
    public Job reviewsDataLoad(@Qualifier("step1") Step step1) {
        return jobBuilderFactory.get("animeDataLoad").start(step1).build();
    }

    @Bean(name="animeStatsGenerator")
    public Job documentIdDeletion(@Qualifier("step2") Step step2){
        return jobBuilderFactory.get("animeStatsGenerator").start(step2).build();
    }

    /**
     * Overrides Default Batch Configurer setDataSource to provide dummy
     * implementation
     */
    @Override
    public void setDataSource(DataSource dataSource) {
        // This Batch Configurer ignores any DataSource
    }
}
