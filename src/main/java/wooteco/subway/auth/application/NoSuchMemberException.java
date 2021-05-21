package wooteco.subway.auth.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NoSuchMemberException extends RuntimeException {
    private static String MESSAGE = "해당 Email로 등록된 회원이 없습니다.";

    public NoSuchMemberException() {
        super(MESSAGE);
    }
}
