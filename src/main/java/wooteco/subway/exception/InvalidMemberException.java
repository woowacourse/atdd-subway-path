package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidMemberException extends SubwayException {
    public InvalidMemberException(String email) {
        super(email + "에 해당하는 회원이 없습니다.");
    }
}
