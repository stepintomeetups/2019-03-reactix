package hu.stepintomeetups.reactix.concert.detail;

import hu.stepintomeetups.reactix.artist.repository.Artist;
import hu.stepintomeetups.reactix.artist.repository.ArtistRepository;
import hu.stepintomeetups.reactix.concert.repository.Concert;
import hu.stepintomeetups.reactix.concert.repository.ConcertRepository;
import hu.stepintomeetups.reactix.venue.VenueRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

import static java.util.Objects.requireNonNull;

@Service
public class ConcertDetailServiceImpl implements ConcertDetailService {
    private final ConcertRepository concerts;

    private final ArtistRepository artists;

    private final VenueRepository venues;

    public ConcertDetailServiceImpl(final ConcertRepository concerts, final ArtistRepository artists,
                                    final VenueRepository venues) {
        this.concerts = concerts;
        this.artists = artists;
        this.venues = venues;
    }

    @Override
    public Flux<ConcertDetail> getConcertsForArtist(final String artistName) {
        return artists.findByName(requireNonNull(artistName))
                .map(Artist::getId)
                .flatMapMany(concerts::findByArtistId)
                .flatMap(this::detailFromConcert)
                .sort(Comparator.comparing(ConcertDetail::getTime));
    }

    @Override
    public Mono<ConcertDetail> getConcertById(final String concertId) {
        return concerts.findById(requireNonNull(concertId))
                .flatMap(this::detailFromConcert);
    }

    private Mono<ConcertDetail> detailFromConcert(final Concert concert) {
        return artists.findById(concert.getArtistId())
                .zipWith(venues.findById(concert.getVenueId()))
                .map(tup -> new ConcertDetail(concert.getId(), concert.getTime(), tup.getT1(),
                                              concert.getTitle(), tup.getT2()));
    }
}
