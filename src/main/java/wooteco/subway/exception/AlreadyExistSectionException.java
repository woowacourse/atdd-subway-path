package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public final class AlreadyExistSectionException extends SubwayException {

    public AlreadyExistSectionException() {
        super("상행, 하행이 대상 노선에 둘 다 존재합니다.", HttpStatus.BAD_REQUEST);
    }
}
