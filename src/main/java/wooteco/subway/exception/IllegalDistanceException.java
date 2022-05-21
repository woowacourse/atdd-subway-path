package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public final class IllegalDistanceException extends SubwayException {

    public IllegalDistanceException() {
        super("역 사이에 새로운 역을 등록할 경우, 기존 역 사이 길이보다 크거나 같으면 등록할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
