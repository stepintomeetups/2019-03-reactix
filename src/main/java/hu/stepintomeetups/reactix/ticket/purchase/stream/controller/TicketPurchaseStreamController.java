package hu.stepintomeetups.reactix.ticket.purchase.stream.controller;

import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

@Controller
public class TicketPurchaseStreamController {
    private static final String ARTIST_ID_PARAMETER = "artistId";

    private final TicketPurchaseStreamService purchaseStreamService;

    private final FluxSink<String> purchaseEventSink;

    public TicketPurchaseStreamController(final TicketPurchaseStreamService purchaseStreamService) {
        this.purchaseStreamService = purchaseStreamService;

        final EmitterProcessor<String> purchaseEventEmitter = EmitterProcessor.create(10, false);

        purchaseStreamService.publishPurchaseEvents(purchaseEventEmitter)
                .subscribe();

        purchaseEventSink = purchaseEventEmitter.sink(FluxSink.OverflowStrategy.DROP);
    }

    public Mono<ServerResponse> getPurchaseStream(final ServerRequest request) {
        final Flux<PurchaseResponse> stream = Mono.justOrEmpty(request.queryParam(ARTIST_ID_PARAMETER))
                .flatMapMany(purchaseStreamService::streamPurchaseEventsForArtist)
                .map(PurchaseResponse::fromConcertId);

        return ServerResponse.ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(stream, PurchaseResponse.class);
    }

    public Mono<ServerResponse> postPurchaseEvent(final ServerRequest request) {
        return Mono.justOrEmpty(request.queryParam("concertId"))
                .doOnNext(purchaseEventSink::next)
                .then(ServerResponse.ok().build());
    }

    @Value
    private static final class PurchaseResponse {
        private final String concertId;

        private static PurchaseResponse fromConcertId(final String concertId) {
            return new PurchaseResponse(concertId);
        }
    }
}
