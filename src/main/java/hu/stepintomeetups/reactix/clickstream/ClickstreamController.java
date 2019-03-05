package hu.stepintomeetups.reactix.clickstream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ClickstreamController {
    @Autowired
    private TestConsumer testConsumer;

    @GetMapping("/stream")
    public Flux<ServerSentEvent<String>> stream() {
        return testConsumer.consumeMessages()
                .map(message -> ServerSentEvent.<String>builder().data(message).build());
    }
}
