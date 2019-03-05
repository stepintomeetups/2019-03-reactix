package hu.stepintomeetups.reactix.tracking.repository.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaProducer {
    private final KafkaSender<Integer, String> sender;

    public KafkaProducer(final KafkaConfiguration configuration) {
        sender = setupSender(configuration);
    }

    public Mono<Void> publish(final Flux<ProducerRecord<Integer, String>> events) {
        return sender.createOutbound()
                .send(events)
                .then();
    }

    private KafkaSender<Integer, String> setupSender(final KafkaConfiguration configuration) {
        return KafkaSender.create(setupSenderOptions(configuration));
    }

    private SenderOptions<Integer, String> setupSenderOptions(final KafkaConfiguration configuration) {
        final Map<String, Object> properties = new HashMap<>();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.getBootstrapServer());
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, configuration.getClientId());
        properties.put(ProducerConfig.ACKS_CONFIG, configuration.getAcks());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return SenderOptions.create(properties);
    }
}
