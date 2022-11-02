package com.example.anime.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Anime {
    String id;
    String originalTitle;
    String synonyms;
    String englishTitle;
    String synopsis;
    String displayType;
    Integer episodeCount;
    String status;
    Date startAired;
    Date endAired;
    String premiered;
    String producers;
    String licensors;
    String studios;
    String animeSource;
    String genres;
    String themes;
    Integer durationInMinutes;
    Double score;
    Long scoredUsers;
    Integer showRank;
    Integer popularity;
    Long members;
    Long favorites;
}
