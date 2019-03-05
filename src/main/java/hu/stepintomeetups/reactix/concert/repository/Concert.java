package hu.stepintomeetups.reactix.concert.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "Concert")
@AllArgsConstructor
public class Concert {
    @Id
    private String id;

    private LocalDateTime time;

    private String artistId;

    private String title;

    private String venueId;
}
