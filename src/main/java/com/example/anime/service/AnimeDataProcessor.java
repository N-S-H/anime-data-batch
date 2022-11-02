package com.example.anime.service;

import com.example.anime.model.Anime;
import com.example.anime.utils.AnimeBatchUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnimeDataProcessor implements ItemProcessor<List<String>,List<Anime>> {

    @Autowired
    AnimeBatchUtils utils;

    public static long totalProcessedRecordsCount=0l;

    @Override
    public List<Anime> process(List<String> records) throws Exception {
        if(records==null || records.isEmpty()) return null;

        List<Anime> animeList = records.stream().map(line -> utils.transformFileRecordToAnime(line))
                .filter(animeRecord->animeRecord!=null)
                .collect(Collectors.toList());

        if(animeList!=null && !animeList.isEmpty()) incrementTotalRecordsProcessed(animeList.size());

        return animeList;
    }

    public synchronized void incrementTotalRecordsProcessed(int currentBatchSize) {
        totalProcessedRecordsCount+=currentBatchSize;
        log.info("{} records have been processed so far",totalProcessedRecordsCount);
    }
}
