package hu.stepintomeetups.reactix.tracking.repository.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "tracking.kafka")
@Data
public class KafkaConfiguration {
    private String bootstrapServer;

    private String clientId;

    private String groupId;

    private String autoOffsetReset;

    private String acks;
}
