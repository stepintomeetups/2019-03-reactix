package hu.stepintomeetups.reactix.concert.search.stream;

import hu.stepintomeetups.reactix.artist.repository.Artist;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ConcertSearchStreamService {
    Mono<Void> publishSearchEvents(Flux<String> artistNames);

    Flux<Artist> streamSearchEvents();
}
