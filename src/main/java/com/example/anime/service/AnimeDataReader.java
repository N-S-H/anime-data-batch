package com.example.anime.service;

import com.example.anime.utils.AnimeBatchConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AnimeDataReader implements ItemReader<List<String>> {

    @Value("${JOB_NAME}")
    private String jobName;

    @Value("${CHUNK_SIZE}")
    private int chunkSize;

    @Autowired
    FeedFileReader fileReader;

    BufferedReader br;

    public static long totalRecordsRead=0l;

    @PostConstruct
    public void init() {
        if(jobName.equals(AnimeBatchConstants.ANIME_DATA_LOAD_JOB_NAME)) {
            br = fileReader.createAnimeFileReader();
        }
    }

    @Override
    public List<String> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        List<String> lines = readLinesFromFeed();
        if(lines==null || lines.isEmpty()) {
            log.info("End of the current feed file is reached");
            return null;
        }
        incrementReadCount(lines.size());
        return lines;
    }

    public List<String> readLinesFromFeed() throws IOException {
        if(br==null) return null;
        List<String> records = new ArrayList<>();
        int currentRecordCount=0;
        String currentLine;
        while(currentRecordCount<chunkSize && (currentLine=br.readLine())!=null) {
            records.add(currentLine);
            currentRecordCount++;
        }
        return records;
    }

    public synchronized void incrementReadCount(int currentBatchSize) {
        totalRecordsRead+=currentBatchSize;
        log.info("{} records have been read so far",totalRecordsRead);
    }
}
