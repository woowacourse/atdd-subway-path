package wooteco.subway.member.exception;

import org.springframework.http.HttpStatus;

public class TokenNotValidException extends HttpException {
    public TokenNotValidException() {
        super(HttpStatus.UNAUTHORIZED, new ErrorMessage("유효한 토큰이 아닙니다."));
    }
}
