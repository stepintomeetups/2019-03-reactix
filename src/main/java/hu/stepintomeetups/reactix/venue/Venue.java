package hu.stepintomeetups.reactix.venue;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("Venue")
public class Venue {
    @Id
    private String id;

    private String location;

    private String name;
}
