package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public final class NotFoundLineException extends SubwayException{

    public NotFoundLineException() {
        super("존재하지 않는 노선입니다.", HttpStatus.NOT_FOUND);
    }
}
