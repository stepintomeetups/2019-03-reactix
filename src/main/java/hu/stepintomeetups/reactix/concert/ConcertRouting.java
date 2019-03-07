package hu.stepintomeetups.reactix.concert;

import hu.stepintomeetups.reactix.concert.detail.ConcertDetailController;
import hu.stepintomeetups.reactix.concert.search.ConcertSearchByArtistController;
import hu.stepintomeetups.reactix.concert.search.stream.ConcertSearchStreamController;
import hu.stepintomeetups.reactix.concert.view.stream.ConcertViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RenderingResponse;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

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

    @Autowired
    private ConcertSearchStreamController searchStreamController;

    @Autowired
    private ConcertViewController viewStreamController;

    @Bean
    public RouterFunction<RenderingResponse> concertPageRouting() {
        return nest(path(WEB_PREFIX),
                route(GET("/"), searchByArtistController::getConcertsByArtist)
                .andRoute(GET("/{id}"), detailController::getDetailsOfConcertById));
    }

    @Bean
    public RouterFunction<ServerResponse> concertApiRouting() {
        return route()
                .path(API_PREFIX, builder -> builder
                    .path("/search/stream", this::searchStreamRouting)
                    .path("/view/stream", this::viewStreamRouting)
                    .build())
                .build();
    }

    private RouterFunction<ServerResponse> searchStreamRouting() {
        return route()
                .GET("/", searchStreamController::getSearchStream)
                .POST("/", searchStreamController::postSearchEvent)
                .build();
    }

    private RouterFunction<ServerResponse> viewStreamRouting() {
        return route()
                .GET("/", viewStreamController::getViewStream)
                .POST("/", viewStreamController::postLoadEvent)
                .DELETE("/", viewStreamController::deleteUnloadEvent)
                .build();
    }
}
