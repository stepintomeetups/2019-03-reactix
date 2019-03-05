package hu.stepintomeetups.reactix.venue;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface VenueRepository extends ReactiveMongoRepository<Venue, String> {
}
