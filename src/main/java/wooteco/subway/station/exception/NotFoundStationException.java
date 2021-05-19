package wooteco.subway.station.exception;

import wooteco.subway.common.exception.BusinessException;

public class NotFoundStationException extends BusinessException {

    public NotFoundStationException(String message) {
        super(message);
    }
}
