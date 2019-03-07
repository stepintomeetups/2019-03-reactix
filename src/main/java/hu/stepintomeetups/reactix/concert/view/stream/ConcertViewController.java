package hu.stepintomeetups.reactix.concert.view.stream;

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
public class ConcertViewController {
    private static final String CONCERT_ID_PARAMETER = "concertId";

    private final ConcertViewStreamService viewStreamService;

    private final FluxSink<String> loadEventSink;

    private final FluxSink<String> unloadEventSink;

    public ConcertViewController(final ConcertViewStreamService viewStreamService) {
        this.viewStreamService = viewStreamService;

        final EmitterProcessor<String> loadEventEmitter = EmitterProcessor.create(10, false);
        final EmitterProcessor<String> unloadEventEmitter = EmitterProcessor.create(10, false);

        viewStreamService.publishLoadEvents(loadEventEmitter)
                .subscribe();

        viewStreamService.publishUnloadEvents(unloadEventEmitter)
                .subscribe();

        loadEventSink = loadEventEmitter.sink();
        unloadEventSink = unloadEventEmitter.sink();
    }

    public Mono<ServerResponse> getViewStream(final ServerRequest request) {
        final Flux<ConcertViewResponse> stream = Mono.justOrEmpty(request.queryParam(CONCERT_ID_PARAMETER))
                .flatMapMany(viewStreamService::getViewStreamForConcert)
                .map(count -> count > 1 ? count - 1 : 0)
                .map(ConcertViewResponse::fromCount);

        return ServerResponse.ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(stream, ConcertViewResponse.class);
    }

    public Mono<ServerResponse> postLoadEvent(final ServerRequest request) {
        return Mono.justOrEmpty(request.queryParam(CONCERT_ID_PARAMETER))
                .doOnNext(loadEventSink::next)
                .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> deleteUnloadEvent(final ServerRequest request) {
        return Mono.justOrEmpty(request.queryParam(CONCERT_ID_PARAMETER))
                .doOnNext(unloadEventSink::next)
                .then(ServerResponse.ok().build());
    }

    @Value
    private static final class ConcertViewResponse {
        private final Integer viewCount;

        private static ConcertViewResponse fromCount(final Integer count) {
            return new ConcertViewResponse(count);
        }
    }
}
