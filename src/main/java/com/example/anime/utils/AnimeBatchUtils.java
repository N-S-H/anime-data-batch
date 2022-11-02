package com.example.anime.utils;

import com.example.anime.model.Anime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

@Component
@Slf4j
public class AnimeBatchUtils {

    public Pattern csvParser = Pattern.compile(AnimeBatchConstants.GENERIC_DELIMITER_PATTERN);
    public Anime transformFileRecordToAnime(String line) {
        String[] columns = csvParser.split(line);
        if(columns.length!=AnimeBatchConstants.TOTAL_EXPECTED_COLUMNS_IN_FEED) {
            log.error("The current column count is not equals to the total expected column count for line {}",line);
            return null;
        }
        Anime animeEntry = new Anime();
        animeEntry.setId(columns[0]);
        animeEntry.setOriginalTitle(columns[1]);
        animeEntry.setSynonyms(columns[2]);
        animeEntry.setEnglishTitle(columns[4]);
        animeEntry.setSynopsis(columns[5]);
        animeEntry.setDisplayType(columns[6]);
        animeEntry.setEpisodeCount(parseIntegerInFeed(columns[7]));
        animeEntry.setStatus(columns[8]);
        animeEntry.setStartAired(parseAiredDateInFeed(columns[9]));
        animeEntry.setEndAired(parseAiredDateInFeed(columns[10]));
        animeEntry.setPremiered(columns[11]);
        animeEntry.setProducers(columns[13]);
        animeEntry.setLicensors(columns[14]);
        animeEntry.setStudios(columns[15]);
        animeEntry.setAnimeSource(columns[16]);
        animeEntry.setGenres(columns[17]);
        animeEntry.setThemes(columns[18]);
        animeEntry.setDurationInMinutes(parseIntegerInFeed(columns[20]));
        animeEntry.setScore(parseDoubleInFeed(columns[22]));
        animeEntry.setScoredUsers(parseLongInFeed(columns[23]));
        animeEntry.setShowRank(parseIntegerInFeed(columns[24]));
        animeEntry.setPopularity(parseIntegerInFeed(columns[25]));
        animeEntry.setMembers(parseLongInFeed(columns[26]));
        animeEntry.setFavorites(parseLongInFeed(columns[27]));
        return animeEntry;
    }

    private Integer parseIntegerInFeed(String entry) {
        try {
            Integer value = Integer.parseInt(entry);
            return value;
        } catch (Exception e) {
            log.error("Exception {} occurred while parsing {} entry to Integer in the feed",e.getMessage(),entry);
            return null;
        }
    }

    private Long parseLongInFeed(String entry) {
        try {
            Long value = Long.parseLong(entry);
            return value;
        } catch (Exception e) {
            log.error("Exception {} occurred while parsing {} entry to Long in the feed",e.getMessage(),entry);
            return null;
        }
    }

    private Double parseDoubleInFeed(String entry) {
        try {
            Double value = Double.parseDouble(entry);
            return value;
        } catch (Exception e) {
            log.error("Exception {} occurred while parsing {} entry to Double in the feed",e.getMessage(),entry);
            return null;
        }
    }

    private Date parseAiredDateInFeed(String dateEntry) {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy");
        try {
            Date date = format.parse(dateEntry);
            return date;
        } catch (ParseException e) {
            log.error("Exception {} occurred while parsing entry {} to date",e.getMessage(),dateEntry);
            return null;
        }
    }
}
