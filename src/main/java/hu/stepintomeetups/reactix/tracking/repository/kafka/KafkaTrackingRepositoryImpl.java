package hu.stepintomeetups.reactix.tracking.repository.kafka;

import hu.stepintomeetups.reactix.tracking.repository.TrackingRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverRecord;

@Repository
public class KafkaTrackingRepositoryImpl implements TrackingRepository {
    private static final Integer NO_KEY = null;

    private final KafkaConsumer kafkaConsumer;

    private final KafkaProducer kafkaProducer;

    public KafkaTrackingRepositoryImpl(final KafkaConsumer kafkaConsumer, final KafkaProducer kafkaProducer) {
        this.kafkaConsumer = kafkaConsumer;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public Flux<String> getTicketPurchaseStream() {
        return eventsFilteredByTopic(Topics.Ticket.PURCHASE);
    }

    @Override
    public Flux<String> getConcertSearchStream() {
        return eventsFilteredByTopic(Topics.Concert.SEARCH);
    }

    @Override
    public Flux<String> getConcertLoadStream() {
        return eventsFilteredByTopic(Topics.Concert.LOAD);
    }

    @Override
    public Flux<String> getConcertUnloadStream() {
        return eventsFilteredByTopic(Topics.Concert.UNLOAD);
    }

    @Override
    public Mono<Void> saveTicketPurchaseStream(final Flux<String> concertIds) {
        return publishOnTopic(Topics.Ticket.PURCHASE, concertIds);
    }

    @Override
    public Mono<Void> saveConcertSearchStream(final Flux<String> artistNames) {
        return publishOnTopic(Topics.Concert.SEARCH, artistNames);
    }

    @Override
    public Mono<Void> saveConcertLoadStream(final Flux<String> concertIds) {
        return publishOnTopic(Topics.Concert.LOAD, concertIds);
    }

    @Override
    public Mono<Void> saveConcertUnloadStream(final Flux<String> concertIds) {
        return publishOnTopic(Topics.Concert.UNLOAD, concertIds);
    }

    private Flux<String> eventsFilteredByTopic(final String topic) {
        return Flux.from(kafkaConsumer.getEventStream())
                .filter(record -> record.topic().equals(topic))
                .map(ReceiverRecord::value);
    }

    private Mono<Void> publishOnTopic(final String topic, final Flux<String> events) {
        final Flux<ProducerRecord<Integer, String>> records = events.map(contents -> new ProducerRecord<>(topic, NO_KEY, contents));

        return kafkaProducer.publish(records);
    }
}
