package hu.stepintomeetups.reactix.concert.search.stream;

import hu.stepintomeetups.reactix.artist.repository.Artist;
import hu.stepintomeetups.reactix.artist.repository.ArtistRepository;
import hu.stepintomeetups.reactix.tracking.repository.TrackingRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class ConcertSearchStreamServiceImpl implements ConcertSearchStreamService {
    private final TrackingRepository trackingRepository;

    private final Flux<Artist> searchStream;

    public ConcertSearchStreamServiceImpl(final ArtistRepository artistRepository, final TrackingRepository trackingRepository) {
        this.trackingRepository = trackingRepository;

        this.searchStream = trackingRepository.getConcertSearchStream()
                .delayElements(Duration.ofSeconds(2))
                .sample(Duration.ofSeconds(5))
                .flatMap(artistRepository::findByName)
                .share();
    }

    @Override
    public Mono<Void> publishSearchEvents(final Flux<String> artistNames) {
        return trackingRepository.saveConcertSearchStream(artistNames);
    }

    @Override
    public Flux<Artist> streamSearchEvents() {
        return Flux.from(searchStream);
    }
}
