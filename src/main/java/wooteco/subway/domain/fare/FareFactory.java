package wooteco.subway.domain.fare;

import java.util.List;
import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.exception.domain.FeeException;

public class FareFactory {

    private static final List<Fare> fares = List.of(
            new DefaultFare(),
            new SurchargeFare(),
            new ExtraSurchargeFare()
    );

    public Fare getFare(int distance) {
        return fares.stream()
                .filter(fare -> fare.checkDistanceRange(distance))
                .findFirst()
                .orElseThrow(() -> new FeeException(ExceptionMessage.UNDER_MIN_DISTANCE.getContent()));
    }
}

