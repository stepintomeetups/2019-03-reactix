package hu.stepintomeetups.reactix.ticket.purchase.stream.controller;

import hu.stepintomeetups.reactix.artist.repository.Artist;
import hu.stepintomeetups.reactix.concert.repository.Concert;
import hu.stepintomeetups.reactix.concert.repository.ConcertRepository;
import hu.stepintomeetups.reactix.tracking.repository.TrackingRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TicketPurchaseStreamServiceImpl implements TicketPurchaseStreamService {
    private final TrackingRepository trackingRepository;

    private final Flux<Concert> purchaseStream;

    public TicketPurchaseStreamServiceImpl(final TrackingRepository trackingRepository, ConcertRepository concertRepository) {
        this.trackingRepository = trackingRepository;

        this.purchaseStream = this.trackingRepository.getTicketPurchaseStream()
                .flatMap(concertRepository::findById)
                .share();
    }

    @Override
    public Flux<String> streamPurchaseEventsForArtist(final String artistId) {
        return  Flux.from(purchaseStream)
                .filter(concert -> concert.getArtistId().equals(artistId))
                .map(Concert::getId);
    }

    @Override
    public Mono<Void> publishPurchaseEvents(Flux<String> concertIds) {
        return trackingRepository.saveTicketPurchaseStream(concertIds);
    }
}
