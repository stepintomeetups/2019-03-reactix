package hu.stepintomeetups.reactix.concert.view.stream;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ConcertViewStreamService {
    Flux<Integer> getViewStreamForConcert(String concertId);

    Mono<Void> publishLoadEvents(Flux<String> concertIds);

    Mono<Void> publishUnloadEvents(Flux<String> concertIds);
}
