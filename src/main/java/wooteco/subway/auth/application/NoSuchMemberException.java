package wooteco.subway.auth.application;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NoSuchMemberException extends EmptyResultDataAccessException {
    private static final String message = "해당하는 email의 회원이 없습니다.";
    private static final int EXPECTED_SIZE = 1;

    public NoSuchMemberException() {
        super(message, EXPECTED_SIZE);
    }
}
