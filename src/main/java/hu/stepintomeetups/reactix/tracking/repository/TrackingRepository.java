package hu.stepintomeetups.reactix.tracking.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TrackingRepository {
    Flux<String> getConcertSearchStream();

    Flux<String> getTicketPurchaseStream();

    Flux<String> getConcertLoadStream();

    Flux<String> getConcertUnloadStream();

    Mono<Void> saveTicketPurchaseStream(Flux<String> concertIds);

    Mono<Void> saveConcertSearchStream(Flux<String> artistNames);

    Mono<Void> saveConcertLoadStream(Flux<String> concertIds);

    Mono<Void> saveConcertUnloadStream(Flux<String> concertIds);
}
