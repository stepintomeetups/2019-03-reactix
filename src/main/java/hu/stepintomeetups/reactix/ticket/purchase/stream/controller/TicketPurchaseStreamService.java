package hu.stepintomeetups.reactix.ticket.purchase.stream.controller;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TicketPurchaseStreamService {
    Flux<String> streamPurchaseEventsForArtist(String artistId);

    Mono<Void> publishPurchaseEvents(Flux<String> concertIds);
}
