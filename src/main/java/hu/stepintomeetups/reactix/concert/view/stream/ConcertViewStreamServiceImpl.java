package hu.stepintomeetups.reactix.concert.view.stream;

import hu.stepintomeetups.reactix.tracking.repository.TrackingRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Service
public class ConcertViewStreamServiceImpl implements ConcertViewStreamService {
    private static final Integer INCREASE = 1;
    private static final Integer DECREASE = -1;

    private final TrackingRepository trackingRepository;

    private final Flux<Tuple2<String, Integer>> eventStream;

    public ConcertViewStreamServiceImpl(final TrackingRepository trackingRepository) {
        this.trackingRepository = trackingRepository;

        final Flux<Tuple2<String, Integer>> load = trackingRepository.getConcertLoadStream()
                .map(concertId -> Tuples.of(concertId, INCREASE));
        final Flux<Tuple2<String, Integer>> unload = trackingRepository.getConcertUnloadStream()
                .map(concertId -> Tuples.of(concertId, DECREASE));

        eventStream = load.mergeWith(unload).share()
                .groupBy(Tuple2::getT1)
                .flatMap(this::accumulate)
                .share();
    }

    @Override
    public Flux<Integer> getViewStreamForConcert(final String concertId) {
        return Flux.from(eventStream)
                .filter(event -> event.getT1().equals(concertId))
                .map(Tuple2::getT2)
                .log();
    }

    @Override
    public Mono<Void> publishLoadEvents(final Flux<String> concertIds) {
        return trackingRepository.saveConcertLoadStream(concertIds);
    }

    @Override
    public Mono<Void> publishUnloadEvents(final Flux<String> concertIds) {
        return trackingRepository.saveConcertUnloadStream(concertIds);
    }

    private Flux<Tuple2<String, Integer>> accumulate(final GroupedFlux<String, Tuple2<String, Integer>> stream) {
        return stream.scan((acc, curr) -> Tuples.of(acc.getT1(), acc.getT2() + curr.getT2()));
    }
}
