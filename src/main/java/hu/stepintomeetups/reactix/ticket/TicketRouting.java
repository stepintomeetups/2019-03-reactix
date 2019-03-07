package hu.stepintomeetups.reactix.ticket;

import hu.stepintomeetups.reactix.ticket.purchase.stream.controller.TicketPurchaseStreamController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TicketRouting {
    private static final String API_PREFIX = "/api/tickets";

    @Autowired
    private TicketPurchaseStreamController purchaseStreamController;

    @Bean
    public RouterFunction<ServerResponse> ticketApiRouting() {
        return route()
                .path(API_PREFIX, builder -> builder
                        .path("/purchase/stream", this::purchaseStreamRouting)
                        .build())
                .build();
    }

    private RouterFunction<ServerResponse> purchaseStreamRouting() {
        return route(GET("/"), purchaseStreamController::getPurchaseStream)
                .andRoute(POST("/"), purchaseStreamController::postPurchaseEvent);
    }
}
