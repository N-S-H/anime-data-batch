package com.example.anime.utils;

import org.springframework.stereotype.Component;

@Component
public class AnimeBatchConstants {

    public static final String ANIME_DATA_LOAD_JOB_NAME="animeDataLoad";
    public static final String ANIME_STATS_GENERATOR_JOB_NAME="animeStatsGenerator";
    public static final Integer TOTAL_EXPECTED_COLUMNS_IN_FEED=28;

    public static final String ANIME_TABLE_NAME="anime_data";
    public static final String GENERIC_DELIMITER_PATTERN = ",(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))";
}
