package hu.stepintomeetups.reactix.concert;

import hu.stepintomeetups.reactix.concert.detail.ConcertDetailController;
import hu.stepintomeetups.reactix.concert.search.ConcertSearchByArtistController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RenderingResponse;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ConcertRouting {
    private static final String WEB_PREFIX = "/concerts";

    private static final String API_PREFIX = "/api/concerts";

    @Autowired
    private ConcertSearchByArtistController searchByArtistController;

    @Autowired
    private ConcertDetailController detailController;

    @Bean
    public RouterFunction<RenderingResponse> concertPageRouting() {
        return nest(path(WEB_PREFIX),
                route(GET("/"), searchByArtistController::getConcertsByArtist)
                .andRoute(GET("/{id}"), detailController::getDetailsOfConcertById));
    }
}
