package hu.stepintomeetups.reactix.tracking.repository.kafka;

import java.util.List;

import static java.util.Arrays.asList;

public final class Topics {

    public final class Ticket {
        public static final String PURCHASE = "tracking.ticket.purchase";

        private Ticket() {
        }
    }

    public final class Concert {
        public static final String SEARCH = "tracking.concert.search";
        public static final String LOAD = "tracking.concert.load";
        public static final String UNLOAD = "tracking.concert.unload";

        private Concert() {
        }
    }

    public static List<String> all() {
        return all;
    }

    private static final List<String> all = asList(
            Ticket.PURCHASE,
            Concert.SEARCH,
            Concert.LOAD,
            Concert.UNLOAD
    );

    private Topics() {
    }
}
