package hu.stepintomeetups.reactix.concert.search.stream;

import hu.stepintomeetups.reactix.artist.repository.Artist;
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
public class ConcertSearchStreamController {
    private final ConcertSearchStreamService searchStreamService;

    private final FluxSink<String> searchEventSink;

    public ConcertSearchStreamController(final ConcertSearchStreamService searchStreamService) {
        this.searchStreamService = searchStreamService;

        final EmitterProcessor<String> searchEventEmitter = EmitterProcessor.create(10, false);

        searchStreamService.publishSearchEvents(searchEventEmitter)
                .subscribe();

        searchEventSink = searchEventEmitter.sink(FluxSink.OverflowStrategy.DROP);
    }

    public Mono<ServerResponse> getSearchStream(final ServerRequest request) {
        final Flux<ArtistSearchFeedItem> stream = searchStreamService
                .streamSearchEvents()
                .map(ArtistSearchFeedItem::fromArtist);

        return ServerResponse.ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(stream, ArtistSearchFeedItem.class);
    }

    public Mono<ServerResponse> postSearchEvent(final ServerRequest request) {
        return Mono.justOrEmpty(request.queryParam("artistName"))
                .doOnNext(searchEventSink::next)
                .then(ServerResponse.ok().build());
    }

    @Value
    private static final class ArtistSearchFeedItem {
        private final String name;

        private final String searchCardImage;

        private static ArtistSearchFeedItem fromArtist(final Artist artist) {
            return new ArtistSearchFeedItem(artist.getName(), artist.getAssets().get("searchCardImage"));
        }
    }
}
