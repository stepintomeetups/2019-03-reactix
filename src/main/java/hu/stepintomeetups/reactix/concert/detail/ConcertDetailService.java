package hu.stepintomeetups.reactix.concert.detail;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ConcertDetailService {
    Flux<ConcertDetail> getConcertsForArtist(String name);

    Mono<ConcertDetail> getConcertById(String concertId);
}
