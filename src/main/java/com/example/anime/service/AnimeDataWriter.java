package com.example.anime.service;

import com.example.anime.model.Anime;
import com.example.anime.repository.AnimeDataRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimeDataWriter implements ItemWriter<List<Anime>> {

    @Autowired
    AnimeDataRepository repository;

    @Override
    public void write(List<? extends List<Anime>> animeLists) throws Exception {
        animeLists.stream().forEach(animeList-> {
            if(animeList!=null && !animeList.isEmpty()) {
                repository.insertAnimeRecordsToDB(animeList);
            }
        });
    }
}
