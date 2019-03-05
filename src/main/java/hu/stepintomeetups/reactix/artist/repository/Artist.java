package hu.stepintomeetups.reactix.artist.repository;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@Document(collection = "Artist")
public class Artist {
    @Id
    private String id;

    private String name;

    private Map<String, String> assets;
}
