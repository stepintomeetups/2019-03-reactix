package hu.stepintomeetups.reactix.landing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RenderingResponse;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LandingRouting {
    @Bean
    public RouterFunction<RenderingResponse> landingRouter() {
        return route(GET("/"), request -> RenderingResponse.create("landing").build());
    }
}
