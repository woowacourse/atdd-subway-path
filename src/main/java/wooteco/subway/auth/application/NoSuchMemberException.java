package wooteco.subway.auth.application;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NoSuchMemberException extends EmptyResultDataAccessException {
    private static String msg = "해당 Email로 등록된 회원이 없습니다.";
    private static int expectedSize = 1;

    public NoSuchMemberException() {
        super(msg, expectedSize);
    }
}
