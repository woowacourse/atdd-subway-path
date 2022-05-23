package wooteco.subway.domain.fare;

import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.exception.domain.FeeException;

public class FareFactory {

    public Fare getFare(int distance) {
        if (distance <= 0) {
            throw new FeeException(ExceptionMessage.UNDER_MIN_DISTANCE.getContent());
        }
        if (distance <= 10) {
            return new DefaultFare();
        }
        if (distance <= 50) {
            return new SurchargeFare();
        }
        return new ExtraSurchargeFare();
    }
}
