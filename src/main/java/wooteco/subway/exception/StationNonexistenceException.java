package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StationNonexistenceException extends SubwayException {
    public StationNonexistenceException() {
        super("존재하지 않는 역입니다.");
    }
}
