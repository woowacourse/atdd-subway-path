package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public final class NotFoundStationException extends SubwayException{

    public NotFoundStationException() {
        super("해당 지하철역이 등록이 안되어 있습니다.", HttpStatus.NOT_FOUND);
    }
}
