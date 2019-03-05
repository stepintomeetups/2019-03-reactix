package hu.stepintomeetups.reactix.concert.detail;

import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.RenderingResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Controller
public class ConcertDetailController {
    private final ConcertDetailService concertDetailService;

    public ConcertDetailController(final ConcertDetailService concertDetailService) {
        this.concertDetailService = concertDetailService;
    }

    public Mono<RenderingResponse> getDetailsOfConcertById(final ServerRequest request) {
        return Mono.justOrEmpty(request.pathVariables().get("id"))
                .flatMap(concertDetailService::getConcertById)
                .flatMap(detail -> RenderingResponse.create("concert")
                                    .modelAttribute("concert", detail)
                                    .build())
                .switchIfEmpty(RenderingResponse.create("concert").build());
    }
}
