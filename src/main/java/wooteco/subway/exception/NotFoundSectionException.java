package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public final class NotFoundSectionException extends SubwayException{

    public NotFoundSectionException() {
        super("일치하는 Section이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
