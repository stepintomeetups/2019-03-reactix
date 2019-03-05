package hu.stepintomeetups.reactix.concert.detail;

import hu.stepintomeetups.reactix.artist.repository.Artist;
import hu.stepintomeetups.reactix.venue.Venue;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ConcertDetail {
    private String id;

    private LocalDateTime time;

    private Artist artist;

    private String title;

    private Venue venue;
}
