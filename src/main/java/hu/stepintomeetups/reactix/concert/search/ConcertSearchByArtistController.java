package hu.stepintomeetups.reactix.concert.search;

import hu.stepintomeetups.reactix.artist.repository.Artist;
import hu.stepintomeetups.reactix.artist.repository.ArtistRepository;
import hu.stepintomeetups.reactix.concert.detail.ConcertDetail;
import hu.stepintomeetups.reactix.concert.detail.ConcertDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.RenderingResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class ConcertSearchByArtistController {
    private static final String SEARCH_PARAMETER = "s";

    private final ConcertDetailService concertDetailService;

    private final ArtistRepository artistRepository;

    public ConcertSearchByArtistController(final ConcertDetailService concertDetailService, final ArtistRepository artistRepository) {
        this.concertDetailService = concertDetailService;
        this.artistRepository = artistRepository;
    }

    public Mono<RenderingResponse> getConcertsByArtist(final ServerRequest request) {
        final Mono<String> query = Mono.justOrEmpty(request.queryParam(SEARCH_PARAMETER));

        final Mono<Artist> artist = query.flatMap(artistRepository::findByName);

        final Flux<ConcertDetail> concerts = query.flatMapMany(concertDetailService::getConcertsForArtist);

        return query
                .flatMap(artistName -> RenderingResponse.create("concert-search")
                                        .modelAttribute("artist", artist)
                                        .modelAttribute("concerts", concerts)
                                        .build())
                .switchIfEmpty(RenderingResponse.create("error").build());
    }
}
