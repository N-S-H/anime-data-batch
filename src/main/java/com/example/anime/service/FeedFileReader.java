package com.example.anime.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
@Slf4j
public class FeedFileReader {


    public BufferedReader createAnimeFileReader() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("anime.csv"));
            return br;
        } catch (IOException e) {
            log.error("Exception {} occurred while setting up the anime feed file reader",e.getMessage());
            return null;
        }
    }


}
