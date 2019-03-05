package hu.stepintomeetups.reactix.concert.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ConcertRepository extends ReactiveMongoRepository<Concert, String> {
    Flux<Concert> findByVenueId(String venueId);

    Flux<Concert> findByArtistId(String artistId);
}
