package wooteco.subway.member.exception;

import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends HttpException {
    public MemberNotFoundException() {
        super(HttpStatus.UNAUTHORIZED, new ErrorMessage("존재하지 않는 사용자입니다."));
    }
}
