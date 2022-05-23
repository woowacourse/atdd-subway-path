package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class NotNegativeFareException extends SubwayException {

    public NotNegativeFareException() {
        super("지하철 요금이 음수일 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
