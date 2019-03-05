package hu.stepintomeetups.reactix.tracking.repository.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaConsumer {
    private final Flux<ReceiverRecord<Integer, String>> eventStream;

    public KafkaConsumer(final KafkaConfiguration configuration) {
        this.eventStream = setupEventStream(configuration);
    }

    public Flux<ReceiverRecord<Integer, String>> getEventStream() {
        return this.eventStream;
    }

    private Flux<ReceiverRecord<Integer, String>> setupEventStream(final KafkaConfiguration configuration) {
        final ReceiverOptions<Integer, String> receiverOptions = setupReceiverOptions(configuration);

        // share() is the key part - multiple invocations of the consumeMessages method is possible
        // using a shared Flux.
        return KafkaReceiver.create(receiverOptions).receive()
                .doOnNext(record -> record.receiverOffset().acknowledge())
                .share();
    }

    private ReceiverOptions<Integer, String> setupReceiverOptions(final KafkaConfiguration configuration) {
        final Map<String, Object> properties = new HashMap<>();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.getBootstrapServer());
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, configuration.getClientId());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, configuration.getGroupId());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, configuration.getAutoOffsetReset());

        return ReceiverOptions.<Integer, String>create(properties)
                .subscription(Topics.all());
    }
}
