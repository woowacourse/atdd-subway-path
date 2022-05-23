package wooteco.subway.domain.fare;

import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.exception.domain.FeeException;

public class FareFactory {

    private static final int MINIMUM_DISTANCE = 0;
    private static final int MAXIMUM_DISTANCE = 10;
    private static final int EXTRA_MAXIMUM_DISTANCE = 50;

    public Fare getFare(int distance, int extraFare) {
        if (distance <= MINIMUM_DISTANCE) {
            throw new FeeException(ExceptionMessage.UNDER_MIN_DISTANCE.getContent());
        }
        if (distance <= MAXIMUM_DISTANCE) {
            return new DefaultFare(distance, extraFare);
        }
        if (distance <= EXTRA_MAXIMUM_DISTANCE) {
            return new SurchargeFare(distance, extraFare);
        }
        return new ExtraSurchargeFare(distance, extraFare);
    }
}
