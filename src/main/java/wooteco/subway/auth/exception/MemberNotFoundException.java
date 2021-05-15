package wooteco.subway.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MemberNotFoundException extends MemberException{
    public MemberNotFoundException() {
        super("존재하지 않는 사용자입니다.");
    }
}
