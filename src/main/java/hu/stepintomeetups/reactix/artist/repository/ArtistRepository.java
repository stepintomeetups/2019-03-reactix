package hu.stepintomeetups.reactix.artist.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ArtistRepository extends ReactiveMongoRepository<Artist, String> {
    Mono<Artist> findByName(String name);
}
