package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public final class DuplicateStationException extends SubwayException {

    public DuplicateStationException() {
        super("이미 존재하는 역 이름입니다.", HttpStatus.BAD_REQUEST);
    }
}
