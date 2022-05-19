package wooteco.subway.domain.graph;

import org.springframework.stereotype.Component;

@Component
public class Cashier {

    private static final long BASE_FARE = 1250;

    public Long calculateFare(int distance) {
        if (distance <= 10) {
            return BASE_FARE;
        }

        if (distance <= 50) {
            return BASE_FARE + ((distance - 10 - 1) / 5 + 1) * 100;
        }

        long baseOverFifty = BASE_FARE + 40 / 5 * 100L;
        baseOverFifty += ((distance - 50 - 1) / 8 + 1) * 100L;
        return baseOverFifty;
    }
}
