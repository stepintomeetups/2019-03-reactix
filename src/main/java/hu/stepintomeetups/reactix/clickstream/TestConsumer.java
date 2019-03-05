package hu.stepintomeetups.reactix.clickstream;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class TestConsumer {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC = "test";

    private final Flux<String> messages;

    public TestConsumer() {
        final ReceiverOptions<Integer, String> receiverOptions = setupReceiverOptions();

        // share() is the key part - multiple invocations of the consumeMessages method is possible
        // using a shared Flux.
        this.messages = KafkaReceiver.create(receiverOptions).receive()
                .doOnNext(record -> record.receiverOffset().acknowledge())
                .map(record -> record.value())
                .share();
    }

    public Flux<String> consumeMessages() {
        return messages;
    }

    private ReceiverOptions<Integer, String> setupReceiverOptions() {
        final Map<String, Object> properties = new HashMap<>();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "test-consumer");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return ReceiverOptions.<Integer, String>create(properties)
                .subscription(Collections.singletonList(TOPIC));
    }
}
