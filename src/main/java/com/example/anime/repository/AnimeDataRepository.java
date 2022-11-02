package com.example.anime.repository;

import com.example.anime.model.Anime;
import com.example.anime.utils.AnimeBatchConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AnimeDataRepository {

    @Autowired
    @Qualifier("dataSourceConfig")
    private DataSource datasource;

    @Value("${JOB_NAME}")
    String jobName;

    @Value("${insert.anime.data.sql}")
    String insertAnimeDataSql;

    JdbcTemplate jdbcTemplate=null;

    @PostConstruct
    public void init() {
        log.info("Initializing the JDBC data source");
        jdbcTemplate = new JdbcTemplate(datasource);
    }

    public void insertAnimeRecordsToDB(List<Anime> animeList) {

        List<Object[]> params = new ArrayList<Object[]>();
        String sql = String.format(insertAnimeDataSql, AnimeBatchConstants.ANIME_TABLE_NAME);
        animeList.stream().forEach(anime-> {
            params.add(new Object[]{anime.getId(),anime.getOriginalTitle(),anime.getSynonyms(),anime.getEnglishTitle(),anime.getSynopsis(),
            anime.getDisplayType(),anime.getEpisodeCount(),anime.getStatus(),convertToSqlDate(anime.getStartAired()),convertToSqlDate(anime.getEndAired()),anime.getPremiered(),
            anime.getProducers(),anime.getLicensors(),anime.getStudios(),anime.getAnimeSource(),anime.getGenres(),anime.getThemes(),
            anime.getDurationInMinutes(),anime.getScore(),anime.getScoredUsers(),anime.getShowRank(),anime.getPopularity(),anime.getMembers(),anime.getFavorites()});
        });
        try {
            jdbcTemplate.batchUpdate(sql, params);
        } catch (Exception e) {
            log.error("Exception while inserting anime data {} ", e.getMessage());
        }
    }

    public Date convertToSqlDate(java.util.Date date) {
        if(date!=null) {
           return new Date(date.getTime());
        }
        return null;
    }
}
